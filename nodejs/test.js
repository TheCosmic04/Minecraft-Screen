var net = require("net");
var sleep = require("system-sleep");

var host = "localhost";
var port = 1234;
var packet_util = require("./packets-util.js");

var socket = net.createConnection({host, port});

var fps = 20;
var width;
var height;

var position = {x: 0, y: 0};

socket.once("ready", () => {});

socket.on("data", parsePacket);

socket.write(packet_util.packet(0x01, Buffer.from(process.argv[process.argv.length-1])));

function start() {
    
    let interval = setInterval(() => {update()}, 1000/fps);
    /*
    while(true) {
        console.log("update...");
        //update();

        //console.log(1000/fps)
        sleep(1000);
    }
    */
}

function update() {
    position.x++;

    if (position.x % width == 0) {
        position.x = 0;
        position.y++;
    }

    position.y %= height;


    let pixels = packet_util.genArray2d(width, height, 0);

    
    for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
            pixels[y][x] = (x == position.x && y == position.y) ? 133 : 163;
        }
    }


    socket.write(packet_util.packet(0x02, packet_util.UInt8Array2d(pixels)));
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
    console.log({id, size, data});

    switch (id) {
        case 0x01:
            let success = data.readInt8();
            if (success == 0) {
                console.log(`Invalid token!`);
                process.exit();
                break;
            } else {
                width = data.readInt32BE(1);
                height = data.readInt32BE(5);

                start();
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