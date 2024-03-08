package Assembler;

public class Instruction {
    // TODO: method to convert ot bit string
    public InstructionType type;
    public Operation operation;
    public RegisterFormat registers;
    public int immediate = 0;

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

    public sealed interface RegisterFormat
            permits RegisterFormat.DestOnly, RegisterFormat.NoR, RegisterFormat.TwoR, RegisterFormat.ThreeR {

        public record Register(int number) {
        }

        public record DestOnly(Register Rd) implements RegisterFormat {
        }

        public record NoR() implements RegisterFormat {
        }

        public record TwoR(Register Rs1, Register Rd)
                implements RegisterFormat {
        }

        public record ThreeR(Register Rs1, Register Rs2, Register Rd) implements RegisterFormat {
        }
    }

    @Override
    public String toString() {
        return "Instruction [type=" + type + ", operation=" + operation + ", registers=" + registers + ", immediate="
                + immediate + "]";
    }
}
