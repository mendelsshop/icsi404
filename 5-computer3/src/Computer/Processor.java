package Computer;

import static Utils.Utils.*;

import java.util.function.Function;

public class Processor {
    private Word PC = new Word(new Bit[32]);
    private Word SP = new Word(new Bit[32]);
    private Word currentInstruction = new Word(new Bit[32]);
    private Bit halted = new Bit(false);

    private Word[] registers = new Word[] {
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
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
        IF = getNBits(2, 0);
        IC = getNBits(3, 2);
        switch (getInstructionFormat()) {
            case ZEROR -> {
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
            default -> throw new IllegalArgumentException("Unexpected value: " + getInstructionFormat());
        }
    }

    private int nBitUnsignedDecoder(int n, Word word) {
        checkBitRange1(n);
        int res = 0;
        for (int i = 0; i < n; i++) {
            res += word.getBit(i).getValue() ? (int) Math.pow(2, i) : 0;
        }
        return res;
    }

    private Word getNBits(int size, int shift) {
        return getNBits(currentInstruction, size, shift);
    }

    private Word getRegister(Word word) {
        return getRegister(nBitUnsignedDecoder(5, word));
    }

    private void setRegister(Word word, Word newWord) {
        setRegister(nBitUnsignedDecoder(5, word), newWord);
    }

    public static Word getNBits(Word word, int size, int shift) {
        Function<Integer, Bit> getMaskBit = i -> new Bit(i >= shift && i < shift + size);
        Word mask = new Word(new Bit[] {
                getMaskBit.apply(0), getMaskBit.apply(1), getMaskBit.apply(2), getMaskBit.apply(3), getMaskBit.apply(4),
                getMaskBit.apply(5), getMaskBit.apply(6), getMaskBit.apply(7), getMaskBit.apply(8), getMaskBit.apply(9),
                getMaskBit.apply(10), getMaskBit.apply(11), getMaskBit.apply(12), getMaskBit.apply(13),
                getMaskBit.apply(14), getMaskBit.apply(15), getMaskBit.apply(16), getMaskBit.apply(17),
                getMaskBit.apply(18), getMaskBit.apply(19), getMaskBit.apply(20), getMaskBit.apply(21),
                getMaskBit.apply(22), getMaskBit.apply(23), getMaskBit.apply(24), getMaskBit.apply(25),
                getMaskBit.apply(26), getMaskBit.apply(27), getMaskBit.apply(28), getMaskBit.apply(29),
                getMaskBit.apply(30), getMaskBit.apply(31),
        });
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
        return switch (new Triple<>(IC.getBit(1).getValue(), IC.getBit(1).getValue(), IC.getBit(1).getValue())) {
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
        return switch (new Tuple<>(IF.getBit(0).getValue(), IF.getBit(1).getValue())) {
            case Tuple(var fst, var snd) when fst && snd -> InstructionFormat.TWOR;
            case Tuple(var fst, var snd) when fst -> InstructionFormat.ONER;
            case Tuple(var fst, var snd) when snd -> InstructionFormat.THREER;
            case Tuple(var fst, var snd) -> InstructionFormat.ZEROR;
            default -> throw new RuntimeException();
        };
    }

    // TODO: do we always use store to do the store part (ie for load,store)?

    // result of execute - could the address of to store in memory or what to store
    // depending on instructins
    private Word result;

    private void execute() {

        var op = getInstructionFormat() != InstructionFormat.ZEROR ? new Bit[] {
                Function.getBit(3).clone(),
                Function.getBit(2).clone(),
                Function.getBit(1).clone(),
                Function.getBit(0).clone(),
        } : new Bit[4];
        System.out.println(getInstructionCode() + " " + getInstructionFormat());
        switch (getInstructionCode()) {
            case CALL -> {
                switch (getInstructionFormat()) {
                    case ONER ->
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                    case THREER ->
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                    case TWOR ->
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                    case ZEROR ->
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                }
            }
            case LOAD -> {
                switch (getInstructionFormat()) {
                    case TWOR -> result = ALU.add(Rs1, Immediate);
                    case THREER -> result = ALU.add(Rs1, Rs2);
                    case ONER -> result = ALU.add(Rd, Immediate);
                    case ZEROR -> {
                        // TODO: when we say Return is pop and set the PC
                        // or any instruction does some sub indtruction do we just do that basic version
                        // of that instruction ie --p
                    }
                }
            }
            case MATH -> {
                switch (getInstructionFormat()) {
                    // for zeror and oner these are "trvial things" and we don't differtiate execute
                    // and store
                    case ZEROR -> halted.set(true);
                    case ONER -> {
                        // do we sign extend immidiate (no)
                        setRegister(Rd, Immediate);
                    }
                    case THREER -> {
                        alu.setOp1(getRegister(Rs1));
                        alu.setOp2(getRegister(Rs2));
                        alu.doOperation(op);
                        result = alu.getResult();
                    }
                    case TWOR -> {
                        // put rs1 in op1 and op2
                        alu.setOp1(getRegister(Rd));
                        alu.setOp2(getRegister(Rs1));
                        alu.doOperation(op);
                        result = alu.getResult();
                    }
                }
            }
            case POP -> {
                switch (getInstructionFormat()) {
                    // PEEK (does not modify sp)
                    case TWOR -> {
                        var sub = ALU.add(Rs1, Immediate);
                        result = MainMemory.read(ALU.add(SP, sub));
                    }
                    // PEEK (does not modify sp)
                    case THREER -> {
                        var sub = ALU.add(Rs1, Rs2);
                        result = MainMemory.read(ALU.add(SP, sub));
                    }
                    // POP (modifies SP)
                    case ONER -> {
                        // i think its post increment so first we read and then we update
                        result = MainMemory.read(SP);
                        SP.increment();
                    }
                    // interupt
                    case ZEROR ->
                        // TODO: interupts
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                }
            }
            case PUSH -> {
                switch (getInstructionFormat()) {
                    case TWOR -> {
                        alu.setOp1(Rd);
                        alu.setOp2(Rs1);
                        alu.doOperation(op);
                        result = alu.getResult();
                        SP.decrement();
                    }
                    case THREER -> {
                        alu.setOp1(Rs1);
                        alu.setOp2(Rs2);
                        alu.doOperation(op);
                        result = alu.getResult();
                        SP.decrement();
                    }
                    case ONER -> {
                        alu.setOp1(Rd);
                        alu.setOp2(Immediate);
                        alu.doOperation(op);
                        result = alu.getResult();
                        SP.decrement();
                    }
                    case ZEROR -> {
                        // TODO: what happens if unused instructions are used
                    }
                }
            }
            case STORE -> {
                switch (getInstructionFormat()) {
                    // oner is trivial so extecute + store is combined into execute
                    // TODO: when adding should we use ALU.add2 or call dispatch on alu (or maybe
                    // move add2/add4 to word)
                    // maybe do addition here and rest in store
                    case TWOR -> result = ALU.add(Rd, Immediate);
                    case THREER -> result = ALU.add(Rd, Rs1);
                    case ONER -> MainMemory.write(Rd, Immediate);
                    case ZEROR -> {
                        // TODO: what happens if unused instructions are used
                    }
                }
            }
            case BRANCH -> {
                // zeror is trivial so we could combine execute and store int execute
                // but we don't because it means we don't have to switch on instruction format
                // in store if the all do the same thing
                switch (getInstructionFormat()) {
                    case ONER -> result = ALU.add(PC, Immediate);
                    case THREER -> {
                        alu.setOp1(Rs1);
                        alu.setOp2(Rs2);
                        result = (alu.doBooleanOperation(op)) ? ALU.add(PC, Immediate) : PC;
                    }
                    case TWOR -> {
                        alu.setOp1(Rs1);
                        alu.setOp2(Rd);
                        result = (alu.doBooleanOperation(op)) ? ALU.add(PC, Immediate) : PC;
                    }
                    case ZEROR -> result = Immediate;
                }
            }
        }
    }

    private void store() {
        switch (getInstructionCode()) {
            case MATH -> {
                switch (getInstructionFormat()) {
                    case THREER, TWOR -> setRegister(Rd, result);
                    default -> {
                    }
                }
            }
            case BRANCH -> PC.copy(result);
            case CALL -> throw new UnsupportedOperationException("Unimplemented case: " + getInstructionCode());
            case LOAD -> {
                switch (getInstructionFormat()) {
                    case THREER, TWOR, ONER -> setRegister(Rd, result);
                    // return modifies pc
                    case ZEROR ->
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                }
            }
            case POP -> {
                switch (getInstructionFormat()) {
                    case THREER, TWOR, ONER -> setRegister(Rd, result);
                    // interupt
                    case ZEROR ->
                        throw new UnsupportedOperationException("Unimplemented case: " + getInstructionFormat());
                }
            }
            case PUSH -> {
                switch (getInstructionFormat()) {
                    // we already decremented sp in execute
                    case THREER, TWOR, ONER -> MainMemory.write(SP, result);
                    default -> {
                    }
                }
            }
            case STORE -> {
                switch (getInstructionFormat()) {
                    case TWOR -> MainMemory.write(result, Rs1);
                    case THREER -> MainMemory.write(result, Rs2);
                    default -> {
                    }
                }
            }
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
