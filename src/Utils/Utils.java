package Utils;

import Computer.*;

public final class Utils {
        private static final Bit FALSE = new Bit(false);

        public static final Bit getFalse() {
                return FALSE.clone();
        }

        private static final Bit TRUE = new Bit(true);

        public static final Bit getTrue() {
                return TRUE.clone();
        }

        private static final Bit[] TRUE_BITS = new Bit[] {
                        getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                        getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                        getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
                        getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(), getTrue(),
        };

        public static final Bit[] getTrueBits() {
                return TRUE_BITS.clone();
        }

        private static final Bit[] FALSE_BITS = new Bit[] {
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
                        getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(), getFalse(),
        };

        public static final Bit[] getFalseBits() {
                return FALSE_BITS.clone();
        }

        private static final Word BIG_NUMBER = new Word(TRUE_BITS);

        public static final Word getBigNumber() {
                return BIG_NUMBER.clone();
        }

        private static final Word ZERO = new Word(FALSE_BITS);

        public static final Word getZero() {
                return ZERO.clone();
        }

        public static final void checkBitRange0(final int i) {
                if (i < 0 || i > 31) {
                        throw new IndexOutOfBoundsException();
                }
        }

        public static final void checkBitRange1(final int i) {
                if (i < 1 || i > 32) {
                        throw new IndexOutOfBoundsException();
                }
        }

        public static final record Tuple<T, U>(T fst, U snd) {
        }

        public static final record Triple<T, U, V>(T fst, U snd, V thrd) {
                public final Triple<T, U, V> setFst(final T newFst) {
                        return new Triple<T, U, V>(newFst, snd, thrd);
                }

                public final Triple<T, U, V> setSnd(final U newSnd) {
                        return new Triple<T, U, V>(fst, newSnd, thrd);
                }

                public final Triple<T, U, V> setThrd(final V newThrd) {
                        return new Triple<T, U, V>(fst, snd, newThrd);
                }
        }

        @FunctionalInterface
        public interface TriFunction<T, U, V, R> {
                public R apply(T t, U u, V v);

        }

        public static final Bit[] AND = new Bit[] { new Bit(true), new Bit(false), new Bit(false), new Bit(false) };
        public static final Bit[] OR = new Bit[] { new Bit(true), new Bit(false), new Bit(false), new Bit(true) };
        public static final Bit[] XOR = new Bit[] { new Bit(true), new Bit(false), new Bit(true), new Bit(false) };
        public static final Bit[] NOT = new Bit[] { new Bit(true), new Bit(false), new Bit(true), new Bit(true) };
        public static final Bit[] LEFT_SHIFT = new Bit[] { new Bit(true), new Bit(true), new Bit(false),
                        new Bit(false) };
        public static final Bit[] RIGHT_SHIFT = new Bit[] { new Bit(true), new Bit(true), new Bit(false),
                        new Bit(true) };
        public static final Bit[] ADD = new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(false) };
        public static final Bit[] SUB = new Bit[] { new Bit(true), new Bit(true), new Bit(true), new Bit(true) };
        public static final Bit[] MUL = new Bit[] { new Bit(false), new Bit(true), new Bit(true), new Bit(true) };
}
