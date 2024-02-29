package UnitTests;

import static Utils.Utils.getBigNumber;
import static Utils.Utils.getZero;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Computer.MainMemory;
import Computer.Word;

public class MemoryTests {
    @Test
    public void basicReadWriteTest() {
        MainMemory.write(getZero(), getBigNumber());
        assertEquals(-1, MainMemory.read(getZero()).getSigned());
    }

    @Test
    public void basicLoadReadTest() {
        MainMemory.load(new String[] { "11111111111111111111111111111111" });
        assertEquals(-1, MainMemory.read(getZero()).getSigned());
    }

    @Test
    public void basixLoadReadTest1() {
        MainMemory.load(new String[] { 
            "01010101010101010101010101010101", 
            "00000000000000000000000000001111" });
        assertEquals(15, MainMemory.read(new Word(1)).getSigned());
    }
}
