package UnitTests;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import static Utils.Utils.*;

import Computer.Bit;
import Computer.MainMemory;
import Computer.Processor;
import Computer.Word;

public class ProcessorTests {

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
