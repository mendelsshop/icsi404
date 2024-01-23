import java.util.function.Function;

public class Processor {
    private Word PC = new Word(new Bit[32]);
    private Word SP = new Word(new Bit[32]);
    private Word currentInstruction = new Word(new Bit[32]);
    private Bit halted = new Bit(false);
    private static Word ZERO = new Word(new Bit[] {
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false),
    });
    private Word[] registers = new Word[] {
            ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(),
            ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(),
            ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(),
            ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(),
            ZERO.clone(), ZERO.clone(), ZERO.clone(), ZERO.clone(),
    };

    private void setRegister(int index, Word contents) {
        if (index != 0) {
            registers[index] = contents.clone();
        }
    }

    private Word getRegister(int index) {
        return registers[index].clone();
    }

    private Word Immediate;
    private Word Rs1;
    private Word Rs2;
    private Word Rd;
    private Word Function;
    private Word IF;
    private Word IC;

    private ALU alu;

    public Processor() {
        PC.set(0);
        SP.set(1024);
    }
    // TODO: techincally i could just do a n bit binary to decimal converter for
    // decoding instructutions
    // especially for registers

    private void decode() {
        IF = getNBits(2, 0);
        IC = getNBits(3, 2);
        switch (getInstructionFormat()) {
            case ZEROR -> {
                Immediate = currentInstruction.rightShift(5);
            }
            case ONER -> {
                Rd = getNBits(5, 5);
                Function = getNBits(4, 10);
                Immediate = currentInstruction.rightShift(18);
            }
            case THREER -> {
                Rd = getNBits(5, 5);
                Function = getNBits(4, 10);
                Rs1 = getNBits(5, 14);
                Immediate = currentInstruction.rightShift(13);
            }
            case TWOR -> {

                Rd = getNBits(5, 5);
                Function = getNBits(4, 10);
                Rs1 = getNBits(5, 14);
                Rs2 = getNBits(5, 19);
                Immediate = currentInstruction.rightShift(8);
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + getInstructionFormat());
        }
    }

    private Word getNBits(int size, int shift) {
        return getNBits(currentInstruction, size, shift);
    }

    protected static Word getNBits(Word word, int size, int shift) {
        Function<Integer, Bit> getMaskBit = i -> new Bit(i >= shift && i < shift + size);
        Word mask = new Word(new Bit[] {
                getMaskBit.apply(0), getMaskBit.apply(1), getMaskBit.apply(2), getMaskBit.apply(3), getMaskBit.apply(4),
                getMaskBit.apply(5), getMaskBit.apply(6), getMaskBit.apply(7), getMaskBit.apply(8), getMaskBit.apply(9),
                getMaskBit.apply(10), getMaskBit.apply(11), getMaskBit.apply(12), getMaskBit.apply(13),
                getMaskBit.apply(14), getMaskBit.apply(15), getMaskBit.apply(16), getMaskBit.apply(17),
                getMaskBit.apply(18), getMaskBit.apply(19), getMaskBit.apply(20), getMaskBit.apply(21),
                getMaskBit.apply(22), getMaskBit.apply(23), getMaskBit.apply(24), getMaskBit.apply(25),
                getMaskBit.apply(26), getMaskBit.apply(27), getMaskBit.apply(28), getMaskBit.apply(29),
                getMaskBit.apply(30), getMaskBit.apply(31),
        });
        return word.and(mask).rightShift(shift);
    }

    private static enum InstructionFormat {
        ZEROR,
        ONER,
        TWOR,
        THREER,
    }

    private static enum InstructionCode {
        MATH, BRANCH, CALL, PUSH, LOAD, STORE, POP
    }

    private static record Tuple<T, U>(T fst, U snd) {
    }

    private static record Triple<T, U, V>(T fst, U snd, V thrd) {
    }

    private InstructionCode getInstructionCode() {
        return switch (new Triple<>(IC.getBit(1).getValue(), IC.getBit(1).getValue(), IC.getBit(1).getValue())) {
            case Triple(var fst, var snd, var thrd) when fst && snd && thrd -> throw new RuntimeException();
            case Triple(var fst, var snd, var thrd) when snd && thrd -> InstructionCode.POP;
            case Triple(var fst, var snd, var thrd) when fst && thrd -> InstructionCode.STORE;
            case Triple(var fst, var snd, var thrd) when snd && fst -> InstructionCode.PUSH;
            case Triple(var fst, var snd, var thrd) when thrd -> InstructionCode.LOAD;
            case Triple(var fst, var snd, var thrd) when snd -> InstructionCode.CALL;
            case Triple(var fst, var snd, var thrd) when fst -> InstructionCode.BRANCH;
            default -> throw new IllegalArgumentException("Unexpected value: "
                    + new Triple<>(IC.getBit(1).getValue(), IC.getBit(1).getValue(), IC.getBit(1).getValue()));
        };
    }

    private InstructionFormat getInstructionFormat() {
        return switch (new Tuple<>(IF.getBit(0).getValue(), IF.getBit(1).getValue())) {
            case Tuple(var fst, var snd) when fst && snd -> InstructionFormat.THREER;
            case Tuple(var fst, var snd) when fst -> InstructionFormat.ONER;
            case Tuple(var fst, var snd) when snd -> InstructionFormat.TWOR;
            case Tuple(var fst, var snd) -> InstructionFormat.ZEROR;
            default -> throw new RuntimeException();
        };
    }

    private void execute() {
        var op = new Bit[] {
                Function.getBit(0).clone(),
                Function.getBit(1).clone(),
                Function.getBit(2).clone(),
                Function.getBit(3).clone(),
        };
        switch (getInstructionCode()) {
            case CALL -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            case LOAD -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            case MATH -> {

                switch (getInstructionFormat()) {
                    case ZEROR -> halted.set(true);
                    case ONER -> {
                        // TODO: do we sign extend immidiate
                        // do we store immidiate in the register (probably not) or do we set alu.result
                        // to immidiate and let store do the work (what im doing)
                        alu.result = Immediate;
                    }
                    case THREER -> {
                        // TODO: how to get rs1, rs2 from word
                        alu.doOperation(op);
                    }
                    case TWOR ->
                        // TODO: how to get rs1 from word
                        // put rs1 in op1 and op2
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                    default -> throw new IllegalArgumentException("Unexpected value: " + getInstructionFormat());
                }
                if (getInstructionFormat() == InstructionFormat.ZEROR) {
                    halted.set(true);
                } else {
                    // TODO: how get register and set op1. op2 to right register
                    // TODO: maybe reverse op
                    alu.doOperation(op);

                }
            }
            case POP -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            case PUSH -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            case STORE -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            case BRANCH -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            default -> throw new IllegalArgumentException("Unexpected value: " + getInstructionCode());
        }
    }

    private void store() {
        // TODO: maybe dont store if its a halt
        // TODO: how to get register from Rd
    }

    private void fetch() {
        currentInstruction = MainMemory.read(PC);
        // TODO: is increment in place?
        PC = PC.increment();
    }

    public void run() {
        while (halted.not().getValue()) {
            fetch();
            decode();
            execute();
            store();
        }
    }
}
