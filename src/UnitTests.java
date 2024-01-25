
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
    public void sub() {
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

    @Test
    public void decode() {
        System.out.println(Processor.getNBits(new Word(new Bit[] {
                getTrue(), getTrue(), getTrue(), getFalse(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), }).clone(), 6, 3));
    }

    @Test
    public void processor0() {
        var processor = new Processor();
        MainMemory.load(new String[] {
                // 0000000000000 101 0000 00001 000 01
                // garbage 5 nop r1(rd) math 1r
                // r1 = 5
                "00000000000000010100000000100001",
                // 00000000 00001 00001 1110 00010 000 10
                // r2 = r1(5) + r1(5) = 10
                // garbage r1(rs1)r1(rs2)add r2(rd)math 3r
                "00000000000010000111100001000010",
                // 0000000000000 00010 1110 00010 000 11
                // garbage r2(rs1)add r2(rd)math2r
                // r2 = r2(10) + r2(10) = 20
                "00000000000000001011100001000011",
                // 00000000 00010 00001 1110 00011 000 10
                // garbage r2(rs1)r1(rs2)add r3(rd)math 3r
                // r3 = r2(20) + r1 (5) = 25
                "00000000000100000111100001100010",
                // halt
                "00000000000000000000000000000000"
        });
        processor.run();
        System.out.println(processor.getRegister(3).getUnsigned());
        assertEquals(25, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor1() {
        var processor = new Processor();
        MainMemory.load(new String[] {
                // 0000000000000 101 0000 00001 000 01
                // garbage 5 nop r1(rd) math 1r
                // r1 = 5
                "00000000000000010100000000100001",
                // 0000000000000 101 0000 00010 000 01
                // garbage 5 nop r1(rd) math 1r
                // r2 = 5
                "00000000000000010100000001000001",
                // 0000000000000 00001 0111 00010 000 11
                // garbage r1(rs1)mull r2(rd)math2r
                // r2 = r2(5) * r1(5) = 25
                "00000000000000000101110001000011",
                // 00000000 00001 00010 0111 00011 000 10
                // garbage r1(rs1) r2(rs2)mull r3(rd)math3r
                // r3 = r1(5) * r2(25) = 125
                "00000000000010001001110001100010",
                // halt
                "00000000000000000000000000000000"
        });
        processor.run();
        System.out.println(processor.getRegister(3).getUnsigned());
        assertEquals(125, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor2() {
        var processor = new Processor();
        MainMemory.load(new String[] {
                // 0000000000000 101 0000 00001 000 01
                // garbage 5 nop r1(rd) math 1r
                // r1 = 5
                "00000000000000010100000000100001",
                // 00000000 00000 00001 1111 00010 000 10
                // garbage r0(rs1) r1(rs2)sub r2(rd)math3r
                // r2 = r0(0) - r1(5) = -5
                "00000000000000000111110001000011",
                // 0000000000000 00010 1111 00010 000 11
                // garbage r2(rs1)sub r2(rd)math2r
                // r2 = r2(-5) - r2(-5) = 0
                "00000000000000001011110001000011",
                // halt
                "00000000000000000000000000000000"
        });
        processor.run();
        System.out.println(processor.getRegister(2).getSigned());
        assertEquals(0, processor.getRegister(2).getUnsigned());
    }
}
