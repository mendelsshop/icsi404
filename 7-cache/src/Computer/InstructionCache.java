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
	private static Word startAddress = new Word(1024);
	private static Word[] cached = new Word[] {
			new Word(0), new Word(0), new Word(0), new Word(0), new Word(0), new Word(0), new Word(0), new Word(0),
	};
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
	public static Word read(Word address) {
		var addressDiffernce = ALU.sub(address, startAddress);
		return Optional
				// checking if the address differnece which tells us how close the address
				// requested is to our cache
				// its greater than or equal to 8 we have cache miss (we do this by or(ing) all
				// the bits starting after 3)
				.ofNullable(Stream.iterate(3, i -> i < 32, i -> i + 1).map(addressDiffernce::getBit).reduce(Bit::or)
						.get()
						// checking if address is valid ie < 1024
						// we do this by or ing all the bits after 10
						.or(Stream.iterate(10, i -> i < 32, i -> i + 1).map(address::getBit).reduce(Bit::or).get())
						// if any of the bits above are true we have a cache miss so we turn that into
						// oprional empty
						// otherwise we decode the 3 first bits to get a decimal value from 0..7
						.not().getValue() ? threeBitDecoder(addressDiffernce) : null)

				.map(i -> {
					clockCycle = 10;
					// then we read from the cache
					return cached[i];
				}).orElseGet(() -> {
					// if cache miss read from l2
					// and update start address
					startAddress.copy(LevelTwoCache.readBlock(address, cached));
					clockCycle = 50 + LevelTwoCache.getClockCycle();
					// we recompute address difference in case where at the bounds of memory like
					// 1020
					// usually after reading from memory our requested address will be the first in
					// the cache but if we read from the end we cant go past 1024 so we wrap cache
					// start address to 1015
					// so request address is not first
					var newAddressDiffernce = ALU.sub(address, startAddress);
					return cached[threeBitDecoder(newAddressDiffernce)];
				});
	}

	private static int threeBitDecoder(Word word) {
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
