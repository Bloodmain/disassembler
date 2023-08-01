import elf.ElfParser;
import elf.ElfReadableTypes;
import elf.ElfStructure;
import elf.LightElfParser;
import exceptions.ElfException;
import riscv.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static void error(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            error("Filenames are not provided.");
        } else if (args.length == 1) {
            error("Output filename is not provided.");
        }

        ElfStructure.SymTableEntry[] symTable = null;
        ElfStructure.Instruction[] commands = null;
        int textSectionIndex = -1;

        // parsing data
        try (ElfParser parser = new LightElfParser(args[0])) {
            symTable = parser.getSymTable();
            commands = parser.parseTextSection(4);
            textSectionIndex = parser.getTextSectionIndex();
        } catch (FileNotFoundException e) {
            error("Input file is not found: " + e.getMessage());
        } catch (IOException e) {
            error("Error while reading data: " + e.getMessage());
        } catch (ElfException e) {
            error("Error while parsing elf file: " + e.getMessage());
        } catch (Exception e) {
            error("Probably, you have incorrect elf file: " + e.getMessage());
        }

        if (symTable == null || commands == null || textSectionIndex == -1) {
            error("Probably, you have incorrect elf file.");
        }

        RiscVDecoder decoder = new RiscVIMDecoder();
        RiscVInstruction[] instructions = new RiscVInstruction[commands.length];
        Map<Integer, String> marks = new HashMap<>();
        int unknownMarkNum = 0;

        // find out what address should be marked and search for mark names in symtab
        for (int i = 0; i < commands.length; ++i) {
            RiscVInstruction instruction = decoder.decode(commands[i].instruction);
            instructions[i] = instruction;
            if (RiscConstants.B_TYPES.contains(instruction.getOpcode()) ||
                    RiscConstants.J_TYPES.contains(instruction.getOpcode())) {
                int jumpTo = commands[i].address + instruction.getImm();
                if (!marks.containsKey(jumpTo)) {
                    boolean foundName = false;
                    for (ElfStructure.SymTableEntry entry : symTable) {
                        if (entry.ndx == textSectionIndex) {
                            if (entry.value == jumpTo && entry.size != 0) {
                                marks.put(jumpTo, entry.name);
                                foundName = true;
                                break;
                            }
                        }
                    }
                    if (!foundName) {
                        marks.put(jumpTo, "L" + unknownMarkNum++);
                    }
                }
            }
        }

        RiscReadable toReadable = new RiscVIMReadable(
                "   %05x:\t%08x\t",
                "%08x   <%s>:",
                "%-7s\t%s, %s, %s",
                "%-7s\t%s, %s",
                "%-7s\t%s, %s(%s)",
                marks,
                true
        );

        //outputting data
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(args[1]),
                StandardCharsets.UTF_8
        ))) {
            writer.write(".text\n");
            for (int i = 0; i < commands.length; ++i) {
                writer.write(toReadable.toString(instructions[i], commands[i]) + '\n');
            }
            writer.write("\n");

            writer.write(".symtab\n");
            writer.write("Symbol Value              Size Type     Bind     Vis       Index Name\n");
            for (int i = 0; i < symTable.length; ++i) {
                ElfStructure.SymTableEntry entry = symTable[i];
                writer.write(String.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s\n",
                        i, entry.value, entry.size,
                        ElfReadableTypes.SymTableTypes.typeToString(entry.getType()),
                        ElfReadableTypes.SymTableTypes.bindToString(entry.getBind()),
                        ElfReadableTypes.SymTableTypes.otherToString(entry.other),
                        ElfReadableTypes.SymTableTypes.ndxToString(entry.ndx),
                        entry.name));
            }
        } catch (IOException e) {
            error("Error while writing data: " + e.getMessage());
        } catch (Exception e) {
            error(e.getMessage());
        }
    }
}
