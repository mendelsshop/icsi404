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

    // add4 should realy take 4 operands and add them together
    // not the thing we use for multiplication
    public static Word add4(Word a, Word b) {
        return IntStream.range(0, 32).boxed().filter(i -> b.getBit(i).getValue()).map(i -> a.leftShift2(i))
                .reduce(getZero(), ALU::add);

    }

    private static Tuple<Bit, Bit> add2(Bit x, Bit y, Bit cin) {
        var s = x.xor(y).xor(cin);
        var cout = x.and(y).or(x.xor(y).and(cin));
        return new Tuple<Bit, Bit>(s, cout);
    }

    private static Tuple<Bit, Bit> add4(Bit w, Bit x, Bit y, Bit z) {
        var s1 = w.xor(x).xor(y).xor(z);
        var cout1 = w.and(x).or(w.and(y)).or(w.and(z)).or(x.and(y)).or(x.and(z)).or(y.and(z));
        // wxyz
        // w ***
        // x* **
        // y** *
        // z***
        return new Tuple<Bit, Bit>(s1, cout1);
    }

    protected static Word add4_(Word a, Word b, Word c, Word d) {
        var fst = IntStream.range(0, 32).boxed()
                .reduce(new Tuple<>(new Bit[32], new Bit[32]), (tuple, i) -> {
                    var r1 = add4(a.getBit(i), b.getBit(i), c.getBit(i), d.getBit(i));
                    tuple.fst()[i] = r1.fst();
                    if (i == 0) {
                        tuple.snd()[i] = new Bit(false);
                    }
                    if (i < 31) {
                        tuple.snd()[i + 1] = r1.snd();
                    }
                    return tuple;
                }, (i, x) -> x);
        return add(new Word(fst.fst()), new Word(fst.snd()));
    }

    protected static Word add4(Word a, Word b, Word c, Word d) {
        Bit[] fst = IntStream.range(0, 32).boxed()
                .reduce(new Tuple<>(new Bit[32], new Triple<>(new Bit(false), new Bit(false), new Bit(false))),
                        (tuple, i) -> {
                            var s1 = add2(a.getBit(i), b.getBit(i), tuple.snd().fst());
                            var s2 = add2(c.getBit(i), d.getBit(i), tuple.snd().snd());
                            var s3 = add2(s1.fst(), s2.fst(), tuple.snd().thrd());
                            tuple.fst()[i] = s3.fst();
                            return new Tuple<>(tuple.fst(), new Triple<>(s1.snd(), s2.snd(), s3.snd()));
                        }, (i, x) -> x)
                .fst();
        Word add4 = add4_(a, b, c, d);
        if (add4.getSigned() != new Word(fst).getSigned()) {
            System.out.println(add4 + " " + new Word(fst) );
            System.out.println(add4.getSigned() + " " + new Word(fst).getSigned() );
            throw new RuntimeException();
        }
        return new Word(fst);
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
