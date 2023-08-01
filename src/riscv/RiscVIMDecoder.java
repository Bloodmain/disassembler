package riscv;

public class RiscVIMDecoder implements RiscVDecoder {
    private int getNBits(int command, int from, int len) {
        return (command >>> from) & ((1 << len) - 1);
    }

    private RiscInstructionsTypes.TypeR parseRType(int command) {
        int opcode = getNBits(command, 0, 7);
        int rd = getNBits(command, 7, 5);
        int funct3 = getNBits(command, 12, 3);
        int rs1 = getNBits(command, 15, 5);
        int rs2 = getNBits(command, 20, 5);
        int funct7 = getNBits(command, 25, 7);
        return new RiscInstructionsTypes.TypeR(funct7, rs2, rs1, funct3, rd, opcode);
    }

    private RiscInstructionsTypes.TypeI parseIType(int command) {
        int opcode = getNBits(command, 0, 7);
        int rd = getNBits(command, 7, 5);
        int funct3 = getNBits(command, 12, 3);
        int rs1 = getNBits(command, 15, 5);
        int imm = getNBits(command, 20, 12) << 20 >> 20;
        if (opcode == 0b0010011 && funct3 == 0b101) {   // special case with shifts
            funct3 += getNBits(command, 25, 7) << 3;
            imm &= 0b000000011111;
        }
        return new RiscInstructionsTypes.TypeI(imm, rs1, funct3, rd, opcode);
    }

    private RiscInstructionsTypes.TypeS parseSType(int command) {
        int opcode = getNBits(command, 0, 7);
        int funct3 = getNBits(command, 12, 3);
        int rs1 = getNBits(command, 15, 5);
        int rs2 = getNBits(command, 20, 5);
        int imm = ((getNBits(command, 25, 7) << 5) + getNBits(command, 7, 5)) << 20 >> 20;
        return new RiscInstructionsTypes.TypeS(imm, rs2, rs1, funct3, opcode);
    }

    private RiscInstructionsTypes.TypeB parseBType(int command) {
        int opcode = getNBits(command, 0, 7);
        int funct3 = getNBits(command, 12, 3);
        int rs1 = getNBits(command, 15, 5);
        int rs2 = getNBits(command, 20, 5);
        int imm = ((getNBits(command, 31, 1) << 12) + (getNBits(command, 7, 1) << 11)
                + (getNBits(command, 25, 6) << 5) + (getNBits(command, 8, 4) << 1)) << 19 >> 19;
        return new RiscInstructionsTypes.TypeB(imm, rs2, rs1, funct3, opcode);
    }

    private RiscInstructionsTypes.TypeU parseUType(int command) {
        int opcode = getNBits(command, 0, 7);
        int rd = getNBits(command, 7, 5);
        int imm = getNBits(command, 12, 20);
        return new RiscInstructionsTypes.TypeU(imm, rd, opcode);
    }

    private RiscInstructionsTypes.TypeJ parseJType(int command) {
        int opcode = getNBits(command, 0, 7);
        int rd = getNBits(command, 7, 5);
        int imm = ((getNBits(command, 31, 1) << 20) + (getNBits(command, 12, 8) << 12) +
                (getNBits(command, 20, 1) << 11) + (getNBits(command, 21, 10) << 1)) << 11 >> 11;
        return new RiscInstructionsTypes.TypeJ(imm, rd, opcode);
    }

    @Override
    public RiscVInstruction decode(int command) {
        int opcode = getNBits(command, 0, 7);

        if (RiscConstants.R_TYPES.contains(opcode)) {
            return parseRType(command);
        } else if (RiscConstants.I_TYPES.contains(opcode)) {
            return parseIType(command);
        } else if (RiscConstants.S_TYPES.contains(opcode)) {
            return parseSType(command);
        } else if (RiscConstants.B_TYPES.contains(opcode)) {
            return parseBType(command);
        } else if (RiscConstants.U_TYPES.contains(opcode)) {
            return parseUType(command);
        } else if (RiscConstants.J_TYPES.contains(opcode)) {
            return parseJType(command);
        }

        return new RiscInstructionsTypes.UnknownInstruction(opcode);
    }
}
