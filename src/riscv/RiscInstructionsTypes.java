package riscv;

public final class RiscInstructionsTypes {
    private RiscInstructionsTypes() {}

    public static class TypeR implements RiscVInstruction {
        int funct7;
        int rs2;
        int rs1;
        int funct3;
        int rd;
        int opcode;

        public TypeR(int funct7, int rs2, int rs1, int funct3, int rd, int opcode) {
            this.funct7 = funct7;
            this.rs2 = rs2;
            this.rs1 = rs1;
            this.funct3 = funct3;
            this.rd = rd;
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return null;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }

    public static class TypeI implements RiscVInstruction {
        int imm;
        int rs1;
        int funct3;
        int rd;
        int opcode;

        public TypeI(int imm, int rs1, int funct3, int rd, int opcode) {
            this.imm = imm;
            this.rs1 = rs1;
            this.funct3 = funct3;
            this.rd = rd;
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return imm;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }

    public static class TypeS implements RiscVInstruction {
        int imm;
        int rs2;
        int rs1;
        int funct3;
        int opcode;

        public TypeS(int imm, int rs2, int rs1, int funct3, int opcode) {
            this.imm = imm;
            this.rs2 = rs2;
            this.rs1 = rs1;
            this.funct3 = funct3;
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return imm;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }

    public static class TypeB implements RiscVInstruction {
        int imm;
        int rs2;
        int rs1;
        int funct3;
        int opcode;

        public TypeB(int imm, int rs2, int rs1, int funct3, int opcode) {
            this.imm = imm;
            this.rs2 = rs2;
            this.rs1 = rs1;
            this.funct3 = funct3;
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return imm;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }

    public static class TypeU implements RiscVInstruction {
        int imm;
        int rd;
        int opcode;

        public TypeU(int imm, int rd, int opcode) {
            this.imm = imm;
            this.rd = rd;
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return imm;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }

    public static class TypeJ implements RiscVInstruction {
        int imm;
        int rd;
        int opcode;

        public TypeJ(int imm, int rd, int opcode) {
            this.imm = imm;
            this.rd = rd;
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return imm;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }

    public static class UnknownInstruction implements RiscVInstruction {
        int opcode;

        public UnknownInstruction(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public Integer getImm() {
            return null;
        }

        @Override
        public int getOpcode() {
            return opcode;
        }
    }
}
