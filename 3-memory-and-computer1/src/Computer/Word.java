package Computer;

import static Utils.Utils.*;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Word {
	// we often think of binary as rtl:
	// 128 | 64 | 32 | 16 | 8 | 4 | 2 | 1
	// and arrays as being ltr
	// 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7
	// in here the bit for the one place will be index 0
	// 2s place will be index 1
	// .. last bit the 4294967296s placw will be index 31
	// this can sometimes be a bit (no pun intended) counterintuitive for bit
	// shifiting and to/from decimal conversions

	// another note:
	// most methods here have duplicates meaning often named like name2
	// they are a more perfromant, but less elegant soloution to same problem
	private Bit[] bits;

	public Word(Bit[] startBits) {
		if (startBits.length != 32) {
			throw new IndexOutOfBoundsException();
		}
		bits = startBits;
	}

	public Word(int i) {
		bits = new Bit[32];
		set(i);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bits);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (!Arrays.equals(bits, other.bits))
			return false;
		return true;
	}

	public Bit getBit(int i) {
		checkBitRange0(i);
		return new Bit(bits[i].getValue());
	}

	public void setBit(int i, Bit bit) {
		checkBitRange0(i);
		// TODO: time for clone?
		bits[i] = new Bit(bit.getValue());
	}

	public Word and(Word other) {
		return map(Bit::and, other);
	}

	public Word or(Word other) {

		return map(Bit::or, other);
	}

	public Word xor(Word other) {

		return map(Bit::xor, other);
	}

	public Word and2(Word other) {
		return map2(Bit::and, other);
	}

	public Word or2(Word other) {

		return map2(Bit::or, other);
	}

	public Word xor2(Word other) {

		return map2(Bit::xor, other);
	}

	public Word not() {

		return map(Bit::not);
	}

	public Word not2() {

		return map2(Bit::not);
	}

	public Word leftShift(int amount) {
		checkBitRange0(amount);
		var zerod = Stream.generate(() -> new Bit(false)).limit(amount);
		var shifted = Arrays.stream(bits).limit(32 - amount).map(b -> new Bit(b.getValue()));
		return new Word(Stream.concat(zerod, shifted)
				.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	public Word leftShift2(int amount) {
		checkBitRange0(amount);
		var res = new Bit[32];
		var i = 0;
		for (; i < amount; i++) {
			res[i] = new Bit(false);
		}
		for (; i < 32; i++) {
			res[i] = new Bit(bits[i - amount].getValue());
		}
		return new Word(res);
	}

	public Word rightShift2(int amount) {
		checkBitRange0(amount);
		var res = new Bit[32];
		var i = 0;
		for (; i < 32 - amount; i++) {
			res[i] = new Bit(bits[i + amount].getValue());
		}
		for (; i < 32; i++) {
			res[i] = new Bit(false);
		}
		return new Word(res);
	}

	public Word rightShift(int amount) {
		checkBitRange0(amount);
		var zerod = Stream.generate(() -> new Bit(false)).limit(amount);
		var shifted = Arrays.stream(bits).skip(amount).map(b -> new Bit(b.getValue()));
		return new Word(Stream.concat(shifted, zerod)
				.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	@Override
	// like my comment about rtl vs ltr above we output the strings as being ltr
	public String toString() {
		return Arrays.stream(bits).map(Bit::toString).collect(Collectors.joining(","));
	}

	public int getSigned() {
		return (bits[31].getValue() ? -2147483648 : 0)
				+ IntStream.range(0, 31).map((i) -> (bits[i].getValue() ? (int) Math.pow(2, i) : 0))
						.sum();
	}

	public Word negate() {
		var res = not();
		res.increment();
		return res;
	}

	public Word add1() {
		var res = clone();
		res.increment();
		return res;
	}

	public void increment() {
		Stream.iterate(0, i -> i < 32, i -> i + 1).reduce((new Bit(true)), (t, i) -> {
			// order matters if you get carry after setting bit you are doing it wronmg (the
			// joys of mutation)
			var carry = t.and(getBit(i));
			setBit(i, getBit(i).xor(t));
			return (carry);
		}, (x, y) -> y);
	}

	public void increment2() {
		var carry = new Bit(true);
		for (int i = 0; i < 32; i++) {
			// order matters if you get carry after setting bit you are doing it wronmg (the
			// joys of mutation)
			Bit bit = getBit(i).xor(carry);
			carry = getBit(i).and(carry);
			bits[i] = bit;
		}
	}

	public int getSigned2() {
		var res = bits[31].getValue() ? -2147483648 : 0;
		for (int i = 0; i < 31; i++) {
			res += bits[i].getValue() ? (int) Math.pow(2, i) : 0;
		}
		return res;
	}

	public long getUnsigned2() {
		long res = 0;
		for (int i = 0; i < 32; i++) {
			res += bits[i].getValue() ? (long) Math.pow(2, i) : 0;
		}
		return res;
	}

	public long getUnsigned() {
		return IntStream.range(0, 32).mapToLong((i) -> (bits[i].getValue() ? (long) Math.pow(2, i) : 0)).sum();
	}

	public void set(int i) {
		// we do the +1 for negative number before converting binary
		var i_abs = i < 0 ? -1 * (1 + i) : i;
		int index = 0;
		// we extract the bytes using %2 and /2 until we run out of bits
		while (i_abs > 0) {
			int bit = (int) (i_abs % 2);
			bits[index] = new Bit(bit == 1);
			i_abs /= 2;
			index++;
		}
		// clear rest of bits
		for (; index < 32; index++) {
			bits[index] = new Bit(false);
		}
		if (i < 0) {
			bits = not().bits;
		}
	}

	public void set2(int i) {
		// we do the +1 for negative number before converting binary
		var i_abs = i < 0 ? -1 * (1 + i) : i;
		int index = 0;
		// we extract the bytes using %2 and /2 until we run out of bits
		while (i_abs > 0) {
			int bit = (int) (i_abs % 2);
			bits[index] = new Bit(bit == 1);
			i_abs /= 2;
			index++;
		}
		// clear rest of bits
		for (; index < 32; index++) {
			bits[index] = new Bit(false);
		}
		if (i < 0) {
			bits = not2().bits;
		}
	}

	private Word map(Function<Bit, Bit> mapper) {
		return new Word(
				Arrays.stream(bits).map(b -> mapper.apply(b))
						.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	private Word map(BiFunction<Bit, Bit, Bit> mapper, Word other) {
		return new Word(
				Stream.iterate(0, i -> i < 32, i -> i + 1)
						.map(i -> mapper.apply(bits[i], other.bits[i]))
						.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	private Word map2(BiFunction<Bit, Bit, Bit> mapper, Word other) {
		var res = new Bit[32];
		for (var i = 0; i < 32; i++) {
			res[i] = mapper.apply(bits[i], other.bits[i]);
		}
		return new Word(res);
	}

	private Word map2(Function<Bit, Bit> mapper) {
		var res = new Bit[32];
		for (var i = 0; i < 32; i++) {
			res[i] = mapper.apply(bits[i]);
		}
		return new Word(res);
	}

	public void copy(Word other) {
		bits = (Bit[]) Arrays.stream(other.bits)
		.map(Bit::clone)
		.toArray(Bit[]::new);
	}

	public void copy2(Word other) {
		for (int i = 0; i < 32; i++) {
			bits[i] = new Bit(other.bits[i].getValue());
		}
	}

	@Override
	public Word clone() {
		return new Word(bits.clone());
	}
}
