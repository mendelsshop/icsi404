package UnitTests;

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
        assertEquals(Integer.MAX_VALUE, ALU.mul(new Word(Integer.MIN_VALUE + 1), new Word(-1)).getSigned());
    }

    @Test
    public void BasicSubTest() {

        assertEquals(0, ALU.sub(getZero(), getZero()).getSigned());
        assertEquals(1, ALU.sub(getZero(), getBigNumber()).getSigned());
        assertEquals(-1, ALU.sub(getBigNumber(), getZero()).getSigned());
        assertEquals(0, ALU.sub(getBigNumber(), getBigNumber()).getSigned());
        // Testing subtraction wrap around
        assertEquals(Integer.MAX_VALUE, ALU.sub(new Word(Integer.MIN_VALUE), new Word(1)).getSigned());
        assertEquals(Integer.MIN_VALUE, ALU.sub(new Word(Integer.MAX_VALUE), new Word(-1)).getSigned());
        // 2 - 2 = 0
        assertEquals(0, ALU.sub(new Word(2), new Word(2)).getSigned());
        // bignumber - bignumber = 0
        assertEquals(0, ALU.sub(new Word(Integer.MIN_VALUE), new Word(Integer.MIN_VALUE)).getSigned());
        // 0 - -7 = 7
        assertEquals(7, ALU.sub(getZero(), new Word(-7)).getSigned());

    }

    private static final Tuple<Integer, Integer> RANGE_0_TO_10 = new Tuple<Integer, Integer>(0, 10);
    private static final Tuple<Integer, Integer> RANGE_NEGATIVE_10_TO_0 = new Tuple<Integer, Integer>(-10, 0);
    private static final Tuple<Integer, Integer> RANGE_BIG_NUMBERS = new Tuple<Integer, Integer>(Integer.MAX_VALUE - 10,
            Integer.MAX_VALUE);
    private static final Tuple<Integer, Integer> RANGE_NEGATIVE_BIG_NUMBERS = new Tuple<Integer, Integer>(
            Integer.MIN_VALUE, Integer.MIN_VALUE + 10);
    private static final Tuple<Integer, Integer> RANGE_MID_SIZE_NUMBERS = new Tuple<Integer, Integer>(
            (Integer.MAX_VALUE / 2) - 10, (Integer.MAX_VALUE / 2));
    private static final Tuple<Integer, Integer> RANGE_NEGATIVE_MID_SIZE_NUMBERS = new Tuple<Integer, Integer>(
            (Integer.MIN_VALUE / 2), (Integer.MIN_VALUE / 2) + 10);

    @Test
    public void add0to10() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", RANGE_0_TO_10, RANGE_0_TO_10);
    }

    @Test
    public void addnegative10to0() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", RANGE_NEGATIVE_10_TO_0, RANGE_NEGATIVE_10_TO_0);
    }

    @Test
    public void mul0to10() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", RANGE_0_TO_10, RANGE_0_TO_10);
    }

    @Test
    public void mulnegative10to0() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", RANGE_NEGATIVE_10_TO_0, RANGE_NEGATIVE_10_TO_0);
    }

    @Test
    public void sub0to10() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", RANGE_0_TO_10, RANGE_0_TO_10);
    }

    @Test
    public void subnegative10to0() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", RANGE_NEGATIVE_10_TO_0, RANGE_NEGATIVE_10_TO_0);
    }

    @Test
    public void addbignumbertbignubmer() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", RANGE_BIG_NUMBERS, RANGE_BIG_NUMBERS);
    }

    @Test
    public void addnegativebignumbertonegativebignumber() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", RANGE_NEGATIVE_BIG_NUMBERS, RANGE_NEGATIVE_BIG_NUMBERS);
    }

    @Test
    public void mulbignumbertobignumber() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", RANGE_BIG_NUMBERS, RANGE_BIG_NUMBERS);
    }

    @Test
    public void mulnegativebignumbertonegativebignumber() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", RANGE_NEGATIVE_BIG_NUMBERS, RANGE_NEGATIVE_BIG_NUMBERS);
    }

    @Test
    public void subbignumbertobignumber() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", RANGE_BIG_NUMBERS, RANGE_BIG_NUMBERS);
    }

    @Test
    public void subnegativebignumbertonegativebignumber() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", RANGE_NEGATIVE_BIG_NUMBERS, RANGE_NEGATIVE_BIG_NUMBERS);
    }

    @Test
    public void addmiddlenumbertomiddlenumber() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", RANGE_MID_SIZE_NUMBERS, RANGE_MID_SIZE_NUMBERS);
    }

    @Test
    public void addnegativemiddlenumbertonegativemiddlenumber() {
        MatrixDoMath((a, b) -> a + b, ALU::add, "add", RANGE_NEGATIVE_MID_SIZE_NUMBERS,
                RANGE_NEGATIVE_MID_SIZE_NUMBERS);
    }

    @Test
    public void mulmiddlenumbertomiddlenumber() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", RANGE_MID_SIZE_NUMBERS, RANGE_MID_SIZE_NUMBERS);
    }

    @Test
    public void mulnegativemiddlenumbertonegativemiddlenumber() {
        MatrixDoMath((a, b) -> a * b, ALU::mul, "mul", RANGE_NEGATIVE_MID_SIZE_NUMBERS,
                RANGE_NEGATIVE_MID_SIZE_NUMBERS);
    }

    @Test
    public void submiddlenumbertomiddlenumber() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", RANGE_MID_SIZE_NUMBERS, RANGE_MID_SIZE_NUMBERS);
    }

    @Test
    public void subnegativemiddlenumbertonegativemiddlenumber() {
        MatrixDoMath((a, b) -> a - b, ALU::sub, "sub", RANGE_NEGATIVE_MID_SIZE_NUMBERS,
                RANGE_NEGATIVE_MID_SIZE_NUMBERS);
    }

    @Test
    // tests that the alu properly decodes and does the correct instruction for a
    // given 4 bits
    public void ALUDecodeTest() {

        var alu = new ALU();
        // and
        alu.setOp1(new Word(56));
        alu.setOp2(new Word(900_000));
        alu.doOperation(AND);
        assertEquals(56 & 900_000, alu.getResult().getSigned());

        // or
        alu.setOp1(new Word(-756));
        alu.setOp2(new Word(24000));
        alu.doOperation(OR);
        assertEquals(-756 | 24000, alu.getResult().getSigned());

        // xor
        alu.setOp1(new Word(99_999));
        alu.setOp2(new Word(654));
        alu.doOperation(XOR);
        assertEquals(99_999 ^ 654, alu.getResult().getSigned());

        // not
        alu.setOp1(new Word(135790));
        alu.doOperation(NOT);
        assertEquals(~135790, alu.getResult().getSigned());

        // left shift
        alu.setOp1(new Word(891));
        alu.setOp2(new Word(27));
        alu.doOperation(LEFT_SHIFT);
        assertEquals(891 << 27, alu.getResult().getSigned());

        // right shift
        alu.setOp1(new Word(-1_000_000));
        alu.setOp2(new Word(13));
        alu.doOperation(RIGHT_SHIFT);
        assertEquals((-1_000_000) >>> 13, alu.getResult().getSigned());

        // add
        alu.setOp1(new Word(1795));
        alu.setOp2(new Word(1820));
        alu.doOperation(ADD);
        assertEquals(1795 + 1820, alu.getResult().getSigned());

        // sub
        alu.setOp1(new Word(613));
        alu.setOp2(new Word(248));
        alu.doOperation(SUB);
        assertEquals(613 - 248, alu.getResult().getSigned());

        // mul
        alu.setOp1(new Word(-2));
        alu.setOp2(new Word(65));
        alu.doOperation(MUL);
        assertEquals(-2 * 65, alu.getResult().getSigned());
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
