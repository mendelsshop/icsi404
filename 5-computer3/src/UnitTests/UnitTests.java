package UnitTests;

import static Utils.Utils.*;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BiFunction;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import Computer.*;

public class UnitTests {
        static final Bit[] AND = new Bit[] { new Bit(true), new Bit(false), new Bit(false), new Bit(false) };
        static final Bit[] OR = new Bit[] { new Bit(true), new Bit(false), new Bit(false), new Bit(true) };
        static final Bit[] XOR = new Bit[] { new Bit(true), new Bit(false), new Bit(true), new Bit(false) };
        static final Bit[] NOT = new Bit[] { new Bit(true), new Bit(false), new Bit(true), new Bit(true) };
        static final Bit[] LEFT_SHIFT = new Bit[] { new Bit(true), new Bit(true), new Bit(false),
                        new Bit(false) };
        static final Bit[] RIGHT_SHIFT = new Bit[] { new Bit(true), new Bit(true), new Bit(false),
                        new Bit(true) };
        static final Bit[] ADD = new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(false) };
        static final Bit[] SUB = new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(true) };
        static final Bit[] MUL = new Bit[] { new Bit(false), new Bit(true), new Bit(true), new Bit(true) };
        static final Word ZEROONE = new Word(new Bit[] {
                        getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
                        getFalse(),
                        getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
                        getTrue(),
                        getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
                        getFalse(),
                        getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
        });
        static final Word ONEZERO = new Word(new Bit[] {
                        getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
                        getTrue(),
                        getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
                        getFalse(),
                        getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
                        getTrue(),
                        getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
        });

        private static Duration timeOperation(Runnable r) {
                Instant start = Instant.now();
                r.run();
                Instant end = Instant.now();
                return Duration.between(start, end);
        }

        static void compareRange(int lowerInclusive, int higherExclusive, IntConsumer doer, IntConsumer doer2,
                        String name) {
                var t1 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive)
                                .parallel()
                                .forEach(doer));
                var t2 = timeOperation(() -> IntStream.range(lowerInclusive, higherExclusive)
                                .parallel()
                                .forEach(doer2));

                System.out.println(name + " orginal:" + t1.toMillis() + " new:" + t2.toMillis());
        }

        public static void doInRange(int start, int end, IntConsumer doer, String beingTested) {
                var t1 = timeOperation(() -> IntStream.range(start, end).parallel().forEach(doer));
                System.out.println("doing " + beingTested + " from " + start + "to end " + end + "took " + t1);
        };

        public static void MatrixDoMath(BiFunction<Integer, Integer, Integer> actualOp, BiFunction<Word, Word, Word> op,
                        String opName, Tuple<Integer, Integer> n, Tuple<Integer, Integer> m) {
                IntConsumer inner = i -> {
                        var n1 = new Word(i);
                        doInRange(m.fst(), m.snd(), j -> {
                                var n2 = new Word(j);
                                var result = op.apply(n1, n2);
                                assertEquals((int) actualOp.apply(i, j), (int) result.getUnsigned());
                        }, opName + " inner");
                };
                doInRange(n.fst(), n.snd(), inner, opName + " outer");
        }
}
