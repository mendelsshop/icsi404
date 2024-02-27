package UnitTests;

import static Utils.Utils.*;

import java.time.Duration;
import java.time.Instant;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import Computer.*;

public class UnitTests {
    static final Word ZEROONE = new Word(new Bit[] {
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
    });
    static final Word ONEZERO = new Word(new Bit[] {
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
            getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(),
            getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(), getFalse(), getTrue(),
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
        System.out.println("doing " + beingTested + "from " + start + "to end " + end + "took " + t1);
    };

}
