package Assembler;

import java.util.HexFormat;
import Assembler.Instruction.RegisterFormat.DestOnly;
import Assembler.Instruction.RegisterFormat.NoR;
import Assembler.Instruction.RegisterFormat.ThreeR;
import Assembler.Instruction.RegisterFormat.TwoR;

public class Instruction {
    public InstructionType type;
    public Operation operation = Operation.NOP;
    public RegisterFormat registers = new RegisterFormat.NoR();
    public int immediate = 0;

    private static String tagDebug(String tag, String value) {
        // return "<" + tag + ":" + value.length() + ">" + value + "<" + tag + ">";
        return value;
    }

    /*
     * * * private constructor so that the only way to create an instruction is via
     * * * buildwithtype
     */ private Instruction() {
    }

    public Instruction immediate(int immediate) {
        this.immediate = immediate;
        return this;
    }

    public static Instruction buildWithType(InstructionType type) {
        var instruction = new Instruction();
        instruction.type = type;
        return instruction;
    }

    public Instruction operation(Operation op) {
        this.operation = op;
        return this;
    }

    public Instruction registers(RegisterFormat registers) {
        this.registers = registers;
        return this;
    }

    public enum Operation {
        AND, OR, XOR, NOT, LEFT_SHIFT, RIGHT_SHIFT, ADD, SUB, MUL, EQ, NE, LT, GE, GT, LE, NOP
    }

    public enum InstructionType {
        MATH, BRANCH, CALL, PUSH, LOAD, STORE, POP,
    }

    private static String intToBytes(int value, int length) {
        var valueString = Integer.toBinaryString(value);
        var msb = value < 0 ? "1" : "0";
        while (valueString.length() < length) {
            valueString = msb + valueString;
        }
        return valueString;
    }

    public sealed interface RegisterFormat
            permits RegisterFormat.DestOnly, RegisterFormat.NoR, RegisterFormat.TwoR, RegisterFormat.ThreeR {
        public record Register(int number) {
            public String toBitPattern() {
                return intToBytes(number, 5);
            }
        }

        public record DestOnly(Register Rd) implements RegisterFormat {
            private static String tagDebug(String tag, String value) {
                // return "<" + tag + ":" + value.length() + ">" + value + "<" + tag + ">";
                return value;
            }

            @Override
            public String toBitPattern(String op) {
                return tagDebug("function", op) + tagDebug("Rd", Rd.toBitPattern());
            }
        }

        public record NoR() implements RegisterFormat {
            @Override
            public String toBitPattern(String op) {
                return "";
            }
        }

        public record TwoR(Register Rs1, Register Rd) implements RegisterFormat {
            private static String tagDebug(String tag, String value) {
                // return "<" + tag + ":" + value.length() + ">" + value + "<" + tag + ">";
                return value;
            }

            @Override
            public String toBitPattern(String op) {
                return tagDebug("Rs1", Rs1.toBitPattern()) + tagDebug("function", op)
                        + tagDebug("Rd", Rd.toBitPattern());
            }
        }

        public record ThreeR(Register Rs1, Register Rs2, Register Rd) implements RegisterFormat {
            private static String tagDebug(String tag, String value) {
                // return "<" + tag + ":" + value.length() + ">" + value + "<" + tag + ">";
                return value;
            }

            @Override
            public String toBitPattern(String op) {
                return tagDebug("Rs1", Rs1.toBitPattern()) + tagDebug("Rs2", Rs2.toBitPattern())
                        + tagDebug("function", op) + tagDebug("Rd", Rd.toBitPattern());
            }
        }

        public String toBitPattern(String op);
    }

    public String toBitPattern() {
        var instructionFormat = switch (registers) {
            case ThreeR r -> "10";
            case TwoR r -> "11";
            case DestOnly r -> "01";
            case NoR r -> "00";
        };
        var immediateLength = switch (registers) {
            case ThreeR r -> 8;
            case TwoR r -> 13;
            case DestOnly r -> 18;
            case NoR r -> 27;
        };
        var instructionType = switch (type) {
            case MATH -> "000";
            case BRANCH -> "001";
            case CALL -> "010";
            case LOAD -> "100";
            case POP -> "110";
            case PUSH -> "011";
            case STORE -> "101";
        };
        var function = switch (operation) {
            case AND -> "1000";
            case OR -> "1001";
            case ADD -> "1110";
            case EQ -> "0000";
            case GE -> "0011";
            case GT -> "0100";
            case LE -> "0101";
            case LEFT_SHIFT -> "1100";
            case LT -> "0010";
            case MUL -> "0111";
            case NE -> "0001";
            case NOP -> "0110";
            case NOT -> "1011";
            case RIGHT_SHIFT -> "1101";
            case SUB -> "1111";
            case XOR -> "1010";
        };
        return tagDebug("imm", intToBytes(immediate, immediateLength)) + registers.toBitPattern(function)
                + tagDebug("type", instructionType) + tagDebug("format", instructionFormat);
    }

    @Override
    public String toString() {
        return "Instruction [type=" + type + ", operation=" + operation + ", registers=" + registers + ", immediate="
                + immediate + "]";
    }
}
