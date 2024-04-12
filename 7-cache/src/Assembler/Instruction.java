package Assembler;

import Assembler.Instruction.RegisterFormat.DestOnly;
import Assembler.Instruction.RegisterFormat.NoR;
import Assembler.Instruction.RegisterFormat.ThreeR;
import Assembler.Instruction.RegisterFormat.TwoR;

public class Instruction {
    public InstructionType type;
    // by default an instruction has no operation (function) has NoR format
    // and has 0 as its immediate value
    public Operation operation = Operation.NOP;
    public RegisterFormat registers = new RegisterFormat.NoR();
    public int immediate = 0;

    // used for debugging output of assembler to show length in bits of certain
    // parts of instruction
    private static String tagDebug(String tag, String value) {
        // return "<" + tag + ":" + value.length() + ">" + value + "<" + tag + ">";
        return value;
    }

    /*
     * private constructor so that the only way to create an instruction is via
     * buildwithtype
     */
    private Instruction() {
    }

    // set the immediate value and return the `new` instruction
    public Instruction immediate(int immediate) {
        this.immediate = immediate;
        return this;
    }

    // create a new instruction with a given type (math, call, ...)
    public static Instruction buildWithType(InstructionType type) {
        var instruction = new Instruction();
        instruction.type = type;
        return instruction;
    }

    // set the operation of this instrcuction and return the `new` instruction
    public Instruction operation(Operation op) {
        this.operation = op;
        return this;
    }

    // set the registers of this instrcuction and return the `new` instruction
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

    // turn a integer to a string of bytes of a certain length
    private static String intToBytes(int value, int length) {
        // we first obtain an intial binary string using a java builtin
        // unfortantly its not exactly what we need as for postive numbers it only goes
        // up the minum length needed to represent the number in biary
        // but for negative number since it account for 2's compliment it does use like
        // 32 bits
        var valueString = Integer.toBinaryString(value);
        var actualStart = valueString.length() - length;
        // if the java generated binary string is to big truncate it
        if (actualStart > 0) {
            valueString = valueString.substring(actualStart, valueString.length());
        } else if (actualStart < 0) {
            // otherwise sign extend it
            var msb = value < 0 ? "1" : "0";
            valueString = msb.repeat(-1 * actualStart) + valueString;
        }
        return valueString;
    }

    // how store registers
    // uses records + sealed to obtain sum type like syntax like seen in ml based
    // languages
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

        // mandate that each variant have a way to get it as bits
        public String toBitPattern(String op);
    }

    public String toBitPattern() {
        // obtain the bit patterns for different parts of the instruction
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
        // then join and return the bits
        return tagDebug("imm", intToBytes(immediate, immediateLength)) + registers.toBitPattern(function)
                + tagDebug("type", instructionType) + tagDebug("format", instructionFormat);
    }

    @Override
    public String toString() {
        return "Instruction [type=" + type + ", operation=" + operation + ", registers=" + registers + ", immediate="
                + immediate + "]";
    }
}
