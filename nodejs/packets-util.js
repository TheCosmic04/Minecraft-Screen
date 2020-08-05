function packet(id, buffer) {
    if (!Buffer.isBuffer(buffer) || id < -0xff || id > 0xff)
        return Buffer.from([0x00]);

    let packet_id = Buffer.alloc(5);
    packet_id.writeInt8(id);
    packet_id.writeInt32BE(buffer.length, 1);

    return concatBuffers(packet_id, buffer);

}

function concatBuffers(...buff_arr) {
    buff_arr = buff_arr.filter(buff => Buffer.isBuffer(buff));
    return Buffer.concat(buff_arr, buff_arr.map(buf => buf.length).reduce((acc, curr) => acc + curr));
}

function decodeInt32Array(buffer) {
    if (buffer.length % 4 != 0)
        return [];
    
    let arr = [];
    for (let i = 0; i < buffer.length / 4; i++) {
        let int = buffer.readInt32BE(i*4);
        // console.log(int);
        arr.push(int);
    }
    return arr;
}

function decodeInt16Array(buffer) {
    if (buffer.length % 2 != 0)
        return [];
    
    let arr = [];
    for (let i = 0; i < buffer.length / 2; i++) {
        let short = buffer.readInt16BE(i*2);
        // console.log(int);
        arr.push(short);
    }
    return arr;
}

function decodeInt8Array(buffer) {
    let arr = [];
    for (let i = 0; i < buffer.length; i++) {
        let byte = buffer.readInt8(i);
        // console.log(int);
        arr.push(byte);
    }
    return arr;
}

function int32Array(array) {
    if (!Array.isArray(array))
        return [];
    
    array = array.filter(num => !isNaN(num) || (num > (2**31 - 1) && num < (2**31 * -1)) );

      
    let arr_buf = Buffer.alloc(array.length * 4);
    array.forEach((num, i) => arr_buf.writeInt32BE(num, i*4));

    //0x01 -> Int8 | 0x02 -> Int16 | 0x03 -> Int32 | 0x04 -> 2dInt8 | 0x05 -> 2dInt16 | 0x06 -> 2dInt32   
    let arr_type = Buffer.from([0x03]);

    return concatBuffers(arr_type, arr_buf);
}

function int16Array(array) {
    if (!Array.isArray(array))
        return [];
    
    array = array.filter(num => !isNaN(num) || (num > (2**15 - 1) && num < (2**15 * -1)) );

      
    let arr_buf = Buffer.alloc(array.length * 2);
    array.forEach((num, i) => arr_buf.writeInt16BE(num, i*2));

    //0x01 -> Int8 | 0x02 -> Int16 | 0x03 -> Int32 | 0x04 -> 2dInt8 | 0x05 -> 2dInt16 | 0x06 -> 2dInt32   
    let arr_type = Buffer.from([0x02]);

    return concatBuffers(arr_type, arr_buf);
}

function int8Array(array) {
    if (!Array.isArray(array))
        return [];
    
    array = array.filter(num => !isNaN(num) || (num > (2**7 - 1) && num < (2**7 * -1)) );

      
    let arr_buf = Buffer.from(array);

    //0x01 -> Int8 | 0x02 -> Int16 | 0x03 -> Int32 | 0x04 -> 2dInt8 | 0x05 -> 2dInt16 | 0x06 -> 2dInt32   
    let arr_type = Buffer.from([0x01]);

    return concatBuffers(arr_type, arr_buf);
}

function genArray2d(width, height, fill_value) {
    let array = new Array(height);

    for (let y = 0; y < height; y++) {
        array[y] = new Array(width).fill(fill_value);
    }
    return array;
}

function array2dToArrray(array_2d) {
    let width = array_2d[0].length;
    let height = array_2d.length;

    let array = new Array(width * height);

    array_2d.forEach((colon, y) => {
        colon.forEach((row, x) => {
            array[y * width + x] = array_2d[y][x];
        });
    });

    return {width, height, array};
}

function Int32Array2d(array_2d) {
    let arr_obj = array2dToArrray(array_2d);

    let buffer = Buffer.alloc(arr_obj.array.length*4 + 8);

    buffer.writeInt32BE(arr_obj.width);
    buffer.writeInt32BE(arr_obj.height, 4);

    for (let i = 0; i < arr_obj.array.length; i++) {
        buffer.writeInt32BE(arr_obj.array[i], i*4 + 8);
    }

    let arr_type = Buffer.from([0x06]);

    return concatBuffers(arr_type, buffer);
}

function Int16Array2d(array_2d) {
    let arr_obj = array2dToArrray(array_2d);

    let buffer = Buffer.alloc(arr_obj.array.length*2 + 8);

    buffer.writeInt32BE(arr_obj.width);
    buffer.writeInt32BE(arr_obj.height, 4);

    for (let i = 0; i < arr_obj.array.length; i++) {
        buffer.writeInt16BE(arr_obj.array[i], i*2 + 8);
    }

    let arr_type = Buffer.from([0x07]);

    return concatBuffers(arr_type, buffer);
}

function Int8Array2d(array_2d) {
    let arr_obj = array2dToArrray(array_2d);

    let buffer = Buffer.alloc(arr_obj.array.length + 8);

    buffer.writeInt32BE(arr_obj.width);
    buffer.writeInt32BE(arr_obj.height, 4);

    for (let i = 0; i < arr_obj.array.length; i++) {
        buffer.writeInt8(arr_obj.array[i], i + 8);
    }

    let arr_type = Buffer.from([0x04]);

    return concatBuffers(arr_type, buffer);
}

function UInt8Array2d(array_2d) {
    let arr_obj = array2dToArrray(array_2d);

    let buffer = Buffer.alloc(arr_obj.array.length + 8);

    buffer.writeInt32BE(arr_obj.width);
    buffer.writeInt32BE(arr_obj.height, 4);

    for (let i = 0; i < arr_obj.array.length; i++) {
        buffer.writeInt8(unsignedByte(arr_obj.array[i]), i + 8);
    }

    let arr_type = Buffer.from([0x07]);

    return concatBuffers(arr_type, buffer);
}


function unsignedByte(byte) {
    if (byte < 0 || byte > 2**8-1)
        return 0;
    return byte - 2**7 + 1;
}

function decodeInt32Array2d(buffer) {
    let width = buffer.readInt32BE();
    let height = buffer.readInt32BE(4);

    let array = [];
    for (let i = 0; i < (buffer.length-8)/4; i++) {
        array[i] = buffer.readInt32BE(i*4 + 8);
    }

    let array_2d = new Array(height);
    for (let i = 0; i < height; i++) {
        array_2d[i] = array.slice(width*i, width*(i+1));
    }

    return array_2d;
}

module.exports = {
    packet,
    concatBuffers,
    decodeInt32Array,
    decodeInt16Array,
    decodeInt8Array,
    int32Array,
    int16Array,
    int8Array,
    genArray2d,
    array2dToArrray,
    Int32Array2d,
    Int16Array2d,
    Int8Array2d,
    unsignedByte,
    decodeInt32Array2d,
    UInt8Array2d
}