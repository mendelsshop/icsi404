package Computer;

public class Processor {
    private Word PC = new Word(new Bit[32]);
    private Word SP = new Word(new Bit[32]);
    private Word currentInstruction = new Word(new Bit[32]);
    private Bit halted = new Bit(false);

    public Processor() {
        PC.set(0);
        SP.set(1024);
    }

    private void decode() {
    }

    private void execute() {
    }

    private void store() {
    }

    private void fetch() {
        currentInstruction = MainMemory.read(PC);
        PC.increment();
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
