import static org.junit.Assert.*;

import org.junit.Test;

public class UnitTests {


    static final Bit False = new Bit(false);
    static final Bit True = new Bit(true);

    static final Bit[] TRUE_BITS = new Bit[] {
            True, True, True, True, True, True, True, True, True, True, True, True, True, True, True, True, True, True,
            True, True, True, True, True, True, True, True, True, True, True, True, True, True,
    };

    static final Bit[] FALSE_BITS = new Bit[] {
            False, False, False, False, False, False, False, False, False, False, False, False, False, False, False, False, False, False,
            False, False, False, False, False, False, False, False, False, False, False, False, False, False,
    };

    static final Word ZERO = new Word(FALSE_BITS);
    static final Word BIG_NUMBER = new Word(TRUE_BITS);

    // Word Tests
    @Test
    public void BasicWordNot() {
        assertEquals(ZERO, BIG_NUMBER.not());
    }

    @Test
    public void BasicWordAnd() {
        assertEquals(ZERO, BIG_NUMBER.and(ZERO));
    }

    @Test
    public void BasicWordOr() {
        assertEquals(BIG_NUMBER, ZERO.or(BIG_NUMBER));
    }

    @Test
    public void BasicWordXor() {
        assertEquals(ZERO, BIG_NUMBER.xor(BIG_NUMBER));
    }

    @Test 
    public void BasicWordSet() {
        var big_number = new Word(TRUE_BITS);
        big_number.set(6);
        System.out.println(big_number);
        assertEquals(-6, big_number.getSigned());
        assertEquals(ZERO, big_number);
    }
}
