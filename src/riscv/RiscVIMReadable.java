package riscv;

import elf.ElfStructure;

import java.util.HashMap;
import java.util.Map;

public class RiscVIMReadable implements RiscReadable {
    private final String baseFormat;
    private final String marksFormat;
    private final String argsFormat3;
    private final String argsFormat2;
    private final String loadStoreFormat;
    private final Map<Integer, String> marks;
    private final boolean translateOffsetToAddresses;

    public RiscVIMReadable(String baseFormat, String marksFormat, String argsFormat3,
                           String argsFormat2, String loadStoreFormat,
                           Map<Integer, String> marks,
                           boolean translateOffsetToAddresses) {
        this.baseFormat = baseFormat;
        this.marksFormat = marksFormat;
        this.argsFormat3 = argsFormat3;
        this.argsFormat2 = argsFormat2;
        this.loadStoreFormat = loadStoreFormat;
        this.marks = new HashMap<>(marks);
        this.translateOffsetToAddresses = translateOffsetToAddresses;
    }

    private String regToString(int reg) {
        return RiscConstants.REGISTERS_NAMES[reg];
    }

    private String formatJB(int addr, int offset) {
        if (translateOffsetToAddresses) {
            return "0x" + Integer.toHexString(addr + offset) + " <" +
                    marks.getOrDefault(addr + offset, "-") + ">";
        } else {
            return "0x" + Integer.toHexString(offset);
        }
    }

    private String TypeRToString(RiscInstructionsTypes.TypeR instruction) {
        String name = RiscConstants.INSTRUCTIONS_NAMES.get(instruction.opcode + (instruction.funct3 << 7) +
                (instruction.funct7 << 10));
        return String.format(argsFormat3, name, regToString(instruction.rd),
                regToString(instruction.rs1), regToString(instruction.rs2));
    }

    private String TypeIToString(RiscInstructionsTypes.TypeI instruction) {
        int namePart = instruction.opcode + (instruction.funct3 << 7);
        if (instruction.opcode == 0b1110011) {  // Special case (ecall, ebreak)
            namePart += instruction.imm << 10;
            return RiscConstants.INSTRUCTIONS_NAMES.get(namePart);
        }
        String name = RiscConstants.INSTRUCTIONS_NAMES.get(namePart);
        String imm = String.valueOf(instruction.imm);

        if (RiscConstants.LOAD_STORE_INSTRUCTIONS.contains(instruction.opcode)) {
            return String.format(loadStoreFormat, name, regToString(instruction.rd), imm, regToString(instruction.rs1));
        } else {
            return String.format(argsFormat3, name, regToString(instruction.rd), regToString(instruction.rs1), imm);
        }
    }

    private String TypeSToString(RiscInstructionsTypes.TypeS instruction) {
        String name = RiscConstants.INSTRUCTIONS_NAMES.get(instruction.opcode + (instruction.funct3 << 7));
        String imm = String.valueOf(instruction.imm);

        return String.format(loadStoreFormat, name, regToString(instruction.rs2), imm, regToString(instruction.rs1));
    }

    private String TypeBToString(RiscInstructionsTypes.TypeB instruction, int addr) {
        String name = RiscConstants.INSTRUCTIONS_NAMES.get(instruction.opcode + (instruction.funct3 << 7));

        return String.format(argsFormat3, name, regToString(instruction.rs1),
                regToString(instruction.rs2), formatJB(addr, instruction.imm));
    }

    private String TypeUToString(RiscInstructionsTypes.TypeU instruction) {
        String name = RiscConstants.INSTRUCTIONS_NAMES.get(instruction.opcode);
        String imm = String.valueOf(instruction.imm);

        return String.format(argsFormat2, name, regToString(instruction.rd), imm);
    }

    private String TypeJToString(RiscInstructionsTypes.TypeJ instruction, int addr) {
        String name = RiscConstants.INSTRUCTIONS_NAMES.get(instruction.opcode);
        return String.format(argsFormat2, name, regToString(instruction.rd), formatJB(addr, instruction.imm));
    }

    @Override
    public String toString(RiscVInstruction instruction, ElfStructure.Instruction command) {
        String res = "";
        if (marks.containsKey(command.address)) {
            res = String.format(marksFormat, command.address, marks.get(command.address)) + '\n';
        }
        res += String.format(baseFormat, command.address, command.instruction);
        if (instruction instanceof RiscInstructionsTypes.TypeJ) {
            return res + TypeJToString((RiscInstructionsTypes.TypeJ) instruction, command.address);
        } else if (instruction instanceof RiscInstructionsTypes.TypeR) {
            return res + TypeRToString((RiscInstructionsTypes.TypeR) instruction);
        } else if (instruction instanceof RiscInstructionsTypes.TypeI) {
            return res + TypeIToString((RiscInstructionsTypes.TypeI) instruction);
        } else if (instruction instanceof RiscInstructionsTypes.TypeS) {
            return res + TypeSToString((RiscInstructionsTypes.TypeS) instruction);
        } else if (instruction instanceof RiscInstructionsTypes.TypeB) {
            return res + TypeBToString((RiscInstructionsTypes.TypeB) instruction, command.address);
        } else if (instruction instanceof RiscInstructionsTypes.TypeU) {
            return res + TypeUToString((RiscInstructionsTypes.TypeU) instruction);
        } else {
            return res + "unknown_instruction";
        }
    }
}
