package Computer;

import java.util.Optional;
import java.util.Random;
import static Utils.Utils.TriFunction;
import java.util.stream.Stream;

import Computer.MainMemory.MemoryReadError;

/**
 */
public class LevelTwoCache {
	
	private static int clockCycle;

	public static int getClockCycle() {
		return clockCycle;
	}

	private static class CacheUnit {

		private Word startAddress = new Word(1024);
		private Word[] cached = new Word[] {
				new Word(0), new Word(0), new Word(0), new Word(0), new Word(0), new Word(0),new Word(0), new Word(0),
		};

		// since l2 cache can be read we make genric version that can do whatever the accesor parameter want
		public <T> Optional<T> access(Word address, TriFunction<Word, Integer, Word[], T> accesor) {
			var addressDiffernce = ALU.sub(address, startAddress);
			var firstBit = addressDiffernce.getBit(0);
			var secondBit = addressDiffernce.getBit(1);
			var thirdBit = addressDiffernce.getBit(2);
			
			// we check if cache hit (address difference is < 8, by oring all the bits that are >= 8) 
			return Optional.ofNullable(Stream.iterate(3, i -> i < 32, i -> i + 1).map(addressDiffernce::getBit).reduce(
			// @formatter:off
					// then we decode the bits to get decimal index into cache
					Bit::or).get().not().getValue() ? (
						thirdBit.and(secondBit).and(firstBit).getValue() ? 7
						: thirdBit.and(secondBit).getValue() ? 6
						: thirdBit.and(firstBit).getValue() ? 5
						: thirdBit.getValue() ? 4
						: secondBit.and(firstBit).getValue() ? 3
						: secondBit.getValue() ? 2 : firstBit.getValue() ? 1 : 0)
					: null)
					// @formatter:on
					// we then do the action requested by the caller
					// only if we hit
					.map(i -> accesor.apply(startAddress, i, cached));
		}

		public void update(Word address) {
			// get new cache from memory
			startAddress.copy(MainMemory.copyBlock(address, cached));
		}

		public void save() {
			// save current cache to memory
			if (!startAddress.equals(new Word(1024)))
				MainMemory.saveBlock(startAddress, cached);
		}
	}

	private static CacheUnit[] cache = new CacheUnit[] { new CacheUnit(), new CacheUnit(), new CacheUnit(),
			new CacheUnit(), };
	private static Random evicter = new Random();

	// we start address at 1024 because there are only 1024 memory addresses
	// so any address over 1024 invalid
	// what happens if we overwrite an instruction with a store, so the cache has
	// not been updated but the in main memory is updated

	// start = 1024
	// lookup 1014
	// 1014 - 1024 = -10
	// miss
	// start = 1014
	// lookup 1015
	// 1015 - 1014 = 1
	// hit
	// lookup 1023
	// 1023 - 1014 = 9
	// miss
	// start = 1015
	// lookup 1025
	// invalid read
	private static <T> T access(Word address, TriFunction<Word, Integer, Word[], T> accesor) {
		// make sure were accesing valid memory
		// or all bits >= 204
		if (Stream.iterate(10, i -> i < 32, i -> i + 1).map(address::getBit).reduce(Bit::or).get().getValue()) {
			throw new MemoryReadError((int) address.getUnsigned());
		}
		// go through all the cache untis trying to find a cache hit
		return Stream.iterate(0, i -> i < cache.length, i -> i + 1).map(i -> cache[i].access(address, accesor))
				.reduce((a, b) -> a.or(() -> b)).get().map(x -> {
					clockCycle = 20;
					return x;
				}).orElseGet(() -> {
					// if no cache hits evict random piece of cache o memory
					var evicted = cache[evicter.nextInt(cache.length)];
					// cache miss 700 cycles -- because save to memory and then load new
					clockCycle = 700;
					evicted.save();
					evicted.update(address);
					// finally dispatch to mini cache using accesor to and get (because this should not miss)
					return evicted.access(address, accesor).get();
				});
	}

	public static Word read(Word address) {
		return access(address, (start, i, cache) -> cache[i]);
	}

	public static Word readBlock(Word address, Word[] instructionCache) {
		return access(address, (start, i, cache) -> {
			for (int j = 0; j < cache.length; j++) {
				instructionCache[j] = cache[j].clone();
			}
			return start;
		});
	}

	public static void write(Word address, Word value) {
		access(address, (start, i, cache) -> {
			cache[i].copy(value);
			// we have to return something due to the sinature of acces
			return 0;
		});
	}

}
