package Utils;

import Computer.*;

public class Utils {
        private static final Bit FALSE = new Bit(false);

        public static Bit getFalse() {
                return FALSE.clone();
        }

        private static final Bit TRUE = new Bit(true);

        public static Bit getTrue() {
                return TRUE.clone();
        }

        private static final Bit[] TRUE_BITS = new Bit[] {
                        TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
                        TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
                        TRUE, };

        public static Bit[] getTrueBits() {
                return TRUE_BITS.clone();
        }

        private static final Bit[] FALSE_BITS = new Bit[] {
                        FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE,
                        FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE,
                        FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, };

        public static Bit[] getFalseBits() {
                return FALSE_BITS.clone();
        }

        private static final Word BIG_NUMBER = new Word(TRUE_BITS);

        public static Word getBigNumber() {
                return BIG_NUMBER.clone();
        }

        private static final Word ZERO = new Word(FALSE_BITS);

        public static Word getZero() {
                return ZERO.clone();
        }

        public static void checkBitRange(int i) {
                if (i < 0 || i > 32) {
                        // TODO: better exeption
                        throw new IndexOutOfBoundsException();
                }
        }
}
