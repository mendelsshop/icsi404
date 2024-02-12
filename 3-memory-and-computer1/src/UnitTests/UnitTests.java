package UnitTests;

import static Utils.Utils.*;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import org.junit.Test;

import Computer.*;

public class UnitTests {
    private static final Word ZEROONE = new Word(new Bit[] {
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
    });
    private static final Word ONEZERO = new Word(new Bit[] {
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
    });

    private static Duration timeOperation(Runnable r) {
        Instant start = Instant.now();
        r.run();
        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    private static void compareRange(int lowerInclusive, int higherExclusive, IntConsumer doer, IntConsumer doer2,
            String name) {
        var t1 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive)
                .parallel()
                .forEach(doer));
        var t2 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive)
                .parallel()
                .forEach(doer2));

        System.out.println(name + " orginal:" + t1.toMillis() + " new:" + t2.toMillis());
    }

    @Test
    // makes sure the not truth table holds true
    // x |!x
    // +--+--+
    // | 1| 0|
    // +--+--+
    // | 0| 1|
    public void not_bits() {
        var not_false = getFalse().not();
        var not_true = getTrue().not();

        assertEquals(getTrue(), not_false);
        assertEquals(getFalse(), not_true);
    }

    @Test
    // makes sure the or truth table holds true
    // x | y| x | y
    // +--+--+------+
    // | 0| 1| 1 |
    // +--+--+------+
    // | 0| 0| 0 |
    // +--+--+------+
    // | 1| 1| 1 |
    // +--+--+------+
    // | 1| 0| 1 |
    // +--+--+------+
    public void or_bits() {
        var false_or_true = getFalse().or(getTrue());
        var false_or_false = getFalse().or(getFalse());
        var true_or_true = getTrue().or(getTrue());
        var true_or_false = getTrue().or(getFalse());
        assertEquals(getTrue(), false_or_true);
        assertEquals(getFalse(), false_or_false);
        assertEquals(getTrue(), true_or_true);
        assertEquals(getTrue(), true_or_false);
    }

    @Test
    // makes sure the and truth table holds true
    // x | y| x & y
    // +--+--+------+
    // | 0| 1| 0 |
    // +--+--+------+
    // | 0| 0| 0 |
    // +--+--+------+
    // | 1| 1| 1 |
    // +--+--+------+
    // | 1| 0| 0 |
    // +--+--+------+
    public void and_bits() {
        var false_and_true = getFalse().and(getTrue());
        var false_and_false = getFalse().and(getFalse());
        var true_and_true = getTrue().and(getTrue());
        var true_and_false = getTrue().and(getFalse());
        assertEquals(getFalse(), false_and_true);
        assertEquals(getFalse(), false_and_false);
        assertEquals(getTrue(), true_and_true);
        assertEquals(getFalse(), true_and_false);
    }

    @Test
    // makes sure the or truth table holds true
    // x | y| x + y
    // +--+--+------+
    // | 0| 1| 1 |
    // +--+--+------+
    // | 0| 0| 0 |
    // +--+--+------+
    // | 1| 1| 0 |
    // +--+--+------+
    // | 1| 0| 1 |
    // +--+--+------+
    public void xor_bits() {
        var false_xor_true = getFalse().xor(getTrue());
        var false_xor_false = getFalse().xor(getFalse());
        var true_xor_true = getTrue().xor(getTrue());
        var true_xor_false = getTrue().xor(getFalse());
        assertEquals(getTrue(), false_xor_true);
        assertEquals(getFalse(), false_xor_false);
        assertEquals(getFalse(), true_xor_true);
        assertEquals(getTrue(), true_xor_false);
    }

    // Word Tests
    @Test
    public void BasicWordNot() {
        assertEquals(getZero(), getBigNumber().not());
        assertEquals(getBigNumber(), getZero().not());
        assertEquals(getZero(), getBigNumber().not2());
        assertEquals(getBigNumber(), getZero().not2());
    }

    @Test
    public void BasicWordAnd() {
        assertEquals(getZero(), getBigNumber().and(getZero()));
        assertEquals(getBigNumber(), getBigNumber().and(getBigNumber()));
        assertEquals(getZero(), getZero().and(getZero()));
        assertEquals(getZero(), getZero().and(getBigNumber()));
        assertEquals(getZero(), getBigNumber().and2(getZero()));
        assertEquals(getBigNumber(), getBigNumber().and2(getBigNumber()));
        assertEquals(getZero(), getZero().and2(getZero()));
        assertEquals(getZero(), getZero().and2(getBigNumber()));
    }

    @Test
    public void BasicWordOr() {
        assertEquals(getBigNumber(), getBigNumber().or(getZero()));
        assertEquals(getBigNumber(), getBigNumber().or(getBigNumber()));
        assertEquals(getZero(), getZero().or(getZero()));
        assertEquals(getBigNumber(), getZero().or(getBigNumber()));
        assertEquals(getBigNumber(), getBigNumber().or2(getZero()));
        assertEquals(getBigNumber(), getBigNumber().or2(getBigNumber()));
        assertEquals(getZero(), getZero().or2(getZero()));
        assertEquals(getBigNumber(), getZero().or2(getBigNumber()));
    }

    @Test
    public void BasicWordXor() {
        assertEquals(getBigNumber(), getBigNumber().xor(getZero()));
        assertEquals(getZero(), getBigNumber().xor(getBigNumber()));
        assertEquals(getZero(), getZero().xor(getZero()));
        assertEquals(getBigNumber(), getZero().xor(getBigNumber()));
        assertEquals(getBigNumber(), getBigNumber().xor2(getZero()));
        assertEquals(getZero(), getBigNumber().xor2(getBigNumber()));
        assertEquals(getZero(), getZero().xor2(getZero()));
        assertEquals(getBigNumber(), getZero().xor2(getBigNumber()));
    }

    @Test
    public void BasicWordLeftShift() {
        System.out.println(getBigNumber().getSigned());
        compareRange(0, 32, i -> {
            var word = new Word(getTrueBits());
            word = word.leftShift(i);
            assertEquals(-1 << i, word.getSigned());
        },
                (i -> {

                    var word = new Word(getTrueBits());
                    word = word.leftShift2(i);
                    assertEquals(-1 << i, word.getSigned2());

                }), "left shift");

    }

    @Test
    public void BasicWordRightShift() {

        compareRange(0, 32, i -> {
            var word = getBigNumber();
            word = word.rightShift(i);
            assertEquals(-1 >>> i, word.getSigned());
        },
                (i -> {

                    var word = getBigNumber();
                    word = word.rightShift2(i);
                    assertEquals(-1 >>> i, word.getSigned2());

                }), "right shift");

    }

    @Test
    public void BasicWordSet() {
        var big_number = new Word(
                new Bit[] { getTrue(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), });
        big_number.set(2147483647);
        System.out.println(big_number);
        assertEquals(2147483647, big_number.getSigned());
    }

    @Test
    public void leftShiftibyi() {

        compareRange(0, 32, i -> {
            var word = new Word(getTrueBits());
            word.set(i);
            word = word.leftShift(i);
            assertEquals(i << i, word.getSigned());
        },
                (i -> {

                    var word = new Word(getTrueBits());
                    word.set(i);
                    word = word.leftShift2(i);
                    assertEquals(i << i, word.getSigned2());

                }), "left shift");

    }

    @Test
    public void rightShiftibyi() {

        compareRange(0, 32, i -> {
            var word = new Word(getTrueBits());
            word.set(i);
            word = word.rightShift(i);
            assertEquals(i >> i, word.getSigned());
        },
                (i -> {

                    var word = new Word(getTrueBits());
                    word.set(i);
                    word = word.rightShift2(i);
                    assertEquals(i >> i, word.getSigned2());

                }), "right shift");

    }

    @Test
    // This test takes a while as it go from -2147483648 to 2147483647 and make sure
    // the that set and get are correct
    public void intToWord() {
        compareRange(-2147483648, -1073741824, i -> {
            var word = new Word(getFalseBits());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(getFalseBits());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
        System.out.println(-1073741824);
        compareRange(-1073741823, 0, i -> {
            var word = new Word(getFalseBits());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(getFalseBits());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
        System.out.println(0);
        compareRange(1, 1073741823, i -> {
            var word = new Word(getFalseBits());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(getFalseBits());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
        System.out.println(1073741824);
        compareRange(1073741824, 2147483647, i -> {
            var word = new Word(getFalseBits());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(getFalseBits());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
    }

    public Word getZeroOne() {
        return ZEROONE.clone();
    }

    public Word getOneZero() {
        return ONEZERO.clone();
    }

    @Test
    public void AdvancedWordNot() {
        assertEquals(getOneZero(), getZeroOne().not());
        assertEquals(getZeroOne(), getOneZero().not());
        assertEquals(getOneZero(), getZeroOne().not2());
        assertEquals(getZeroOne(), getOneZero().not2());
    }

    @Test
    public void AdvancedWordAnd() {
        assertEquals(getZero(), getZeroOne().and(getOneZero()));
        assertEquals(getZero(), getOneZero().and(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().and(getBigNumber()));
        assertEquals(getOneZero(), getBigNumber().and(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().and(getBigNumber()));
        assertEquals(getZeroOne(), getBigNumber().and(getZeroOne()));
        assertEquals(getZero(), getOneZero().and(getZero()));
        assertEquals(getZero(), getZero().and(getOneZero()));
        assertEquals(getZero(), getZeroOne().and(getZero()));
        assertEquals(getZero(), getZero().and(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().and(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().and(getZeroOne()));

        assertEquals(getZero(), getZeroOne().and2(getOneZero()));
        assertEquals(getZero(), getOneZero().and2(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().and2(getBigNumber()));
        assertEquals(getOneZero(), getBigNumber().and2(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().and2(getBigNumber()));
        assertEquals(getZeroOne(), getBigNumber().and2(getZeroOne()));
        assertEquals(getZero(), getOneZero().and2(getZero()));
        assertEquals(getZero(), getZero().and2(getOneZero()));
        assertEquals(getZero(), getZeroOne().and2(getZero()));
        assertEquals(getZero(), getZero().and2(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().and2(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().and2(getZeroOne()));
    }

    @Test
    public void AdvancedWordOr() {
        assertEquals(getBigNumber(), getZeroOne().or(getOneZero()));
        assertEquals(getBigNumber(), getOneZero().or(getZeroOne()));
        assertEquals(getBigNumber(), getOneZero().or(getBigNumber()));
        assertEquals(getBigNumber(), getBigNumber().or(getOneZero()));
        assertEquals(getBigNumber(), getZeroOne().or(getBigNumber()));
        assertEquals(getBigNumber(), getBigNumber().or(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().or(getZero()));
        assertEquals(getOneZero(), getZero().or(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().or(getZero()));
        assertEquals(getZeroOne(), getZero().or(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().or(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().or(getZeroOne()));

        assertEquals(getBigNumber(), getZeroOne().or2(getOneZero()));
        assertEquals(getBigNumber(), getOneZero().or2(getZeroOne()));
        assertEquals(getBigNumber(), getOneZero().or2(getBigNumber()));
        assertEquals(getBigNumber(), getBigNumber().or2(getOneZero()));
        assertEquals(getBigNumber(), getZeroOne().or2(getBigNumber()));
        assertEquals(getBigNumber(), getBigNumber().or2(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().or2(getZero()));
        assertEquals(getOneZero(), getZero().or2(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().or2(getZero()));
        assertEquals(getZeroOne(), getZero().or2(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().or2(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().or2(getZeroOne()));
    }

    @Test
    public void AdvancedWordXor() {
        assertEquals(getBigNumber(), getZeroOne().xor(getOneZero()));
        assertEquals(getBigNumber(), getOneZero().xor(getZeroOne()));
        assertEquals(getZeroOne(), getOneZero().xor(getBigNumber()));
        assertEquals(getZeroOne(), getBigNumber().xor(getOneZero()));
        assertEquals(getOneZero(), getZeroOne().xor(getBigNumber()));
        assertEquals(getOneZero(), getBigNumber().xor(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().xor(getZero()));
        assertEquals(getOneZero(), getZero().xor(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().xor(getZero()));
        assertEquals(getZeroOne(), getZero().xor(getZeroOne()));
        assertEquals(getZero(), getOneZero().xor(getOneZero()));
        assertEquals(getZero(), getZeroOne().xor(getZeroOne()));

        assertEquals(getBigNumber(), getZeroOne().xor2(getOneZero()));
        assertEquals(getBigNumber(), getOneZero().xor2(getZeroOne()));
        assertEquals(getZeroOne(), getOneZero().xor2(getBigNumber()));
        assertEquals(getZeroOne(), getBigNumber().xor2(getOneZero()));
        assertEquals(getOneZero(), getZeroOne().xor2(getBigNumber()));
        assertEquals(getOneZero(), getBigNumber().xor2(getZeroOne()));
        assertEquals(getOneZero(), getOneZero().xor2(getZero()));
        assertEquals(getOneZero(), getZero().xor2(getOneZero()));
        assertEquals(getZeroOne(), getZeroOne().xor2(getZero()));
        assertEquals(getZeroOne(), getZero().xor2(getZeroOne()));
        assertEquals(getZero(), getOneZero().xor2(getOneZero()));
        assertEquals(getZero(), getZeroOne().xor2(getZeroOne()));
    }

    @Test
    public void AdvancedWordCombinations() {
        assertEquals(0 << 5 & 6 | ~7, getZero().leftShift(5).and(new Word(6)).or(new Word(7).not()).getSigned());
    }

    public void Fuzzer(int times) {
        var rng = new Random();
        var word = getZero();
        var actual = 0;
        for (int i = 0; i < times; i++) {
            switch (rng.nextInt(9)) {
                case 0 -> {
                    var index = rng.nextInt(32);
                    var val = new Bit(rng.nextBoolean());
                    word.setBit(index, val);
                    int mask = (1 << (index));
                    actual = (actual & ~mask | ((val.getValue() ? 1 : 0) << (index)));
                }
                case 1 -> {
                    var val = rng.nextInt(-2147483648, 2147483647);
                    word.set(val);
                    actual = val;
                }
                case 2 -> {
                    word = word.not();
                    actual = ~actual;
                }
                case 3 -> {
                    var shift = rng.nextInt(0, 32);
                    word = word.leftShift(shift);
                    actual = actual << shift;
                }
                case 4 -> {
                    var shift = rng.nextInt(0, 32);
                    word = word.rightShift(shift);
                    // using >>>
                    actual = actual >>> shift;
                }
                case 5 -> {
                    var mask = rng.nextInt(-2147483648, 2147483647);
                    word = word.and(new Word(mask));
                    actual = actual & mask;
                }
                case 6 -> {
                    var mask = rng.nextInt(-2147483648, 2147483647);
                    word = word.or(new Word(mask));
                    actual = actual | mask;
                }
                case 7 -> {
                    var mask = rng.nextInt(-2147483648, 2147483647);
                    word = word.xor(new Word(mask));
                    actual = actual ^ mask;
                }
            }
            assertEquals(actual, word.getSigned());
        }
    }

    @Test
    public void fuzzer100() {
        Fuzzer(100);
    }

    @Test
    public void fuzzer1000() {
        Fuzzer(1000);
    }

    @Test
    public void fuzzer10000() {
        Fuzzer(10000);
    }

    @Test
    public void fuzzer100000() {
        Fuzzer(100000);
    }

    @Test
    public void fuzzer1000000() {
        Fuzzer(1000000);
    }

    @Test
    public void add() {
        var n1 = new Word(new Bit[32]);
        var n2 = new Word(new Bit[32]);
        var alu = new ALU();

        n1.set(7);
        n2.set(7);
        alu.op1 = n1;
        alu.op2 = n2;
        alu.doOperation(new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(true) });
        System.out.println(alu.result.getSigned());
    }

    public static void doInRange(int start, int end, IntConsumer doer, String beingTested) {
        var t1 = timeOperation(() -> IntStream.range(start, end).
        parallel().
                forEach(doer));
        System.out.println("doing " + beingTested + "from " + start + "to end " + end + "took " + t1);
    };

    @Test
    public void add_a_lot() {
        IntConsumer muller = i -> {
            var n1 = new Word(i);
            doInRange(0, 50, j -> {
                var n2 = new Word(j);
                var added = ALU.mul(n1, n2);
                assertEquals(i * j, added.getUnsigned());
            }, "mul inner");
        };
        doInRange(0, 100, muller, "mul outer");
    }

    @Test
    public void mul() {

        doInRange(10000, 100000, i -> {
            var n1 = new Word(new Bit[32]);
            var n2 = new Word(new Bit[32]);
            n1.set2(-i);
            n2.set2(-i);
            assertEquals(i * i, ALU.mul(n1, n2).getSigned2());
        },
                "signed");

    }
}
