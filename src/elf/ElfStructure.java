package elf;

public final class ElfStructure {
    private ElfStructure() {}

    public static class ElfHeader {
        int[] magic;
        int bitDepth;
        int endianness;
        int elfVersion;
        int eType;
        int eMachine;
        int eVersion;
        int eEntry;
        int eProgramHeaderTableOffset;
        int eSectionHeaderTableOffset;
        int eFlags;
        int eElfHeaderSize;
        int eProgramHeaderSize;
        int eProgramHeaderNum;
        int eSectionHeaderSize;
        int eSectionHeaderNum;
        int eStringTableIndex;

        public ElfHeader(int[] magic, int bitDepth, int endianness, int elfVersion, int eType, int eMachine,
                         int eVersion, int eEntry, int eProgramHeaderTableOffset, int eSectionHeaderTableOffset,
                         int eFlags, int eElfHeaderSize, int eProgramHeaderSize, int eProgramHeaderNum,
                         int eSectionHeaderSize, int eSectionHeaderNum, int eStringTableIndex) {
            this.magic = magic;
            this.bitDepth = bitDepth;
            this.endianness = endianness;
            this.elfVersion = elfVersion;
            this.eType = eType;
            this.eMachine = eMachine;
            this.eVersion = eVersion;
            this.eEntry = eEntry;
            this.eProgramHeaderTableOffset = eProgramHeaderTableOffset;
            this.eSectionHeaderTableOffset = eSectionHeaderTableOffset;
            this.eFlags = eFlags;
            this.eElfHeaderSize = eElfHeaderSize;
            this.eProgramHeaderSize = eProgramHeaderSize;
            this.eProgramHeaderNum = eProgramHeaderNum;
            this.eSectionHeaderSize = eSectionHeaderSize;
            this.eSectionHeaderNum = eSectionHeaderNum;
            this.eStringTableIndex = eStringTableIndex;
        }
    }

    public static class SectionHeader {
        int sectionHeaderName;
        int sectionHeaderType;
        int sectionHeaderFlags;
        int sectionHeaderAddr;
        int sectionHeaderOffset;
        int sectionHeaderSize;
        int sectionHeaderLink;
        int sectionHeaderInfo;
        int sectionHeaderAddrAlign;
        int sectionHeaderEntrySize;

        public SectionHeader(int sectionHeaderName, int sectionHeaderType, int sectionHeaderFlags,
                             int sectionHeaderAddr, int sectionHeaderOffset, int sectionHeaderSize,
                             int sectionHeaderLink, int sectionHeaderInfo, int sectionHeaderAddrAlign,
                             int sectionHeaderEntrySize) {
            this.sectionHeaderName = sectionHeaderName;
            this.sectionHeaderType = sectionHeaderType;
            this.sectionHeaderFlags = sectionHeaderFlags;
            this.sectionHeaderAddr = sectionHeaderAddr;
            this.sectionHeaderOffset = sectionHeaderOffset;
            this.sectionHeaderSize = sectionHeaderSize;
            this.sectionHeaderLink = sectionHeaderLink;
            this.sectionHeaderInfo = sectionHeaderInfo;
            this.sectionHeaderAddrAlign = sectionHeaderAddrAlign;
            this.sectionHeaderEntrySize = sectionHeaderEntrySize;
        }
    }

    public static class SymTableEntry {
        public String name;
        public int value;
        public int size;
        public int info;
        public int other;
        public int ndx;

        public SymTableEntry(String name, int value, int size, int info, int other, int ndx) {
            this.name = name;
            this.value = value;
            this.size = size;
            this.info = info;
            this.other = other;
            this.ndx = ndx;
        }

        public int getBind() {
            return info >> 4;
        }

        public int getType() {
            return info & 0xf;
        }


    }

    public static class Instruction {
        public int instruction;
        public int address;

        public Instruction(int instruction, int address) {
            this.instruction = instruction;
            this.address = address;
        }
    }

}
