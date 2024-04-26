package Computer;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * InstructionCache
 */
public class InstructionCache {
	// TODO: caches static or not, also are we supposed to test with and without l2
	// cache
	// we start address at 1024 because there are only 1024 memory addresses
	// so any address over 1024 invalid
	private Word startAddress = new Word(1024);
	private Word[] cached = new Word[8];
	private static int clockCycle = 0;

	// TODO: if we only use this to cache instructions
	// what happens if we overwrite an instruction with a store, so the cache has
	// not been updated but the in main memory is updated

	// TODO: what happens if we read near the end

	public static int getClockCycle() {
		return clockCycle;
	}

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
	public Word Read(Word address) {
		var addressDiffernce = ALU.sub(address, startAddress);
		return Optional
				.ofNullable(Stream.iterate(3, i -> i < 32, i -> i + 1).map(addressDiffernce::getBit).reduce(Bit::or)
						.get()
						.or(Stream.iterate(10, i -> i < 32, i -> i + 1).map(address::getBit).reduce(Bit::or).get())
						.not().getValue() ? threeBitDecoder(addressDiffernce) : null)

				.map(i -> {
					clockCycle = 10;
					return cached[i];
				}).orElseGet(() -> {
					// if cache miss read from l2 cache
					startAddress = LevelTwoCache.readBlock(address, cached);
					clockCycle = 50 + LevelTwoCache.getClockCycle();
					var newAddressDiffernce = ALU.sub(address, startAddress);
					return cached[threeBitDecoder(newAddressDiffernce)];
				});
	}

	private int threeBitDecoder(Word word) {
		var firstBit = word.getBit(0);
		var secondBit = word.getBit(1);
		var thirdBit = word.getBit(2);
		// @formatter:off
		return thirdBit.and(secondBit).and(firstBit).getValue() ? 7
			: thirdBit.and(secondBit).getValue() ? 6
			: thirdBit.and(firstBit).getValue() ? 5
			: thirdBit.getValue() ? 4
			: secondBit.and(firstBit).getValue() ? 3
			: secondBit.getValue() ? 2 
			: firstBit.getValue() ? 1 
			: 0;
			// @formatter:on
	}

}
