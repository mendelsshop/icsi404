package UnitTests;

import static Utils.Utils.getBigNumber;
import static Utils.Utils.getZero;
import static org.junit.Assert.assertEquals;

import java.util.Random;

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
	public void basicLoadReadTest1() {
		MainMemory.load(new String[] { "01010101010101010101010101010101", "00000000000000000000000000001111" });
		assertEquals(15, MainMemory.read(new Word(1)).getSigned());
	}

	@Test
	public void LoadTest() {
		MainMemory.load(new String[] { "00000000000000000000000000000001", "00000000000000000000000000000010",
				"00000000000000000000000000000011", "00000000000000000000000000000100",
				"00000000000000000000000000000101", "00000000000000000000000000000110",
				"00000000000000000000000000000111", "00000000000000000000000000001000",
				"00000000000000000000000000001001", "00000000000000000000000000001010" });
		for (int i = 0; i < 10; i++) {
			assertEquals(i + 1, MainMemory.read(new Word(i)).getSigned());
		}

	}

	@Test
	public void ReadWriteTest100() {
		readRng(100);
	}

	@Test
	public void ReadWriteTest1000() {
		readRng(1024);
	}

	private void readRng(int n) {
		var rng = new Random();
		for (int i = 0; i < n; i++) {
			MainMemory.write(new Word(i), new Word(i));
		}
		for (int i = 0; i < n; i++) {
			if (rng.nextBoolean()) {
				assertEquals(i, MainMemory.read(new Word(i)).getSigned());
			}
		}
	}
}
