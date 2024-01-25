package Computer;

import static Utils.Utils.getZero;

import java.util.function.Function;
import java.util.stream.IntStream;

import Utils.Utils.*;

public class ALU {
    private static enum Operation {
        AND, OR, XOR, NOT, LEFT_SHIFT, RIGHT_SHIFT, ADD, SUB, MUL, EQ, NEQ, LT, GE, GT, LE
    }

    private Operation getOp(Bit[] op) {
        int res = 0;
        res += op[0].getValue() ? (int) Math.pow(2, 0) : 0;
        res += op[1].getValue() ? (int) Math.pow(2, 1) : 0;
        res += op[2].getValue() ? (int) Math.pow(2, 2) : 0;
        res += op[3].getValue() ? (int) Math.pow(2, 3) : 0;
        return switch (res) {
            case 0 -> Operation.EQ;
            case 1 -> Operation.NEQ;
            case 2 -> Operation.LT;
            case 3 -> Operation.GE;
            case 4 -> Operation.GT;
            case 5 -> Operation.LE;
            // no op for 6
            case 7 -> Operation.MUL;
            case 8 -> Operation.AND;
            case 9 -> Operation.OR;
            case 10 -> Operation.XOR;
            case 11 -> Operation.NOT;
            case 12 -> Operation.LEFT_SHIFT;
            case 13 -> Operation.RIGHT_SHIFT;
            case 14 -> Operation.ADD;
            case 15 -> Operation.SUB;
            default -> throw new IllegalArgumentException("Unexpected value: " + res);
        };
    }

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
        switch (getOp(operation)) {
            case AND ->
                result = op1.and(op2);
            case OR ->
                result = op1.or(op2);
            case XOR ->
                result = op1.xor(op2);
            case NOT ->
                result = op1.not();
            case LEFT_SHIFT -> {
                var smallets = op2.and(smallestBitMask);
                result = op1.leftShift((int) smallets.getUnsigned());
            }
            case RIGHT_SHIFT -> {
                var smallets = op2.and(smallestBitMask);
                result = op1.rightShift((int) smallets.getUnsigned());
            }
            case ADD ->
                result = add2(op1, op2);
            case SUB -> {
                result = sub();
            }
            case MUL ->
                result = mul(op1, op2);
            default -> throw new IllegalArgumentException("Unexpected value: " + getOp(operation));
        }
    }

    public boolean doBooleanOperation(Bit[] operation) {
        // TODO: should bops be in alu?
        return switch (getOp(operation)) {
            case EQ ->
                op1.equals(op2);
            case NEQ ->
                !op1.equals(op2);
            case LT ->
                sub().getBit(31).getValue();
            case GE ->
                !sub().getBit(31).getValue();
            case GT -> {
                Word dif = sub();
                yield !(dif.getBit(31).getValue() || dif.equals(getZero()));
            }
            case LE -> {
                Word dif = sub();
                yield dif.getBit(31).getValue() || dif.equals(getZero());
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + getOp(operation));
        };
    }

    private Word sub() {
        var negated = op2.negate();
        return add2(op1, negated);
    }

    protected static Word add2(Word a, Word b) {
        Bit[] fst = IntStream.range(0, 32).boxed().reduce(new Tuple<>(new Bit[32], new Bit(false)), (tuple, i) -> {
            var x = a.getBit(i);
            var y = b.getBit(i);
            var cin = tuple.snd();
            tuple.fst()[i] = x.xor(y).xor(cin);
            var cout = x.and(y).or(x.xor(y).and(cin));
            return new Tuple<>(tuple.fst(), cout);
        }, (i, x) -> x).fst();
        return new Word(fst);
    }

    // add4 should realy take 4 operands and add them together
    // not the thing we use for multiplication
    public static Word add4(Word a, Word b) {
        return IntStream.range(0, 32).boxed().filter(i -> b.getBit(i).getValue()).map(i -> a.leftShift2(i))
                .reduce(getZero(), ALU::add2);

    }

    private static Tuple<Bit, Bit> fullAdder(Bit x, Bit y, Bit cin) {
        var s = x.xor(y).xor(cin);
        var cout = x.and(y).or(x.xor(y).and(cin));
        return new Tuple<Bit, Bit>(s, cout);
    }

    protected static Word add4_real(Word a, Word b, Word c, Word d) {
        Bit[] fst = IntStream.range(0, 32).boxed()
                .reduce(new Tuple<>(new Bit[32], new Triple<>(new Bit(false), new Bit(false), new Bit(false))),
                        (tuple, i) -> {
                            var s1 = fullAdder(a.getBit(i), b.getBit(i), tuple.snd().fst());
                            var s2 = fullAdder(c.getBit(i), d.getBit(i), tuple.snd().snd());
                            var s3 = fullAdder(s1.fst(), s2.fst(), tuple.snd().thrd());
                            tuple.fst()[i] = s3.fst();
                            return new Tuple<>(tuple.fst(), new Triple<>(s1.snd(), s2.snd(), s3.snd()));
                        }, (i, x) -> x)
                .fst();
        return new Word(fst);
    }

    public static Word mul(Word a, Word b) {
        Function<Integer, Word> indexToWord = i -> b.getBit(i).getValue() ? a.leftShift2(i) : getZero();
        return add2(
                add4_real(
                        add4_real(indexToWord.apply(0), indexToWord.apply(1), indexToWord.apply(2),
                                indexToWord.apply(3)),
                        add4_real(indexToWord.apply(4), indexToWord.apply(5), indexToWord.apply(6),
                                indexToWord.apply(7)),
                        add4_real(indexToWord.apply(8), indexToWord.apply(9), indexToWord.apply(10),
                                indexToWord.apply(11)),
                        add4_real(indexToWord.apply(12), indexToWord.apply(13), indexToWord.apply(14),
                                indexToWord.apply(15))),
                add4_real(
                        add4_real(indexToWord.apply(16), indexToWord.apply(17), indexToWord.apply(18),
                                indexToWord.apply(19)),
                        add4_real(indexToWord.apply(20), indexToWord.apply(21), indexToWord.apply(22),
                                indexToWord.apply(23)),
                        add4_real(indexToWord.apply(24), indexToWord.apply(25), indexToWord.apply(26),
                                indexToWord.apply(27)),
                        add4_real(indexToWord.apply(28), indexToWord.apply(29), indexToWord.apply(30),
                                indexToWord.apply(31))));
    }
}
