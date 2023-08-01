package elf;

import java.io.IOException;

public interface ElfParser extends AutoCloseable {
    ElfStructure.SymTableEntry[] getSymTable();
    ElfStructure.Instruction[] parseTextSection(int instructionSize) throws IOException;
    int getTextSectionIndex();
}
