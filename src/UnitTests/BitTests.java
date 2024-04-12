package UnitTests;

import static Utils.Utils.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * BitTests
 */
public class BitTests {
    @Test
    // makes sure the not truth table holds true
    // x |!x
    // +--+--+
    // | 1| 0|
    // +--+--+
    // | 0| 1|
    public void not_bits() {
        var not_false = getFalse().not();
        var not_true = getTrue().not();

        assertEquals(getTrue(), not_false);
        assertEquals(getFalse(), not_true);
    }

    @Test
    // makes sure the or truth table holds true
    // x | y| x | y
    // +--+--+------+
    // | 0| 1| 1 |
    // +--+--+------+
    // | 0| 0| 0 |
    // +--+--+------+
    // | 1| 1| 1 |
    // +--+--+------+
    // | 1| 0| 1 |
    // +--+--+------+
    public void or_bits() {
        var false_or_true = getFalse().or(getTrue());
        var false_or_false = getFalse().or(getFalse());
        var true_or_true = getTrue().or(getTrue());
        var true_or_false = getTrue().or(getFalse());
        assertEquals(getTrue(), false_or_true);
        assertEquals(getFalse(), false_or_false);
        assertEquals(getTrue(), true_or_true);
        assertEquals(getTrue(), true_or_false);
    }

    @Test
    // makes sure the and truth table holds true
    // x | y| x & y
    // +--+--+------+
    // | 0| 1| 0 |
    // +--+--+------+
    // | 0| 0| 0 |
    // +--+--+------+
    // | 1| 1| 1 |
    // +--+--+------+
    // | 1| 0| 0 |
    // +--+--+------+
    public void and_bits() {
        var false_and_true = getFalse().and(getTrue());
        var false_and_false = getFalse().and(getFalse());
        var true_and_true = getTrue().and(getTrue());
        var true_and_false = getTrue().and(getFalse());
        assertEquals(getFalse(), false_and_true);
        assertEquals(getFalse(), false_and_false);
        assertEquals(getTrue(), true_and_true);
        assertEquals(getFalse(), true_and_false);
    }

    @Test
    // makes sure the or truth table holds true
    // x | y| x + y
    // +--+--+------+
    // | 0| 1| 1 |
    // +--+--+------+
    // | 0| 0| 0 |
    // +--+--+------+
    // | 1| 1| 0 |
    // +--+--+------+
    // | 1| 0| 1 |
    // +--+--+------+
    public void xor_bits() {
        var false_xor_true = getFalse().xor(getTrue());
        var false_xor_false = getFalse().xor(getFalse());
        var true_xor_true = getTrue().xor(getTrue());
        var true_xor_false = getTrue().xor(getFalse());
        assertEquals(getTrue(), false_xor_true);
        assertEquals(getFalse(), false_xor_false);
        assertEquals(getFalse(), true_xor_true);
        assertEquals(getTrue(), true_xor_false);
    }

}
