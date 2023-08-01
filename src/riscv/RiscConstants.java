package riscv;

import java.util.Map;
import java.util.Set;

public final class RiscConstants {
    private RiscConstants() {}

    public static final Map<Integer, String> INSTRUCTIONS_NAMES = Map.ofEntries(
            Map.entry(0b0110111, "lui"),
            Map.entry(0b0010111, "auipc"),
            Map.entry(0b1101111, "jal"),
            Map.entry(0b0001100111, "jalr"),
            Map.entry(0b0001100011, "beq"),
            Map.entry(0b0011100011, "bne"),
            Map.entry(0b1001100011, "blt"),
            Map.entry(0b1011100011, "bge"),
            Map.entry(0b1101100011, "bltu"),
            Map.entry(0b1111100011, "bgeu"),
            Map.entry(0b0000000011, "lb"),
            Map.entry(0b0010000011, "lh"),
            Map.entry(0b0100000011, "lw"),
            Map.entry(0b1000000011, "lbu"),
            Map.entry(0b1010000011, "lhu"),
            Map.entry(0b0000100011, "sb"),
            Map.entry(0b0010100011, "sh"),
            Map.entry(0b0100100011, "sw"),
            Map.entry(0b0000010011, "addi"),
            Map.entry(0b0100010011, "slti"),
            Map.entry(0b0110010011, "sltiu"),
            Map.entry(0b1000010011, "xori"),
            Map.entry(0b1100010011, "ori"),
            Map.entry(0b1110010011, "andi"),
            Map.entry(0b00000000010010011, "slli"),
            Map.entry(0b00000001010010011, "srli"),
            Map.entry(0b01000001010010011, "srai"),
            Map.entry(0b00000000000110011, "add"),
            Map.entry(0b01000000000110011, "sub"),
            Map.entry(0b00000000010110011, "sll"),
            Map.entry(0b00000000100110011, "slt"),
            Map.entry(0b00000000110110011, "sltu"),
            Map.entry(0b00000001000110011, "xor"),
            Map.entry(0b00000001010110011, "srl"),
            Map.entry(0b01000001010110011, "sra"),
            Map.entry(0b00000001100110011, "or"),
            Map.entry(0b00000001110110011, "and"),
            Map.entry(0b0001110011, "ecall"),
            Map.entry(0b10001110011, "ebreak"),
            Map.entry(0b00000010000110011, "mul"),
            Map.entry(0b00000010010110011, "mulh"),
            Map.entry(0b00000010100110011, "mulhsu"),
            Map.entry(0b00000010110110011, "mulhu"),
            Map.entry(0b00000011000110011, "div"),
            Map.entry(0b00000011010110011, "divu"),
            Map.entry(0b00000011100110011, "rem"),
            Map.entry(0b00000011110110011, "remu")
    );

    public static final Set<Integer> R_TYPES = Set.of(
            0b0110011
    );

    public static final Set<Integer> I_TYPES = Set.of(
            0b1100111,
            0b0000011,
            0b1110011,
            0b0010011
    );

    public static final Set<Integer> S_TYPES = Set.of(
            0b0100011
    );

    public static final Set<Integer> B_TYPES = Set.of(
            0b1100011
    );

    public static final Set<Integer> U_TYPES = Set.of(
            0b0110111,
            0b0010111
    );

    public static final Set<Integer> J_TYPES = Set.of(
            0b1101111
    );

    public static final String[] REGISTERS_NAMES = new String[]{
            "zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2", "s0", "s1", "a0", "a1",
            "a2", "a3", "a4", "a5", "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7", "s8",
            "s9", "s10", "s11", "t3", "t4", "t5", "t6"
    };

    public static final Set<Integer> LOAD_STORE_INSTRUCTIONS = Set.of(
            0b0000011,
            0b0100011,
            0b1100111
    );
}
