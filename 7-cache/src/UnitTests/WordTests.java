package UnitTests;

import java.util.Random;

import static Utils.Utils.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static UnitTests.UnitTests.*;

import Computer.*;

/**
 * Word
 */
public class WordTests {
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

    // @Test
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
    public void basicIncrementTest() {
        var zero = getZero();
        zero.increment();
        assertEquals(1, zero.getSigned());
        // checking increment overflow
        var max = new Word(Integer.MAX_VALUE);
        max.increment();
        assertEquals(Integer.MIN_VALUE, max.getSigned());
        var ten = new Word(10);
        ten.increment();
        assertEquals(11, ten.getSigned());
        // negative number incremented to 0
        var negative_one = new Word(-1);
        negative_one.increment();
        assertEquals(0, negative_one.getSigned());
    }

    @Test
    public void IncrementSmallNumbers() {

        doInRange(0, 10, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }

    @Test
    public void IncrementLotOfSmallNumbers() {

        doInRange(10, 100000, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }

    @Test
    public void IncrementNegativeNumbers() {

        doInRange(Integer.MIN_VALUE, Integer.MIN_VALUE + 10, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }

    @Test
    public void IncrementLotsOfNegativeNumbers() {

        doInRange(Integer.MIN_VALUE + 10, Integer.MIN_VALUE + 100000, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }

    @Test
    public void IncrementBigNumbers() {

        doInRange(Integer.MAX_VALUE - 10, Integer.MAX_VALUE, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }

    @Test
    public void IncrementMidNegativeNumbers() {

        doInRange(Integer.MIN_VALUE / 2, (Integer.MIN_VALUE / 2) + 10, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }

    @Test
    public void IncrementMidBigNumbers() {

        doInRange((Integer.MAX_VALUE / 2) - 10, Integer.MAX_VALUE / 2, (n) -> {
            var number = new Word(n);
            number.increment();
            assertEquals(n + 1, number.getSigned());
        }, "increment");
    }
}
