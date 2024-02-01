
import static Utils.Utils.*;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import org.junit.Test;

import Computer.*;

public class UnitTests {

    // Word Tests
    @Test
    public void BasicWordNot() {
        assertEquals(getZero(), getBigNumber().not());
    }

    @Test
    public void BasicWordAnd() {
        assertEquals(getZero(), getBigNumber().and(getZero()));
    }

    @Test
    public void BasicWordOr() {
        assertEquals(getBigNumber(), getZero().or(getBigNumber()));
    }

    @Test
    public void BasicWordXor() {
        assertEquals(getZero(), getBigNumber().xor(getBigNumber()));
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
        assertEquals(-6, big_number.getSigned());
        assertEquals(getZero(), big_number);
    }

    private static Duration timeOperation(Runnable r) {
        Instant start = Instant.now();
        r.run();
        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    private static void compareRange(int lowerInclusive, int higherExclusive, IntConsumer doer, IntConsumer doer2,
            String name) {
        var t1 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive).parallel().forEach(doer));
        var t2 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive).parallel().forEach(doer2));

        System.out.println(name + " orginal:" + t1.toMillis() + " new:" + t2.toMillis());
    }

    @Test
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

    @Test
    public void shift() {

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

                }), "signed");

    }

}
