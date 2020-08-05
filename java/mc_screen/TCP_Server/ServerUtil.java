package mc_screen.TCP_Server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerUtil {

    public static byte[] sliceByteArray(byte[] arr, int start, int end) {
        byte[] sliced = new byte[end - start];

        System.arraycopy(arr, start, sliced, 0, sliced.length);
        return sliced;
    }

    public static byte[] sliceByteArray(byte[] arr, int start) {
        byte[] sliced = new byte[arr.length - start];

        System.arraycopy(arr, start, sliced, 0, sliced.length);
        return sliced;
    }

    public static int[] sliceIntArray(int[] arr, int start, int end) {
        int[] sliced = new int[end - start];

        System.arraycopy(arr, start, sliced, 0, sliced.length);
        return sliced;
    }

    public static int[] sliceIntArray(int[] arr, int start) {
        int[] sliced = new int[arr.length - start];

        System.arraycopy(arr, start, sliced, 0, sliced.length);
        return sliced;
    }

    public static short[] sliceShortArray(short[] arr, int start, int end) {
        short[] sliced = new short[end - start];

        System.arraycopy(arr, start, sliced, 0, sliced.length);
        return sliced;
    }

    public static short[] sliceShortArray(short[] arr, int start) {
        short[] sliced = new short[arr.length - start];

        System.arraycopy(arr, start, sliced, 0, sliced.length);
        return sliced;
    }

    public static byte[] joinByteArrays(byte[] ...arrays) {
        if (arrays.length == 1)
            return arrays[0];
        int length = 0;

        for (byte[] array : arrays) {
            length += array.length;
        }

        byte[] result = new byte[length];
        int offset = 0;

        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public static int[] decodeInt32Array(byte[] data) {
        if (data.length % 4 != 0)
            return new int[1];
        else {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int[] array = new int[data.length/4];
            for (int i = 0; i < data.length/4; i++) {
                array[i] = buffer.getInt(i*4);
            }
            return array;
        }
    }

    public static short[] decodeInt16Array(byte[] data) {
        if (data.length % 2 != 0)
            return new short[1];
        else {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            short[] array = new short[data.length/2];
            for (int i = 0; i < data.length/2; i++) {
                array[i] = buffer.getShort(i*2);
            }
            return array;
        }
    }

    public static int[][] decodeInt32Array2d(byte[] data) {
        if (data.length % 4 != 0)
            return new int[0][0];

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int width = buffer.getInt();
        int height = buffer.getInt(4);

        int[] array = new int[width*height];

        for (int i = 0; i < (data.length-8)/4; i++) {
            array[i] = buffer.getInt(i*4 + 8);
        }

        int[][] array_2d = new int[height][width];
        for (int i = 0; i < height; i++) {
            array_2d[i] = sliceIntArray(array, width*i, width*(i+1));
        }

        return array_2d;
    }

    public static short[][] decodeInt16Array2d(byte[] data) {
        if (data.length % 2 != 0)
            return new short[0][0];

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int width = buffer.getInt();
        int height = buffer.getInt(4);

        short[] array = new short[width*height];

        for (int i = 0; i < (data.length-8)/2; i++) {
            array[i] = buffer.getShort(i*2 + 8);
        }

        short[][] array_2d = new short[height][width];
        for (int i = 0; i < height; i++) {
            array_2d[i] = sliceShortArray(array, width*i, width*(i+1));
        }

        return array_2d;
    }

    public static byte[][] decodeInt8Array2d(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int width = buffer.getInt();
        int height = buffer.getInt(4);

        byte[] array = sliceByteArray(data, 8);

        byte[][] array_2d = new byte[height][width];
        for (int i = 0; i < height; i++) {
            array_2d[i] = sliceByteArray(array, width*i, width*(i+1));
        }

        return array_2d;
    }


    public static short[][] decodeUInt8Array2d(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int width = buffer.getInt();
        int height = buffer.getInt(4);

        byte[] array = sliceByteArray(data, 8);

        byte[][] array_2d_byte = new byte[height][width];
        for (int i = 0; i < height; i++) {
            array_2d_byte[i] = sliceByteArray(array, width*i, width*(i+1));
        }

        short[][] array_2d = new short[height][width];
        for (int y = 0; y < array_2d_byte.length; y++) {
            for (int x = 0; x < array_2d_byte[0].length; x++) {
                array_2d[y][x] = toUnsignedByte(array_2d_byte[y][x]);
            }
        }

        return array_2d;
    }


    public static short toUnsignedByte(byte value) {
        return (short)((short)value + (short)Math.pow(2, 7) - 1);
    }

    public static byte[] packet(int id, byte[] data) {
        ByteBuffer header = ByteBuffer.allocate(5);
        header.put((byte) id);
        header.putInt(data.length);

        return joinByteArrays(header.array(), data);
    }

}

