package riscv;

public interface RiscVDecoder {
    RiscVInstruction decode(int command);
}
