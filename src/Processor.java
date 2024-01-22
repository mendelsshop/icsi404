import java.util.function.Function;

public class Processor {
    private Word PC = new Word(new Bit[32]);
    private Word SP = new Word(new Bit[32]);
    private Word currentInstruction = new Word(new Bit[32]);
    private Bit halted = new Bit(false);

    private Word Immediate;
    private Word Rs1;
    private Word Rs2;
    private Word Rd;
    private Word Function;
    private Word IF;
    private Word IC;

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

    private void setRDAndFunction() {
        var currentInstruction = this.currentInstruction.rightShift(5);
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

    private static record Tuple<T, U>(T fst, U snd) {
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
    }

    private void store() {
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
