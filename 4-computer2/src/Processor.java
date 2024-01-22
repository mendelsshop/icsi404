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
    // TODO: techincally i could just do a n bit binary to decimal converter for decoding instructutions
    // especially for registers

    private void decode() {
        IF = currentInstruction.and(new Word(new Bit[] {
                new Bit(true), new Bit(true), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
        }));
        IC = currentInstruction.and(new Word(new Bit[] {
                new Bit(true), new Bit(true), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
                new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
        }));
        switch (getInstructionFormat()) {
            case ZEROR -> {
                Immediate = currentInstruction.rightShift(5);
            }
            case ONER -> {

            }
            case THREER -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
            case TWOR -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
            default -> throw new IllegalArgumentException("Unexpected value: " + getInstructionFormat());
        }
    }

    private void setRDAndFunction() {
        var currentInstruction = this.currentInstruction.rightShift(5);
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
