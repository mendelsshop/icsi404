import java.util.stream.IntStream;

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
    private static final Word ZERO = new Word(new Bit[] {
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false),
    });

    public Word op1;
    public Word op2;
    public Word result;

    private record Tuple<T, U>(T car, U cdr) {
    }

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

    protected static Word add2(Word a, Word b) {
        Bit[] car = IntStream.range(0, 32).boxed().reduce(new Tuple<>(new Bit[32], new Bit(false)), (tuple, i) -> {
            var x = a.getBit(i);
            var y = b.getBit(i);
            var cin = tuple.cdr;
            tuple.car[i] = x.xor(y).xor(cin);
            var cout = x.and(y).or(x.xor(y).and(cin));
            return new Tuple<>(tuple.car, cout);
        }, (i, x) -> x).car;
        return new Word(car);
    }

    // add4 should realy take 4 operands and add them together
    // not the thing we use for multiplication
    protected static Word add4(Word a, Word b) {
        return IntStream.range(0, 32).boxed().filter(i->b.getBit(i).getValue()).map(i->a.leftShift(i)).reduce(ALU::add2).get();
        
    }
}
