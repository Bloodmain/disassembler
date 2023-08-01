package riscv;

import elf.ElfStructure;

public interface RiscReadable {
    String toString(RiscVInstruction instruction, ElfStructure.Instruction command);
}
