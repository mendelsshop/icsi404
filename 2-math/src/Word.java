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
	private Bit[] bits;

	// TODO: check array is right size
	public Word(Bit[] startBits) {
		bits = startBits;
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

	// TODO: check out of bounds?
	public Bit getBit(int i) {
		return new Bit(bits[i].getValue());
	}

	// TODO: check out of bounds?
	public void setBit(int i, Bit bit) {
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

	// TODO: check where not over shifting/or adjust shift so its with shifting
	// range ie > 32 = 32
	public Word leftShift(int amount) {
		var zerod = Stream.generate(() -> new Bit(false)).limit(amount);
		var shifted = Arrays.stream(bits).limit(32 - amount).map(b -> new Bit(b.getValue()));
		return new Word(Stream.concat(zerod, shifted)
				.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	public Word leftShift2(int amount) {
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

	// TODO: check where not over shifting
	public Word rightShift(int amount) {
		var zerod = Stream.generate(() -> new Bit(false)).limit(amount);
		var shifted = Arrays.stream(bits).skip(amount).map(b -> new Bit(b.getValue()));
		return new Word(Stream.concat(shifted, zerod)
				.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	@Override
	public String toString() {
		return Arrays.stream(bits).map(Bit::toString).collect(Collectors.joining(","));
	}

	public int getSigned() {
		return (bits[31].getValue() ? -2147483648 : 0)
				+ IntStream.range(0, 31).map((i) -> (bits[i].getValue() ? (int) Math.pow(2, i) : 0))
						.sum();
	}

	public int getSigned2() {
		var res = bits[31].getValue() ? -2147483648 : 0;
		for (int i = 0; i < 31; i++) {
			res += bits[i].getValue() ? (int) Math.pow(2, i) : 0;
		}
		return res;
	}

	public int getUnsigned2() {
		var res = 0;
		for (int i = 0; i < 32; i++) {
			res += bits[i].getValue() ? (int) Math.pow(2, i) : 0;
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
				Stream.iterate(0, i -> i < 32, i -> i + 1).map(i -> mapper.apply(bits[i], other.bits[i]))
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
		bits = (Bit[]) Arrays.stream(other.bits).map(b -> new Bit(b.getValue())).toArray();
	}

	public void copy2(Word other) {
		for (int i = 0; i < 32; i++) {
			bits[i] = new Bit(other.bits[i].getValue());
		}
	}
}
