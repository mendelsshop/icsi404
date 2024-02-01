
import static Utils.Utils.*;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
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
        // parallel().
        forEach(doer));
        System.out.println("doing " + beingTested + "from " + start + "to end " + end + "took " + t1);
    };

    @Test
    public void add_a_lot() {
        IntConsumer muller = i -> {
            var n1 = new Word(i);
            doInRange(0, 100, j -> {
                var n2 = new Word(j);
                var added = ALU.mul(n1, n2);
                assertEquals(i*j, added.getUnsigned());
            }, "mul inner");
        };
        doInRange(0, 100, muller , "mul outer");
    }

    @Test
    public void mul() {

        compareRange(1000000, 10000000, i -> {
            var n1 = new Word(new Bit[32]);
            var n2 = new Word(new Bit[32]);
            n1.set2(-i);
            n2.set2(-i);
            assertEquals(i * i, ALU.mul(n1, n2).getSigned2());
        },
                (i -> {

                    var n1 = new Word(new Bit[32]);
                    var n2 = new Word(new Bit[32]);
                    n1.set2(-i);
                    n2.set2(-i);
                    assertEquals(i * i, ALU.add4(n1, n2).getSigned2());

                }), "signed");

    }
}
