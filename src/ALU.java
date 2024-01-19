public class ALU {

    private static final Bit[] AND = new Bit[] { new Bit(true), new Bit(false), new Bit(false), new Bit(false) };
    private static final Bit[] OR = new Bit[] { new Bit(true), new Bit(false), new Bit(false), new Bit(true) };
    private static final Bit[] XOR = new Bit[] { new Bit(true), new Bit(false), new Bit(true), new Bit(false) };
    private static final Bit[] NOT = new Bit[] { new Bit(true), new Bit(false), new Bit(true), new Bit(true) };
    private static final Bit[] LEFT_SHIFT = new Bit[] { new Bit(true), new Bit(true), new Bit(false), new Bit(false) };
    private static final Bit[] RIGHT_SHIFT = new Bit[] { new Bit(true), new Bit(true), new Bit(false), new Bit(true) };
    private static final Bit[] ADD = new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(false) };
    private static final Bit[] SUB = new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(true) };
    private static final Bit[] MUL = new Bit[] { new Bit(false), new Bit(true), new Bit(true), new Bit(true) };

    private static final Word smallestBitMask = new Word(new Bit[] {
            new Bit(true), new Bit(true), new Bit(true), new Bit(true), new Bit(true),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false),
    });

    public Word op1;
    public Word op2;
    public Word result;

    public void doOperation(Bit[] operation) {
        if (operation == AND) {
            result = op1.and(op2);
        } else if (operation == OR) {
            result = op1.or(op2);
        } else if (operation == XOR) {
            result = op1.xor(op2);
        } else if (operation == NOT) {
            result = op1.not();
        } else if (operation == LEFT_SHIFT) {
            var smallets = op2.and(smallestBitMask);
            result = op1.leftShift((int) smallets.getUnsigned());
        } else if (operation == RIGHT_SHIFT) {
            var smallets = op2.and(smallestBitMask);
            result = op1.rightShift((int) smallets.getUnsigned());
        } else if (operation == ADD) {
            result = add2(op1, op2);
        } else if (operation == SUB) {
        } else if (operation == MUL) {
            result = add4(op1, op2);
        } else {
        }
    }

    private Word add2(Word a, Word b) {
    }

    private Word add4(Word a, Word b) {
    }
}
