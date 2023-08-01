package elf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightElfReader implements ElfReader {
    private final RandomAccessFile source;
    private final ByteOrder endianness = ByteOrder.LITTLE_ENDIAN;

    public LightElfReader(String fileName) throws FileNotFoundException {
        this.source = new RandomAccessFile(fileName, "r");
    }

    @Override
    public void seek(long offset) throws IOException {
        this.source.seek(offset);
    }

    @Override
    public long getPosition() throws IOException {
        return source.getFilePointer();
    }

    private byte[] getNBytes(int bytes) throws IOException {
        byte[] res = new byte[bytes];
        source.read(res);
        return res;
    }

    private int readElf(int bytes) throws IOException {
        ByteBuffer wrapper = ByteBuffer.wrap(getNBytes(bytes));
        wrapper.order(endianness);
        if (bytes == 1) {
            return wrapper.get();
        } else if (bytes == 2) {
            return wrapper.getShort();
        } else {
            return wrapper.getInt();
        }
    }

    @Override
    public int readElfChar() throws IOException {
        return readElf(1);
    }

    @Override
    public int readElf32Half() throws IOException {
        return readElf(2);
    }

    @Override
    public int readElf32Addr() throws IOException {
        return readElf(4);
    }

    @Override
    public int readElf32Off() throws IOException {
        return readElf(4);
    }

    @Override
    public int readElf32Word() throws IOException {
        return readElf(4);
    }

    @Override
    public void close() throws IOException {
        this.source.close();
    }
}
