package UnitTests;

import java.util.function.IntConsumer;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static UnitTests.UnitTests.*;

import Computer.*;

/**
 * ALUTests
 */
public class ALUTests {
    @Test
    public void add_a_lot() {
        IntConsumer muller = i -> {
            var n1 = new Word(i);
            doInRange(0, 50, j -> {
                var n2 = new Word(j);
                var added = ALU.mul(n1, n2);
                assertEquals(i * j, added.getUnsigned());
            }, "mul inner");
        };
        doInRange(0, 100, muller, "mul outer");
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
