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

    public void setOp1(Word op1) {
        this.op1 = op1;
    }

    private Word op1;

    public void setOp2(Word op2) {
        this.op2 = op2;
    }

    private Word op2;
    private Word result;

    public Word getResult() {
        return result;
    }

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
                result = add(op1, op2);
            case SUB -> {
                result = add(op1, op2);
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
                sub(op1, op2).getBit(31).getValue();
            case GE ->
                !sub(op1, op2).getBit(31).getValue();
            case GT -> {
                Word dif = sub(op1, op2);
                yield !(dif.getBit(31).getValue() || dif.equals(getZero()));
            }
            case LE -> {
                Word dif = sub(op1, op2);
                yield dif.getBit(31).getValue() || dif.equals(getZero());
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + getOp(operation));
        };
    }

    public static Word sub(Word a, Word b) {
        var negated = b.negate();
        return add(a, negated);
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

    // not the real multiply just a test method look at mul
    public static Word mul2(Word a, Word b) {
        return IntStream.range(0, 32).boxed().filter(i -> b.getBit(i).getValue()).map(i -> a.leftShift2(i))
                .reduce(getZero(), ALU::add);

    }

    // Full adder
    private static Tuple<Bit, Bit> add2(Bit x, Bit y, Bit cin) {
        var s = x.xor(y).xor(cin);
        var cout = x.and(y).or(x.xor(y).and(cin));
        return new Tuple<Bit, Bit>(s, cout);
    }

    private static Tuple<Bit, Triple<Bit, Bit, Bit>> add4(Bit w, Bit x, Bit y, Bit z, Triple<Bit, Bit, Bit> cin) {
        // TODO add42 for mutable 'fast' version
        Bit cin1 = cin.fst();
        Bit cin2 = cin.snd();
        Bit cin3 = cin.thrd();
        // sum is simple its just a bunch of xors ie only when we have an odd amount of
        // bits are on
        var s2 = w.xor(x).xor(cin1).xor(y).xor(cin2).xor(z).xor(cin3);
        // why cant java have let in or allow nested lambdas nicley???
        // for carry generation we the third carry out depends on second carry out
        // depends on the first carry and the input bits
        // getNewCarriesInner given a bit and another bit that signifies whether we have
        // done a possible carry
        // if both bits are true it means that we should set one of the three return
        // carries depending on if the first or second carry bit is already set
        TriFunction<Bit, Bit, Triple<Bit, Bit, Bit>, Tuple<Bit, Triple<Bit, Bit, Bit>>> getNewCarriesInner = (b, last,
                cout) -> b.and(last).getValue() ? new Tuple<>(new Bit(false),
                        (cout.fst().getValue() ? (cout.snd().getValue() ? cout.setThrd(last) : cout.setSnd(last))
                                : cout.setFst(last)))
                        : new Tuple<>(b.or(last), cout);
        // getNewCarries is just a wrapper around getNewCarriesInner just makes it
        // chainable
        Function<Bit, Function<Tuple<Bit, Triple<Bit, Bit, Bit>>, Tuple<Bit, Triple<Bit, Bit, Bit>>>> getNewCarries = (
                b) -> (lastAndCarries) -> getNewCarriesInner.apply(b, lastAndCarries.fst(), lastAndCarries.snd());
        // we find carries by starting with 3 caries being 0 and then we generate the
        // carry based on each digit from input via the getNewCarries
        var couts = getNewCarries.apply(x).andThen(getNewCarries.apply(y)).andThen(getNewCarries.apply(z))
                .andThen(getNewCarries.apply(cin1)).andThen(getNewCarries.apply(cin2))
                .andThen(getNewCarries.apply(cin3)).apply(new Tuple<>(w,
                        new Triple<Bit, Bit, Bit>(new Bit(false), new Bit(false), new Bit(false))))
                .snd();
        return new Tuple<>(s2, couts);
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
        return add(add4(add4(indexToWord.apply(0), indexToWord.apply(1), indexToWord.apply(2),
                indexToWord.apply(3)),
                add4(indexToWord.apply(4), indexToWord.apply(5), indexToWord.apply(6),
                        indexToWord.apply(7)),
                add4(indexToWord.apply(8), indexToWord.apply(9), indexToWord.apply(10),
                        indexToWord.apply(11)),
                add4(indexToWord.apply(12), indexToWord.apply(13), indexToWord.apply(14),
                        indexToWord.apply(15))),
                add4(add4(indexToWord.apply(16), indexToWord.apply(17), indexToWord.apply(18),
                        indexToWord.apply(19)),
                        add4(indexToWord.apply(20), indexToWord.apply(21),
                                indexToWord.apply(22), indexToWord.apply(23)),
                        add4(indexToWord.apply(24), indexToWord.apply(25),
                                indexToWord.apply(26), indexToWord.apply(27)),
                        add4(indexToWord.apply(28), indexToWord.apply(29),
                                indexToWord.apply(30), indexToWord.apply(31))));
    }
}
