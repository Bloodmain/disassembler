package elf;

import exceptions.ElfException;

import java.io.IOException;
import java.util.Arrays;

public class LightElfParser implements ElfParser {
    private final ElfReader reader;
    private ElfStructure.ElfHeader header;
    private ElfStructure.SectionHeader[] sectionHeaders;
    private ElfStructure.SymTableEntry[] symTable;
    private int textSectionIndex = -1;

    public LightElfParser(String fileName) throws ElfException, IOException {
        this.reader = new LightElfReader(fileName);
        parse();
    }

    public void parse() throws ElfException, IOException {
        parseElfHeader();
        parseSectionHeaderTable();
        parseSymTable();
    }

    private void parseElfHeader() throws ElfException, IOException {
        reader.seek(0);
        int[] magic = new int[]{reader.readElfChar(), reader.readElfChar(), reader.readElfChar(), reader.readElfChar()};
        if (!Arrays.equals(magic, new int[]{0x7f, 0x45, 0x4c, 0x46})) {
            throw new ElfException("The file doesn't match elf format.");
        }

        int bitDepth = reader.readElfChar();
        if (bitDepth != 1) {
            throw new ElfException("Unsupported bit depth.");
        }

        int endianness = reader.readElfChar();
        if (endianness != 1) {
            throw new ElfException("Unsupported endianness.");
        }

        int elfVersion = reader.readElfChar();
        if (elfVersion != 1) {
            throw new ElfException("Unsupported elf version.");
        }
        reader.seek(16);

        int eType = reader.readElf32Half();
        int eMachine = reader.readElf32Half();
        if (eMachine != 243) {
            throw new ElfException("Unsupported machine.");
        }
        int eVersion = reader.readElf32Word();
        if (eVersion == 0) {
            throw new ElfException("Invalid machine version.");
        }
        int eEntry = reader.readElf32Addr();
        int eProgramHeaderTableOffset = reader.readElf32Off();
        int eSectionHeaderTableOffset = reader.readElf32Off();
        int eFlags = reader.readElf32Word();
        int eElfHeaderSize = reader.readElf32Half();
        int eProgramHeaderSize = reader.readElf32Half();
        int eProgramHeaderNum = reader.readElf32Half();
        int eSectionHeaderSize = reader.readElf32Half();
        int eSectionHeaderNum = reader.readElf32Half();
        int eStringTableIndex = reader.readElf32Half();

        this.header = new ElfStructure.ElfHeader(magic, bitDepth, endianness, elfVersion, eType, eMachine, eVersion, eEntry,
                eProgramHeaderTableOffset, eSectionHeaderTableOffset, eFlags, eElfHeaderSize, eProgramHeaderSize,
                eProgramHeaderNum, eSectionHeaderSize, eSectionHeaderNum, eStringTableIndex);
    }

    private void parseSectionHeaderTable() throws IOException {
        if (header.eSectionHeaderTableOffset == 0) {
            return;
        }

        sectionHeaders = new ElfStructure.SectionHeader[header.eSectionHeaderNum];
        reader.seek(header.eSectionHeaderTableOffset);
        for (int headerNum = 0; headerNum < header.eSectionHeaderNum; ++headerNum) {
            int sectionHeaderName = reader.readElf32Word();
            int sectionHeaderType = reader.readElf32Word();
            int sectionHeaderFlags = reader.readElf32Word();
            int sectionHeaderAddr = reader.readElf32Addr();
            int sectionHeaderOffset = reader.readElf32Off();
            int sectionHeaderSize = reader.readElf32Word();
            int sectionHeaderLink = reader.readElf32Word();
            int sectionHeaderInfo = reader.readElf32Word();
            int sectionHeaderAddrAlign = reader.readElf32Word();
            int sectionHeaderEntrySize = reader.readElf32Word();
            sectionHeaders[headerNum] = new ElfStructure.SectionHeader(sectionHeaderName, sectionHeaderType,
                    sectionHeaderFlags, sectionHeaderAddr, sectionHeaderOffset, sectionHeaderSize, sectionHeaderLink,
                    sectionHeaderInfo, sectionHeaderAddrAlign, sectionHeaderEntrySize);
        }
    }

    private String parseName(int offset) throws IOException {
        for (int i = 0; i < sectionHeaders.length; ++i) {
            if (sectionHeaders[i].sectionHeaderType == 3) { // 3 stands for String table
                if (i != header.eStringTableIndex) {
                    // check whether we should use shstrtab or strtab
                    StringBuilder sb = new StringBuilder();
                    long oldPos = reader.getPosition(); // we should save old reader position to return to it later
                    reader.seek(sectionHeaders[i].sectionHeaderOffset + offset);
                    char ch = (char) reader.readElfChar();
                    while (ch != '\0') {
                        sb.append(ch);
                        ch = (char) reader.readElfChar();
                    }
                    reader.seek(oldPos);    // Not forgetting to return to old position.
                    return sb.toString();
                }
            }
        }

        return "\0";
    }

    private void parseSymTable() throws IOException {
        for (ElfStructure.SectionHeader sectionHeader : sectionHeaders) {
            if (sectionHeader.sectionHeaderType == 2) { // 2 stands for Symbol table
                reader.seek(sectionHeader.sectionHeaderOffset);

                int entriesNum = sectionHeader.sectionHeaderSize / sectionHeader.sectionHeaderEntrySize;
                symTable = new ElfStructure.SymTableEntry[entriesNum];

                for (int entry = 0; entry < entriesNum; ++entry) {
                    String name = parseName(reader.readElf32Word());
                    int value = reader.readElf32Addr();
                    int size = reader.readElf32Word();
                    int info = reader.readElfChar();
                    int other = reader.readElfChar();
                    int ndx = reader.readElf32Half();
                    symTable[entry] = new ElfStructure.SymTableEntry(name, value, size, info, other, ndx);
                }
                return;
            }
        }
    }

    @Override
    public ElfStructure.Instruction[] parseTextSection(final int instructionSize) throws IOException {
        for (int i = 0; i < sectionHeaders.length; ++i) {
            ElfStructure.SectionHeader sectionHeader = sectionHeaders[i];
            if (sectionHeader.sectionHeaderType == 1 && ((sectionHeader.sectionHeaderFlags & 0x4) != 0)) {
                textSectionIndex = i;
                reader.seek(sectionHeader.sectionHeaderOffset);
                int instructionsNum = sectionHeader.sectionHeaderSize / instructionSize;
                ElfStructure.Instruction[] res = new ElfStructure.Instruction[instructionsNum];
                int nowAddress = sectionHeader.sectionHeaderAddr;
                for (int j = 0; j < instructionsNum; ++j) {
                    res[j] = new ElfStructure.Instruction(reader.readElf32Word(), nowAddress);
                    nowAddress += instructionSize;
                }
                return res;
            }
        }
        return new ElfStructure.Instruction[0];
    }

    @Override
    public ElfStructure.SymTableEntry[] getSymTable() {
        return symTable;
    }

    @Override
    public int getTextSectionIndex() {
        return textSectionIndex;
    }

    @Override
    public void close() throws Exception {
        this.reader.close();
    }
}
