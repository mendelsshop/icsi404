
package Computer;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import Computer.MainMemory.MemoryReadError;

/**
 * InstructionCache
 */
// TODO: explain better remove old comments from InsturctionCache
public class LevelTwoCache {
    private class CacheUnit {
        private Word startAddress = new Word(1024);
        private Word[] cached = new Word[32];

        public <T> Optional<T> access(Word address, Function<Word, T> accesor) {
            var addressDiffernce = ALU.sub(address, startAddress);
            var firstBit = addressDiffernce.getBit(0);
            var secondBit = addressDiffernce.getBit(1);
            var thirdBit = addressDiffernce.getBit(2);
            return Optional.ofNullable(
                    Stream.iterate(3, i -> i < 32, i -> i + 1).map(addressDiffernce::getBit).reduce(Bit::or)
                            .get()
                            .not()
                            .getValue()
                                    ? thirdBit.and(secondBit).and(firstBit).getValue() ? 7
                                            : thirdBit.and(secondBit).getValue() ? 6
                                                    : thirdBit.and(firstBit)
                                                            .getValue() ? 5
                                                                    : thirdBit.getValue()
                                                                            ? 4
                                                                            : secondBit.and(firstBit)
                                                                                    .getValue() ? 3
                                                                                            : secondBit.getValue()
                                                                                                    ? 2
                                                                                                    : firstBit
                                                                                                            .getValue()
                                                                                                                    ? 1
                                                                                                                    : 0
                                    : null)
                    .map(
                            i -> accesor.apply(cached[i]));
        }

        public void update(Word address) {
            startAddress = MainMemory.copyBlock(address, cached);
        }

        public void save(Word address) {
            // TODO: only save if its not the first time
            MainMemory.saveBlock(address, cached);
        }
    }

    private CacheUnit[] cache = new CacheUnit[] {
            new CacheUnit(),
            new CacheUnit(),
            new CacheUnit(),
            new CacheUnit(),
    };
    // TODO: eviction strategy
    private Random evicter = new Random();

    // we start address at 1024 because there are only 1024 memory addresses
    // so any address over 1024 invalid
    // what happens if we overwrite an instruction with a store, so the cache has
    // not been updated but the in main memory is updated

    // TODO: what happens if we read near the end

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
    private <T> T access(Word address, Function<Word, T> accesor) {
        if (Stream.iterate(10, i -> i < 32, i -> i + 1).map(address::getBit)
                .reduce(Bit::or)
                .get().getValue()) {

            throw new MemoryReadError((int) address.getUnsigned());
        }
        return Stream.iterate(0, i -> i < cache.length, i -> i + 1).map(i -> cache[i].access(address, accesor))
                .reduce((a, b) -> a.or(() -> b)).get().orElseGet(() -> {
                    var evicted = cache[evicter.nextInt(cache.length)];
                    evicted.save(address);
                    evicted.update(address);
                    return evicted.access(address, accesor).get();
                });
    }

    public Word read(Word address) {
        return access(address, i -> i);
    }

    public void write(Word address, Word value) {
        access(address, i -> {
            i.copy(value);
            // we just use null because generics cannot be instiated with void
            return null;
        });
    }

}
