package Computer;

import Utils.Utils.*;
import static Utils.Utils.*;

import java.util.function.Function;

public class Processor {
        private Word PC = new Word(new Bit[32]);
        private Word SP = new Word(new Bit[32]);
        private Word currentInstruction = new Word(new Bit[32]);
        private Bit halted = new Bit(false);

        private Word[] registers = new Word[] {
                        getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
                        getZero(),
                        getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
                        getZero(),
                        getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
                        getZero(),
                        getZero(), getZero(), getZero(), getZero(), getZero(), };

        private void setRegister(int index, Word contents) {
                if (index != 0) {
                        registers[index] = contents.clone();
                }
        }

        public Word getRegister(int index) {
                return registers[index].clone();
        }

        private Word Immediate;
        private Word Rs1;
        private Word Rs2;
        private Word Rd;
        private Word Function;
        private Word IF;
        private Word IC;

        private ALU alu = new ALU();

        public Processor() {
                PC.set(0);
                SP.set(1024);
        }

        private void decode() {
                // get if (0r/1r/2r/3r)
                IF = getNBits(2, 0);
                // get instruction type
                IC = getNBits(3, 2);
                // decode based if
                switch (getInstructionFormat()) {
                        // for immedieats there is no reason to mask as they are the last part of word
                        case ZEROR -> {
                                // shift right by 5 to get the 27 bit immediate value
                                Immediate = currentInstruction.rightShift(5);
                        }
                        case ONER -> {
                                Rd = getNBits(5, 5);
                                Function = getNBits(4, 10);
                                Immediate = currentInstruction.rightShift(14);
                        }
                        case TWOR -> {
                                Rd = getNBits(5, 5);
                                Function = getNBits(4, 10);
                                Rs1 = getNBits(5, 14);
                                Immediate = currentInstruction.rightShift(19);
                        }
                        case THREER -> {

                                Rd = getNBits(5, 5);
                                Function = getNBits(4, 10);
                                Rs2 = getNBits(5, 14);
                                Rs1 = getNBits(5, 19);
                                Immediate = currentInstruction.rightShift(24);
                        }
                }
        }

        // use ands to go from binary to decimal for 5 bit binary value
        private int registerAddressDecoder(Word word) {
                var firstBit = word.getBit(0);
                var secondBit = word.getBit(1);
                var thirdBit = word.getBit(2);
                var fourthBit = word.getBit(3);
                var fifthBit = word.getBit(4);

                // eclipse/jdt.ls formatting comment
                // so it doesn' fomrat this code
                // @formatter:off
                return fifthBit.and(fourthBit).and(thirdBit).and(secondBit).and(firstBit).getValue() ? 31
                        : fifthBit.and(fourthBit).and(thirdBit).and(secondBit).getValue() ? 30
                        : fifthBit.and(fourthBit).and(thirdBit).and(firstBit).getValue() ? 29
                        : fifthBit.and(fourthBit).and(thirdBit).getValue() ? 28
                        : fifthBit.and(fourthBit).and(secondBit).and(firstBit).getValue() ? 27
                        : fifthBit.and(fourthBit).and(secondBit).getValue() ? 26
                        : fifthBit.and(fourthBit).and(firstBit).getValue() ? 25
                        : fifthBit.and(fourthBit).getValue() ? 24
                        : fifthBit.and(thirdBit).and(secondBit).and(firstBit).getValue()? 23
                        : fifthBit.and(thirdBit).and(secondBit).getValue()? 22
                        : fifthBit.and(thirdBit).and(firstBit).getValue()? 21
                        : fifthBit.and(thirdBit).getValue()? 20
                        : fifthBit.and(secondBit).and(firstBit).getValue()? 19
                        : fifthBit.and(secondBit).getValue()? 18
                        : fifthBit.and(firstBit).getValue()? 17
                        : fifthBit.getValue()? 16
                        : fourthBit.and(thirdBit).and(secondBit).and(firstBit).getValue()? 15
                        : fourthBit.and(thirdBit).and(secondBit).getValue()? 14
                        : fourthBit.and(thirdBit).and(firstBit).getValue()? 13
                        : fourthBit.and(thirdBit).getValue()? 12
                        : fourthBit.and(secondBit).and(firstBit).getValue()? 11
                        : fourthBit.and(secondBit).getValue()? 10
                        : fourthBit.and(firstBit).getValue()? 9
                        : fourthBit.getValue()? 8
                        : thirdBit.and(secondBit).and(firstBit).getValue()? 7
                        : thirdBit.and(secondBit).getValue()? 6
                        : thirdBit.and(firstBit).getValue()? 5
                        : thirdBit.getValue()? 4
                        : secondBit.and(firstBit).getValue()? 3
                        : secondBit.getValue()? 2
                        : firstBit.getValue()? 1
                        : 0;
                // @formatter:on
        }

        private Word getNBits(int size, int shift) {
                return getNBits(currentInstruction, size, shift);
        }

        private Word getRegister(Word word) {
                return getRegister(registerAddressDecoder(word));
        }

        private void setRegister(Word word, Word newWord) {
                setRegister(registerAddressDecoder(word), newWord);
        }

        public static Word getNBits(Word word, int size, int shift) {
                // creates a mask that only preseveres (size) amount of bits after shift right
                // by (shift)
                // we do this instead mutation current instruction which would be shift mask
                // shift mask shift mask
                // we dont mutate so each time we have to shift and mask relative ot start of
                // word
                Function<Integer, Bit> getMaskBit = i -> new Bit(i >= shift && i < shift + size);
                Word mask = new Word(new Bit[] { getMaskBit.apply(0), getMaskBit.apply(1), getMaskBit.apply(2),
                                getMaskBit.apply(3), getMaskBit.apply(4), getMaskBit.apply(5), getMaskBit.apply(6),
                                getMaskBit.apply(7), getMaskBit.apply(8), getMaskBit.apply(9), getMaskBit.apply(10),
                                getMaskBit.apply(11), getMaskBit.apply(12), getMaskBit.apply(13), getMaskBit.apply(14),
                                getMaskBit.apply(15), getMaskBit.apply(16), getMaskBit.apply(17), getMaskBit.apply(18),
                                getMaskBit.apply(19), getMaskBit.apply(20), getMaskBit.apply(21), getMaskBit.apply(22),
                                getMaskBit.apply(23), getMaskBit.apply(24), getMaskBit.apply(25), getMaskBit.apply(26),
                                getMaskBit.apply(27), getMaskBit.apply(28), getMaskBit.apply(29), getMaskBit.apply(30),
                                getMaskBit.apply(31), });
                return word.and(mask).rightShift(shift);
        }

        private static enum InstructionFormat {
                ZEROR,
                ONER,
                TWOR,
                THREER,
        }

        private static enum InstructionCode {
                MATH, BRANCH, CALL, PUSH, LOAD, STORE, POP
        }

        private InstructionCode getInstructionCode() {
                // this swithc statements is like a bunch of and on the first three bits but
                // using siwtch
                return switch (new Triple<>(IC.getBit(1).getValue(), IC.getBit(1).getValue(),
                                IC.getBit(1).getValue())) {
                        // requires preview feautres (java 21) or java 22
                        // fst & sbd & thrd
                        case Triple(var fst, var snd, var thrd) when fst && snd && thrd -> throw new RuntimeException();
                        case Triple(var fst, var snd, var thrd) when snd && thrd -> InstructionCode.POP;
                        case Triple(var fst, var snd, var thrd) when fst && thrd -> InstructionCode.STORE;
                        case Triple(var fst, var snd, var thrd) when snd && fst -> InstructionCode.PUSH;
                        case Triple(var fst, var snd, var thrd) when thrd -> InstructionCode.LOAD;
                        case Triple(var fst, var snd, var thrd) when snd -> InstructionCode.CALL;
                        case Triple(var fst, var snd, var thrd) when fst -> InstructionCode.BRANCH;
                        default -> InstructionCode.MATH;
                };
        }

        private InstructionFormat getInstructionFormat() {
                // same as getInstructionCode, but for 2 bits
                return switch (new Tuple<>(IF.getBit(0).getValue(), IF.getBit(1).getValue())) {
                        case Tuple(var fst, var snd) when fst && snd -> InstructionFormat.TWOR;
                        case Tuple(var fst, var snd) when fst -> InstructionFormat.ONER;
                        case Tuple(var fst, var snd) when snd -> InstructionFormat.THREER;
                        default -> InstructionFormat.ZEROR;
                };
        }

        private void execute() {

                var op = getInstructionFormat() != InstructionFormat.ZEROR ? new Bit[] {
                                Function.getBit(3).clone(),
                                Function.getBit(2).clone(),
                                Function.getBit(1).clone(),
                                Function.getBit(0).clone(),
                } : new Bit[4];
                System.out.println(getInstructionCode() + " " + getInstructionFormat());
                switch (getInstructionCode()) {
                        case CALL ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case LOAD ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case MATH -> {

                                switch (getInstructionFormat()) {
                                        case ZEROR -> halted.set(true);
                                        case ONER -> {
                                                // do we sign extend immidiate (no)
                                                setRegister(Rd, Immediate);
                                        }
                                        case THREER -> {
                                                alu.setOp1(getRegister(Rs1));
                                                alu.setOp2(getRegister(Rs2));
                                                alu.doOperation(op);
                                        }
                                        case TWOR -> {
                                                // put rs1 in op1 and op2
                                                alu.setOp1(getRegister(Rd));
                                                alu.setOp2(getRegister(Rs1));
                                                alu.doOperation(op);

                                        }
                                }
                        }
                        case POP ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case PUSH ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case STORE ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case BRANCH ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                }
        }

        private void store() {
                switch (getInstructionCode()) {
                        case MATH -> {
                                switch (getInstructionFormat()) {
                                        case THREER, TWOR -> setRegister(Rd, alu.getResult());
                                        default -> {
                                        }
                                }
                        }
                        case BRANCH ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case CALL ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case LOAD ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case POP ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case PUSH ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                        case STORE ->
                                throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
                }

        }

        private void fetch() {
                currentInstruction = MainMemory.read(PC);
                PC.increment();
        }

        public void run() {
                while (halted.not().getValue()) {
                        fetch();
                        decode();
                        execute();
                        store();
                }
        }
}
