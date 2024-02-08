package Computer;

import static Utils.Utils.getZero;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

import Utils.Utils.*;

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
            new Bit(true), new Bit(true), new Bit(true), new Bit(true), new Bit(true), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false), new Bit(false),
            new Bit(false),
    });

    public Word op1;
    public Word op2;
    public Word result;

    public void doOperation(Bit[] operation) {
        if (Arrays.equals(operation, AND)) {
            result = op1.and(op2);
        } else if (Arrays.equals(operation, OR)) {
            result = op1.or(op2);
        } else if (Arrays.equals(operation, XOR)) {
            result = op1.xor(op2);
        } else if (Arrays.equals(operation, NOT)) {
            result = op1.not();
        } else if (Arrays.equals(operation, LEFT_SHIFT)) {
            var smallets = op2.and(smallestBitMask);
            result = op1.leftShift((int) smallets.getUnsigned());
        } else if (Arrays.equals(operation, RIGHT_SHIFT)) {
            var smallets = op2.and(smallestBitMask);
            result = op1.rightShift((int) smallets.getUnsigned());
        } else if (Arrays.equals(operation, ADD)) {
            result = add(op1, op2);
        } else if (Arrays.equals(operation, SUB)) {
            var negated = op2.negate();
            result = add(op1, negated);
        } else if (Arrays.equals(operation, MUL)) {
            result = mul(op1, op2);
        } else {
            throw new RuntimeException();
        }
    }

    public static Word add(Word a, Word b) {
        Bit[] fst = IntStream.range(0, 32).boxed().reduce(new Tuple<>(new Bit[32], new Bit(false)), (tuple, i) -> {
            var result = add2(a.getBit(i), b.getBit(i), tuple.snd());
            tuple.fst()[i] = result.fst();
            var cout = result.snd();
            return new Tuple<>(tuple.fst(), cout);
        }, (i, x) -> x).fst();
        return new Word(fst);
    }

    public static Word mul2(Word a, Word b) {
        return IntStream.range(0, 32).boxed().filter(i -> b.getBit(i).getValue()).map(i -> a.leftShift2(i))
                .reduce(getZero(), ALU::add);

    }

    private static Tuple<Bit, Bit> add2(Bit x, Bit y, Bit cin) {
        var s = x.xor(y).xor(cin);
        var cout = x.and(y).or(x.xor(y).and(cin));
        return new Tuple<Bit, Bit>(s, cout);
    }

    private static Tuple<Bit, Triple<Bit, Bit, Bit>> add4(Bit w, Bit x, Bit y, Bit z, Triple<Bit, Bit, Bit> cin) {
        // Bit cin1 = cin.fst();
        // Bit cin2 = cin.snd();
        // Bit cin3 = cin.thrd();
        // // TODO: tructhables to find better way to get cout1, 2
        // // then do all required gates once, with and stack them up ie cout1, may
        // depend on partial sums
        // var s1 = w.xor(x).xor(cin1);
        // var cout = w.and(x).or(w.xor(x).and(cin1));
        // var cout1 = s1.and(y).or(s1.and(cin2));
        // var cout2 =
        // w.xor(x).xor(cin1).xor(y).xor(cin2).and(z).or(w.xor(x).xor(cin1).xor(y).xor(cin2).xor(z).and(cin3));
        // var s = w.xor(x).xor(cin1).xor(y).xor(cin2).xor(z).xor(cin3);
        // return new Tuple<>(s, new Triple<>(cout, cout1, cout2));
        // var r1 = add2(w, x, cin.fst());
        Bit cin1 = cin.fst();
        var partial_s = w.xor(x);
        var s = partial_s.xor(cin1);
        var cout = w.and(x).or(partial_s.and(cin1));
        // var r2 = add2(s, y, cin.snd());
        Bit cin2 = cin.snd();
        var partial_s1 = s.xor(y);
        var s1 = partial_s1.xor(cin2);
        // var s1 = w.xor(x) .xor(cin1).xor(y).xor(cin2);
        var cout1 = s.and(y).or(partial_s1.and(cin2));
        // var cout1 = w.xor(x).xor(cin1).and(y).or(w.xor(x).xor(cin1).xor(y).and(cin2));
        // var r3 = add2(s1, z, cin.thrd());
        Bit cin3 = cin.thrd();
        var partial_s2 = s1.xor(z);
        var s2 = partial_s2.xor(cin3);
        // var s2 = w.xor(x).xor(cin1).xor(y).xor(cin2).xor(z).xor(cin3);
        var cout2 = s1.and(z).or(s1.xor(z).and(cin3));
        // var cout2 = w.xor(x).xor(cin1).xor(y).xor(cin2).and(z).or(w.xor(x).xor(cin1).xor(y).xor(cin2).xor(z).and(cin3));
        return new Tuple<>(s2, new Triple<>(cout, cout1, cout2));
    }

    protected static Word add4(Word a, Word b, Word c, Word d) {
        var bits = IntStream.range(0, 32).boxed()
                .reduce(new Tuple<>(new Bit[32], new Triple<>(new Bit(false), new Bit(false), new Bit(false))),
                        (tuple, i) -> {
                            var r1 = add4(a.getBit(i), b.getBit(i), c.getBit(i), d.getBit(i), tuple.snd());
                            tuple.fst()[i] = r1.fst();
                            return new Tuple<>(tuple.fst(), r1.snd());
                        }, (i, x) -> x)
                .fst();
        return new Word(bits);
    }

    public static Word mul(Word a, Word b) {
        Function<Integer, Word> indexToWord = i -> b.getBit(i).getValue() ? a.leftShift2(i) : getZero();
        return add(
                add4(
                        add4(indexToWord.apply(0), indexToWord.apply(1), indexToWord.apply(2),
                                indexToWord.apply(3)),
                        add4(indexToWord.apply(4), indexToWord.apply(5), indexToWord.apply(6),
                                indexToWord.apply(7)),
                        add4(indexToWord.apply(8), indexToWord.apply(9), indexToWord.apply(10),
                                indexToWord.apply(11)),
                        add4(indexToWord.apply(12), indexToWord.apply(13), indexToWord.apply(14),
                                indexToWord.apply(15))),
                add4(
                        add4(indexToWord.apply(16), indexToWord.apply(17), indexToWord.apply(18),
                                indexToWord.apply(19)),
                        add4(indexToWord.apply(20), indexToWord.apply(21), indexToWord.apply(22),
                                indexToWord.apply(23)),
                        add4(indexToWord.apply(24), indexToWord.apply(25), indexToWord.apply(26),
                                indexToWord.apply(27)),
                        add4(indexToWord.apply(28), indexToWord.apply(29), indexToWord.apply(30),
                                indexToWord.apply(31))));
    }
}
