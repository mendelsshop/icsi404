package UnitTests;

import java.util.function.BiFunction;
import java.util.function.IntConsumer;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static UnitTests.UnitTests.*;
import static Utils.Utils.getBigNumber;
import static Utils.Utils.getZero;

import Computer.*;
import Utils.Utils.Tuple;

/**
 * ALUTests
 */
public class ALUTests {

    @Test
    public void BasicAddTest() {
        assertEquals(0, ALU.add(getZero(), getZero()).getSigned());
        assertEquals(-1, ALU.add(getZero(), getBigNumber()).getSigned());
        assertEquals(-1, ALU.add(getBigNumber(), getZero()).getSigned());
        assertEquals(-2, ALU.add(getBigNumber(), getBigNumber()).getSigned());
        // Testing adddition wrap around
        assertEquals(Integer.MAX_VALUE, ALU.add(new Word(Integer.MIN_VALUE), new Word(-1)).getSigned());
        assertEquals(Integer.MIN_VALUE, ALU.add(new Word(Integer.MAX_VALUE), new Word(1)).getSigned());
        // 2 + 2 = 4
        assertEquals(4, ALU.add(new Word(2), new Word(2)).getSigned());
        // -bignumber + bignumber = 0
        assertEquals(0, ALU.add(new Word(Integer.MAX_VALUE), new Word(Integer.MIN_VALUE + 1)).getSigned());
        // 0 + -7 = -7
        assertEquals(-7, ALU.add(getZero(), new Word(-7)).getSigned());
        // (a + b) + c = a + (b + c)
        assertEquals(ALU.add(ALU.add(new Word(5), new Word(7)), new Word(3)),
                ALU.add(new Word(5), ALU.add(new Word(7), new Word(3))));
    }

    @Test
    public void BasicMulTest() {
        // 0 * n = 0
        assertEquals(0, ALU.mul(getZero(), getZero()).getSigned());
        assertEquals(0, ALU.mul(getBigNumber(), getZero()).getSigned());
        assertEquals(0, ALU.mul(new Word(Integer.MAX_VALUE), getZero()).getSigned());
        assertEquals(0, ALU.mul(new Word(Integer.MIN_VALUE), getZero()).getSigned());
        // -n * k is negative
        assertEquals(Integer.MIN_VALUE + 1, ALU.mul(new Word(Integer.MAX_VALUE), new Word(-1)).getSigned());
        assertEquals(-1, ALU.mul(new Word(1), new Word(-1)).getSigned());
        assertEquals(-25, ALU.mul(new Word(5), new Word(-5)).getSigned());
        // 2 * 2 = 4
        assertEquals(4, ALU.mul(new Word(2), new Word(2)).getSigned());
        // test wrapping nature of multplication
        assertEquals(1_000_000_000 * 5, ALU.mul(new Word(1_000_000_000), new Word(5)).getSigned());
        // -n * -k = n * k
        assertEquals(56, ALU.mul(new Word(-7), new Word(-8)).getSigned());
        assertEquals(1234567 * 89, ALU.mul(new Word(-1_234_567), new Word(-89)).getSigned());

        // do min_value + 1 b/c more negatives then positives
        assertEquals(Integer.MAX_VALUE, ALU.mul(new Word(Integer.MIN_VALUE - 1), new Word(-1)).getSigned());
    }

    @Test
    public void BasicSubTest() {

    }

    public void MatrixDoMath(BiFunction<Integer, Integer, Integer> actualOp, BiFunction<Word, Word, Word> op,
            String opName, Tuple<Integer, Integer> n,
            Tuple<Integer, Integer> m) {
        IntConsumer inner = i -> {
            var n1 = new Word(i);
            doInRange(m.fst(), m.snd(), j -> {
                var n2 = new Word(j);
                var result = op.apply(n1, n2);
                assertEquals((int) actualOp.apply(i, j), (int) result.getUnsigned());
            }, opName + " inner");
        };
        doInRange(n.fst(), n.snd(), inner, opName + " outer");
    }

    @Test
    public void add0to10() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", new Tuple<Integer, Integer>(0, 10),
                new Tuple<Integer, Integer>(0, 10));
    }

    @Test
    public void addnegative10to0() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", new Tuple<Integer, Integer>(-10, 0),
                new Tuple<Integer, Integer>(-10, 0));
    }

    @Test
    public void mul0to10() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", new Tuple<Integer, Integer>(0, 10),
                new Tuple<Integer, Integer>(0, 10));
    }

    @Test
    public void mulnegative10to0() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", new Tuple<Integer, Integer>(-10, 0),
                new Tuple<Integer, Integer>(-10, 0));
    }

    @Test
    public void sub0to10() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", new Tuple<Integer, Integer>(0, 10),
                new Tuple<Integer, Integer>(0, 10));
    }

    @Test
    public void subnegative10to0() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", new Tuple<Integer, Integer>(-10, 0),
                new Tuple<Integer, Integer>(-10, 0));
    }

    @Test
    public void mul() {

        doInRange(10000000, 11110000, i -> {
            var n1 = new Word(new Bit[32]);
            var n2 = new Word(new Bit[32]);
            n1.set2(-i);
            n2.set2(-i);
            assertEquals(i * i, ALU.mul(n1, n2).getSigned2());
        },
                "signed");

    }

    @Test
    public void add() {
        var n1 = new Word(new Bit[32]);
        var n2 = new Word(new Bit[32]);
        var alu = new ALU();

        n1.set(7);
        n2.set(7);
        alu.setOp1(n1);
        alu.setOp2(n2);
        alu.doOperation(ADD);
        System.out.println(alu.getResult().getSigned());
    }
}
