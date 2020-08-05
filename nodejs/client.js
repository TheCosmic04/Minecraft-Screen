var net = require("net");
var robot = require("robotjs");
var sleep = require("system-sleep");
var nearestColor = require('nearest-color');
var Jimp = require("jimp");

var readline = require("readline");

var socket = null;
var remote_server = {host: "localhost", port:"1234", state: "[setup]"};
var packet_util = require("./packets-util.js");

var screen = {width: 0, height: 0, sucess: false};
var colors = require("./maps/block-colors.json");
var name_to_id = require("./maps/name_to_id.json");

var options = {
    running: true,
    fsp: 20
}


setup();

function setup(state = 0) {
    console.clear();
    logServerInfo();

    let rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
      });

    switch (state) {
        case 0:
            log(`\nPress enter to use default.\n`)
            rl.question("Host: ", (answer) => {
                if (answer.length > 0) remote_server.host = answer;
                rl.close();
            });
    
            rl.on("close", () => {
                setup(++state);
            });
            break;

        case 1:
            log(`\nPress enter to use default.\n`)
            let isValid = false;
            rl.question("Port: ", (answer) => {
                isValid = !isNaN(answer);
                if (answer.length > 0 && isValid) 
                    remote_server.port = answer;
                rl.close();
            });

            rl.on("close", () => {
                if (!isValid) 
                    setup(state);
                else {
                    remote_server.state = "[connecting...]";
                    setup(++state);
                }
                
            });
            break;

        case 2:
            rl.close();
            log(`\nConnecting to ${remote_server.host}:${remote_server.port}\n`);
            socket = net.createConnection({host:remote_server.host, port:remote_server.port});

            socket.once("ready", () => {
                remote_server.state = "[connected]";
                setup(++state);
            })
            socket.on("error", (err) => {
                log(`\nCouldnt connect to ${remote_server.host}:${remote_server.port}!\n`);
                process.exit();
            });
            break;

        case 3:
            log(`Connected to ${remote_server.host}:${remote_server.port}\n`);
            rl.question("Screen token: ", (answer) => {
                rl.close();
                if (answer == null || answer == "")
                    setup(state);
                else
                    initilizeConnection(socket, answer);
            });
            break;

        case 4:
            rl.close();
            log("\nSucessfully connected!\n")
            log(`Mc screen size: ${screen.width}x${screen.height}`);
            start();
    }
    
}

function start() {
    console.log("\nStarting...");
    let interval = setInterval(function() {
        if (!options.running)
            clearInterval(this);

        update(options);
    }, 1000/options.fps)
    
    // while (options.running) {
    //     //console.log(options)
    //     update(options);

    //     sleep(1000/options.fsp);
    // }
}

function update() {
    //console.log("working...");
    
    let screenSize = robot.getScreenSize();
    let img = robot.screen.capture(0, 0, screenSize.width, screenSize.height);

    let height = getHeight(screenSize.width, screenSize.height, screen.width);

    let jimp_obj = bitmapToJim(img, screenSize.width, screenSize.height);
    parseImg(jimp_obj, screen.width, height);
}

function parseImg(obj, width, height) {

    Jimp.read(obj).then(img => {
        let pixels = packet_util.genArray2d(width, height, 0);
        img.resize(width, height);

        for (let y = 0; y < height; y++) {
            for (let x = 0; x < width; x++) {
                let rgb = Jimp.intToRGBA( img.getPixelColor(x, y) );

                //let hex = rgbToHex(rgb.r, rgb.g, rgb.b);
                //console.log(getNearestColor(rgb.r, rgb.g, rgb.b));
                pixels[y][x] = name_to_id[getNearestColor(rgb.r, rgb.g, rgb.b)];
            }
        }

        socket.write(packet_util.packet(0x02, packet_util.UInt8Array2d(pixels)));
    });
}

function initilizeConnection(socket, token) {
    socket.on("data", data => {

        if (Buffer.isBuffer(data)) 
            parsePacket(data);
    });

    socket.on("end", () => {
        remote_server.state = "[closed]";

            console.clear();
            logServerInfo();

            log("\nDisconnected by the server!\n")

            process.exit();
    });

    socket.write(packet_util.packet(0x01, Buffer.from(token)));
}


function parsePacket(buffer) {
    if (!Buffer.isBuffer(buffer) || buffer.length < 6) 
        return;
    
    let id = buffer.readInt8();
    let packet_size = buffer.readInt32BE(1);
    let data = buffer.slice(5, packet_size + 5);

    
    handlePacket(id, packet_size, data);

    let remaining = buffer.slice(packet_size + 5, buffer.length);
    if (remaining.length > 0)
        parsePacket(remaining);
}

function handlePacket(id, size, data) {

    switch (id) {
        case 0x01:
            let success = data.readInt8();
            if (success == 0) {
                setup(3);
            } else {
                screen.width = data.readInt32BE(1);
                screen.height = data.readInt32BE(5);

                screen.sucess = true;

                setup(4);
            }
            break;

        case 0x02:
            remote_server.state = "[closed]";

            console.clear();
            logServerInfo();

            log("\nDisconnected by the server!\n");

            process.exit();
            break;
    }
}


function bitmapToJim(bitmap, width, height) {
    let jimg = new Jimp(width, height);

    for (let x = 0; x < width; x++) {
        for (let y = 0; y < height; y++) { 
            let index = (y * bitmap.byteWidth) + (x * bitmap.bytesPerPixel);
            let r = bitmap.image[index];
            let g = bitmap.image[index+1];
            let b = bitmap.image[index+2];
            let num = (r*256) + (g*256*256) + (b*256*256*256) + 255;
            jimg.setPixelColor(num, x, y);
        }
    }
    return jimg
}

function getHeight(screenWidth, screenHeight, width) {
    return Math.floor(width*(screenHeight/screenWidth));
}


function getNearestColor(r, g, b) {
    //let data = Object.keys(colors).map(key => Math.abs(r - colors[key][0]) + Math.abs(g - colors[key][1]) + Math.abs(b - colors[key][2]));
    let map = [];
    Object.keys(colors).forEach((key, i) => {
        map.push({
                color: key,
                value: Math.abs(r - colors[key][0]) + Math.abs(g - colors[key][1]) + Math.abs(b - colors[key][2])
            });
    });
    //console.log(map.sort((a, b) => a.value > b.value));
    return map.sort((a, b) => a.value - b.value)[0].color;
}

function logServerInfo() {
    log(`--------------------------------------------\n`);
    log(`Host: ${remote_server.host}\n`);
    log(`Port: ${remote_server.port}\n`);
    log(`State: ${remote_server.state}\n`);
    log(`--------------------------------------------\n`);
}

function rgbToHex(r, g, b) {
    return `#${componentToHex(r)}${componentToHex(g)}${componentToHex(b)}`;
}

function componentToHex(component) {
    var hex = component.toString(16);
    return hex.length == 1 ? `0${hex}` : `${hex}`;
}


function log(message) {
    process.stdout.write(message);
}