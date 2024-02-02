
import static Utils.Utils.*;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import org.junit.Test;

import Computer.*;

public class UnitTests {

    private static Duration timeOperation(Runnable r) {
        Instant start = Instant.now();
        r.run();
        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    private static void compareRange(int lowerInclusive, int higherExclusive, IntConsumer doer, IntConsumer doer2,
            String name) {
        var t1 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive)
                // .parallel()
                .forEach(doer));
        var t2 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive)
                // .parallel()
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
}
