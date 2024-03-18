package UnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Computer.Bit;
import Computer.MainMemory;
import Computer.Processor;
import Computer.Word;
import static Utils.Utils.*;

public class ProcessorTests {

    // NOTE: some test don't run properly when run in parellel sometimes (its vey
    // weird) as the each have their own proccesor, but it coould have to do with
    // main memeory being static (shard)
    @Test
    public void decode() {
        System.out.println(Processor.getNBits(new Word(new Bit[] {
                getTrue(), getTrue(), getTrue(), getFalse(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(),
                getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), }).clone(), 6, 3));
    }

    // @formatter:off
    /*
     * math
     * if/op | and | or | xor | not | left shift | right shift | add | subtract | multiply
     * ------+-----+----+-----+-----+------------+-------------+-----+----------+---------
     * 2R    |yes  |yes |yes  |yes  |yes         |yes          |yes  |yes       |yes
     * ------+-----+----+-----+-----+------------+-------------+-----+----------+---------
     * 3R    |yes  |yes |yes  |yes  |yes         |yes          |yes  |yes       |yes
     *
     * copy | halt
     * ------+------
     * yes | yes
     */
    // @formatter:on

    // some of the more advanced tests are adapted from
    // https://github.com/keon/awesome-bits
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
                "00000000000000000111110001000010",
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

    @Test
    public void processor3() {
        var processor = new Processor();
        MainMemory.load(new String[] {
                // 0000000000000 011 0000 00001 000 01
                // garbage 3 nop r1(rd) math 1r
                // r1 = 3
                "00000000000000001100000000100001",
                // 0000000000000 100 0000 00010 000 01
                // garbage 3 nop r2(rd) math 1r
                // r2 = 4
                "00000000000000010000000001000001",
                // 00000000 00001 00010 1100 00011 000 10
                // garbage r1(rs1) r2(rs2) lshift r3(rd)math3r
                // r3 = r1(3) << r2(4) = 48
                "00000000000010001011000001100010",
                // 0000000000000 00001 1101 00011 000 11
                // garbage r1(rs1) rshift r3(rd)math1r
                // r3 = r3(3) >> r1(3) = 6
                "00000000000000000111010001100011"

        });
        processor.run();
        System.out.println(processor.getRegister(3).getSigned());
        assertEquals(6, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor4() {
        var processor = new Processor();
        MainMemory.load(new String[] {
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=10]
                "00000000000000101001100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=5]
                "00000000000000010101100001000001",
                // Instruction [type=MATH, operation=LEFT_SHIFT,
                // registers=ThreeR[Rs1=Register[number=2], Rs2=Register[number=1],
                // Rd=Register[number=3]], immediate=0]
                "00000000000100000111000001100010",
                // Instruction [type=MATH, operation=RIGHT_SHIFT,
                // registers=TwoR[Rs1=Register[number=1], Rd=Register[number=3]], immediate=0]
                "00000000000000000111010001100011"
        });
        processor.run();
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(5, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor5() {
        var processor = new Processor();
        MainMemory.load(new String[] {
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=2]
                "00000000000000001001100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=4]
                "00000000000000010001100001000001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=3]], immediate=8]
                "00000000000000100001100001100001",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=4]], immediate=0]
                "00000000000010001010100010000010",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=2], Rs2=Register[number=3],
                // Rd=Register[number=5]], immediate=0]
                "00000000000100001110100010100010",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=6]], immediate=16]
                "00000000000001000001100011000001",
                // Instruction [type=MATH, operation=XOR, registers=TwoR[Rs1=Register[number=3],
                // Rd=Register[number=6]], immediate=0]
                "00000000000000001110100011000011",
        });
        processor.run();
        System.out.println(processor.getRegister(6).getSigned());
        System.out.println(processor.getRegister(5).getSigned());
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(6, processor.getRegister(4).getUnsigned());
        assertEquals(12, processor.getRegister(5).getUnsigned());
        assertEquals(24, processor.getRegister(6).getUnsigned());
    }

    @Test
    public void processor6() {
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=10]
                "00000000000000101001100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=9]
                "00000000000000100101100001000001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=3]], immediate=8]
                "00000000000000100001100001100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=4]], immediate=7]
                "00000000000000011101100010000001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=5]], immediate=6]
                "00000000000000011001100010100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=6]], immediate=5]
                "00000000000000010101100011000001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=7]], immediate=4]
                "00000000000000010001100011100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=8]], immediate=3]
                "00000000000000001101100100000001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=9]], immediate=2]
                "00000000000000001001100100100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=10]], immediate=1]
                "00000000000000000101100101000001",
                // Instruction [type=MATH, operation=AND,
                // registers=ThreeR[Rs1=Register[number=10], Rs2=Register[number=10],
                // Rd=Register[number=10]], immediate=0]
                // "00000000010100101010000101000010",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=9]], immediate=0]
                "00000000000000101010000100100011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=8]], immediate=0]
                "00000000000000101010000100000011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=7]], immediate=0]
                "00000000000000101010000011100011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=6]], immediate=0]
                "00000000000000101010000011000011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=5]], immediate=0]
                "00000000000000101010000010100011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=4]], immediate=0]
                "00000000000000101010000010000011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=3]], immediate=0]
                "00000000000000101010000001100011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=2]], immediate=0]
                "00000000000000101010000001000011",
                // Instruction [type=MATH, operation=AND,
                // registers=TwoR[Rs1=Register[number=10], Rd=Register[number=1]], immediate=0]
                "00000000000000101010000000100011",

        });
        processor.run();
        System.out.println("10: " + processor.getRegister(10).getSigned());
        System.out.println(processor.getRegister(9).getSigned());
        System.out.println(processor.getRegister(8).getSigned());
        System.out.println(processor.getRegister(7).getSigned());
        System.out.println(processor.getRegister(6).getSigned());
        System.out.println(processor.getRegister(5).getSigned());
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(1, processor.getRegister(10).getUnsigned());
        assertEquals(0, processor.getRegister(9).getUnsigned());
        assertEquals(1, processor.getRegister(8).getUnsigned());
        assertEquals(0, processor.getRegister(7).getUnsigned());
        assertEquals(1, processor.getRegister(6).getUnsigned());
        assertEquals(0, processor.getRegister(5).getUnsigned());
        assertEquals(1, processor.getRegister(4).getUnsigned());
        assertEquals(0, processor.getRegister(3).getUnsigned());
        assertEquals(1, processor.getRegister(2).getUnsigned());
        assertEquals(0, processor.getRegister(1).getUnsigned());
    }

    @Test
    public void processor7() {
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=10]
                "00000000000000101001100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=1]
                "00000000000000000101100001000001",
                // Instruction [type=MATH, operation=ADD,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=3]], immediate=0]
                "00000000000010001011100001100010",
                // Instruction [type=MATH, operation=OR,
                // registers=ThreeR[Rs1=Register[number=3], Rs2=Register[number=1],
                // Rd=Register[number=4]], immediate=0]
                "00000000000110000110010010000010",
                // Instruction [type=MATH, operation=ADD,
                // registers=ThreeR[Rs1=Register[number=2], Rs2=Register[number=3],
                // Rd=Register[number=1]], immediate=0]
                "00000000000100001111100000100010",
                // Instruction [type=MATH, operation=OR, registers=TwoR[Rs1=Register[number=1],
                // Rd=Register[number=3]], immediate=0]
                "00000000000000000110010001100011",

        });
        processor.run();
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(11, processor.getRegister(4).getUnsigned());
        assertEquals(15, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor8() {
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=1]
                "00000000000000000101100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=500]
                "00000000011111010001100001000001",
                // Instruction [type=MATH, operation=NOT, registers=TwoR[Rs1=Register[number=2],
                // Rd=Register[number=2]], immediate=0]
                "00000000000000001010110001000011",
                // Instruction [type=MATH, operation=ADD, registers=TwoR[Rs1=Register[number=1],
                // Rd=Register[number=2]], immediate=0]
                "00000000000000000111100001000011",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=3]], immediate=10]
                "00000000000000101001100001100001",
                // Instruction [type=MATH, operation=SUB,
                // registers=ThreeR[Rs1=Register[number=0], Rs2=Register[number=3],
                // Rd=Register[number=3]], immediate=0]
                "00000000000000001111110001100010",
                // Instruction [type=MATH, operation=NOT,
                // registers=ThreeR[Rs1=Register[number=3], Rs2=Register[number=3],
                // Rd=Register[number=3]], immediate=0]
                "00000000000110001110110001100010",

        });
        processor.run();
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(-500, processor.getRegister(2).getSigned());
        assertEquals(9, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor9() {
        // abs
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=100]
                "00000000000110010001100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=31]
                "00000000000001111101100001000001",
                // Instruction [type=MATH, operation=RIGHT_SHIFT,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=3]], immediate=0]
                "00000000000010001011010001100010",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=3],
                // Rd=Register[number=4]], immediate=0]
                "00000000000010001110100010000010",
                // Instruction [type=MATH, operation=SUB,
                // registers=ThreeR[Rs1=Register[number=4], Rs2=Register[number=3],
                // Rd=Register[number=5]], immediate=0]
                "00000000001000001111110010100010",
        });
        processor.run();
        System.out.println(processor.getRegister(5).getSigned());
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(100, processor.getRegister(5).getUnsigned());
    }

    @Test
    public void processor10() {
        // avg 1
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=5]
                "00000000000000010101100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=7]
                "00000000000000011101100001000001",
                // Instruction [type=MATH, operation=ADD,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=3]], immediate=0]
                "00000000000010001011100001100010",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=4]], immediate=1]
                "00000000000000000101100010000001",
                // Instruction [type=MATH, operation=RIGHT_SHIFT,
                // registers=ThreeR[Rs1=Register[number=3], Rs2=Register[number=4],
                // Rd=Register[number=3]], immediate=0]
                "00000000000110010011010001100010",
        });
        processor.run();
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(6, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor11() {
        // swap
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=10]
                "00000000000000101001100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=20]
                "00000000000001010001100001000001",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=1]], immediate=0]
                "00000000000010001010100000100010",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=2], Rs2=Register[number=1],
                // Rd=Register[number=2]], immediate=0]
                "00000000000100000110100001000010",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=1]], immediate=0]
                "00000000000010001010100000100010",
        });
        processor.run();
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(10, processor.getRegister(2).getSigned());
        assertEquals(20, processor.getRegister(1).getUnsigned());
    }

    @Test
    public void processor12() {
        // avg 2
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=5]
                "00000000000000010101100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=2]], immediate=7]
                "00000000000000011101100001000001",
                // Instruction [type=MATH, operation=XOR,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=3]], immediate=0]
                "00000000000010001010100001100010",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=4]], immediate=1]
                "00000000000000000101100010000001",
                // Instruction [type=MATH, operation=RIGHT_SHIFT,
                // registers=ThreeR[Rs1=Register[number=3], Rs2=Register[number=4],
                // Rd=Register[number=3]], immediate=0]
                "00000000000110010011010001100010",
                // Instruction [type=MATH, operation=AND,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=2],
                // Rd=Register[number=4]], immediate=0]
                "00000000000010001010000010000010",
                // Instruction [type=MATH, operation=ADD,
                // registers=ThreeR[Rs1=Register[number=3], Rs2=Register[number=4],
                // Rd=Register[number=3]], immediate=0]
                "00000000000110010011100001100010",
        });
        processor.run();
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(6, processor.getRegister(3).getUnsigned());
    }

    @Test
    public void processor13() {
        // max int
        var processor = new Processor();
        MainMemory.load(new String[] {

                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=1]], immediate=1]
                "00000000000000000101100000100001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=7]], immediate=31]
                "00000000000001111101100011100001",
                // Instruction [type=MATH, operation=LEFT_SHIFT,
                // registers=ThreeR[Rs1=Register[number=1], Rs2=Register[number=7],
                // Rd=Register[number=2]], immediate=0]
                "00000000000010011111000001000010",
                // Instruction [type=MATH, operation=NOT, registers=TwoR[Rs1=Register[number=2],
                // Rd=Register[number=3]], immediate=0]
                "00000000000000001010110001100011",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=4]], immediate=1]
                "00000000000000000101100010000001",
                // Instruction [type=MATH, operation=NOP,
                // registers=DestOnly[Rd=Register[number=6]], immediate=31]
                "00000000000001111101100011000001",
                // Instruction [type=MATH, operation=LEFT_SHIFT,
                // registers=ThreeR[Rs1=Register[number=4], Rs2=Register[number=6],
                // Rd=Register[number=5]], immediate=0]
                "00000000001000011011000010100010",
                // Instruction [type=MATH, operation=SUB,
                // registers=ThreeR[Rs1=Register[number=5], Rs2=Register[number=4],
                // Rd=Register[number=6]], immediate=0]
                "00000000001010010011110011000010",
        });
        processor.run();
        System.out.println(processor.getRegister(7).getSigned());
        System.out.println(processor.getRegister(6).getSigned());
        System.out.println(processor.getRegister(5).getSigned());
        System.out.println(processor.getRegister(4).getSigned());
        System.out.println(processor.getRegister(3).getSigned());
        System.out.println(processor.getRegister(2).getSigned());
        System.out.println(processor.getRegister(1).getSigned());
        assertEquals(Integer.MAX_VALUE, processor.getRegister(6).getSigned());
    }
}
