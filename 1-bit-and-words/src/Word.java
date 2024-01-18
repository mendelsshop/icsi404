import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Word {
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

	public Word not() {

		return map(Bit::not);
	}

	// TODO: check where not over shifting
	public Word rightShift(int amount) {
		var zerod = Stream.generate(() -> new Bit(false)).limit(amount);
		var shifted = Arrays.stream(bits).limit(32 - amount);
		return new Word(Stream.concat(zerod, shifted)
				.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	// TODO: check where not over shifting
	public Word leftShift(int amount) {
		var zerod = Stream.generate(() -> new Bit(false)).limit(amount);
		var shifted = Arrays.stream(bits).skip(amount);
		return new Word(Stream.concat(shifted, zerod)
				.collect(Collectors.toList()).toArray(new Bit[32]));
	}

	@Override
	public String toString() {
		return Arrays.stream(bits).map(Bit::toString).collect(Collectors.joining(","));
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

	public int getSigned() {

		return (bits[0].getValue() ? -1 : 1)
				* IntStream.range(1, 32).map((i) -> (bits[i].getValue() ? (int) Math.pow(2, i) : 0)).sum();

	}

	public long getUnsigned() {
		return IntStream.range(0, 32).mapToLong((i) -> (bits[i].getValue() ? (long) Math.pow(2, i) : 0)).sum();
	}

	public void set(int i) {
		// TODO: unsigned or signed
		int index = 0;
		// we extract the bytes using %2 and /2 until we run out of bits
		while (i > 0) {
			int bit = i % 2;
			System.out.format("%d %d: %d\n", index, (int) Math.pow(2, index), bit);
			bits[index] = new Bit(bit == 1);
			i /= 2;
			index++;
		}
		// clear rest of bits
		for (; index < 32; index++) {
			bits[index] = new Bit(false);
		}
	}

}
