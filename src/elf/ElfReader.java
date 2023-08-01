package elf;

import java.io.IOException;

public interface ElfReader extends AutoCloseable {
    void seek(long offset) throws IOException;
    long getPosition() throws IOException;
    int readElfChar() throws IOException;
    int readElf32Half() throws IOException;
    int readElf32Addr() throws IOException;
    int readElf32Off() throws IOException;
    int readElf32Word() throws IOException;
}
