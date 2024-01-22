import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import org.junit.Test;

public class UnitTests {

    static final Bit False = new Bit(false);
    static final Bit True = new Bit(true);

    static final Bit[] TRUE_BITS = new Bit[] {
            True, True, True, True, True, True, True, True, True, True, True, True, True, True, True, True, True, True,
            True, True, True, True, True, True, True, True, True, True, True, True, True, True,
    };

    static final Bit[] FALSE_BITS = new Bit[] {
            False, False, False, False, False, False, False, False, False, False, False, False, False, False, False,
            False, False, False, False, False, False, False, False, False, False, False, False, False, False, False,
            False, False,
    };

    static final Word ZERO = new Word(FALSE_BITS);
    static final Word BIG_NUMBER = new Word(TRUE_BITS);

    // Word Tests
    @Test
    public void BasicWordNot() {
        assertEquals(ZERO, BIG_NUMBER.not());
    }

    @Test
    public void BasicWordAnd() {
        assertEquals(ZERO, BIG_NUMBER.and(ZERO));
    }

    @Test
    public void BasicWordOr() {
        assertEquals(BIG_NUMBER, ZERO.or(BIG_NUMBER));
    }

    @Test
    public void BasicWordXor() {
        assertEquals(ZERO, BIG_NUMBER.xor(BIG_NUMBER));
    }

    @Test
    public void BasicWordSet() {
        var big_number = new Word(new Bit[] { True, False, False, False, False, False, False, False, False, False,
                False, False, False, False, False, False, False, False, False, False, False, False, False, False, False,
                False, False, False, False, False, False, False, });
        big_number.set(2147483647);
        System.out.println(big_number);
        assertEquals(-6, big_number.getSigned());
        assertEquals(ZERO, big_number);
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
            var word = new Word(FALSE_BITS.clone());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(FALSE_BITS.clone());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
        System.out.println(-1073741824);
        compareRange(-1073741823, 0, i -> {
            var word = new Word(FALSE_BITS.clone());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(FALSE_BITS.clone());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
        System.out.println(0);
        compareRange(1, 1073741823, i -> {
            var word = new Word(FALSE_BITS.clone());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(FALSE_BITS.clone());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
        System.out.println(1073741824);
        compareRange(1073741824, 2147483647, i -> {
            var word = new Word(FALSE_BITS.clone());
            word.set2(i);
            assertEquals(i, word.getSigned2());
        },
                (i -> {
                    var word = new Word(FALSE_BITS.clone());
                    word.set(i);
                    assertEquals(i, word.getSigned());
                }), "signed");
    }

    @Test
    public void shift() {

        compareRange(0, 32, i -> {
            var word = new Word(TRUE_BITS.clone());
            word.set(i);
            word = word.leftShift(i);
            assertEquals(i << i, word.getSigned());
        },
                (i -> {

                    var word = new Word(TRUE_BITS.clone());
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


    @Test public void decode() {
        System.out.println(Processor.getNBits(new Word(new Bit[] {
                True, True, True, False, True, True, True, True, True, True, True, True, True, True, True, True, True, True,
                True, True, True, True, True, True, True, True, True, True, True, True, True, True,
        }).clone(), 6, 3));
    }
}
