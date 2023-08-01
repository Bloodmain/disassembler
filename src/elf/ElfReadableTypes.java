package elf;

public final class ElfReadableTypes {
    private ElfReadableTypes() {}

    public final static class SymTableTypes {
        private SymTableTypes() {}

        public static String bindToString(int bind) {
            switch (bind) {
                case 0 -> {
                    return "LOCAL";
                }
                case 1 -> {
                    return "GLOBAL";
                }
                case 2 -> {
                    return "WEAK";
                }
                case 13, 14, 15 -> {
                    return "PROCRESERVED";
                }
                default -> {
                    return "UNKNOWN";
                }
            }
        }

        public static String typeToString(int type) {
            switch (type) {
                case 0 -> {
                    return "NOTYPE";
                }
                case 1 -> {
                    return "OBJECT";
                }
                case 2 -> {
                    return "FUNC";
                }
                case 3 -> {
                    return "SECTION";
                }
                case 4 -> {
                    return "FILE";
                }
                case 13, 14, 15 -> {
                    return "PROCRESERVED";
                }
                default -> {
                    return "UNKNOWN";
                }
            }
        }

        public static String otherToString(int other) {
            switch (other) {
                case 0 -> {
                    return "DEFAULT";
                }
                case 1 -> {
                    return "INTERNAL";
                }
                case 2 -> {
                    return "HIDDEN";
                }
                case 3 -> {
                    return "PROTECTED";
                }
                case 4 -> {
                    return "EXPORTED";
                }
                case 5 -> {
                    return "SINGLETON";
                }
                case 6 -> {
                    return "ELIMINATE";
                }
                default -> {
                    return "UNKNOWN";
                }
            }
        }

        public static String ndxToString(int ndx) {
            ndx &= 0x0000ffff;
            if (ndx == 0) {
                return "UNDEF";
            } else if (0xff00 <= ndx) {
                if (ndx == 0xfff1) {
                    return "ABS";
                } else if (ndx == 0xfff2) {
                    return "COMMON";
                } else if (ndx <= 0xff1f) {
                    return "PROCRESERVED";
                } else {
                    return "RESERVED";
                }
            } else {
                return String.valueOf(ndx);
            }
        }
    }
}
