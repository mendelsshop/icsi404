package Computer;

import static Utils.Utils.*;

public class MainMemory {
    private static Word MEMORY[] = new Word[] {
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(),
            getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), getZero(), };

    // TODO; rnage check on addres
    public static Word read(Word address) {
        return MEMORY[(int) address.getUnsigned()].clone();
    }

    // TODO; rnage check on addres
    public static void write(Word address, Word value) {
        // TODO: should we copy word
        MEMORY[(int) address.getUnsigned()] = value;
    }

    public static void load(String[] data) {
        for (var i = 0; i < data.length; i++) {
            // We assume bits are encode rtl
            MEMORY[i].setBit(31, new Bit(data[i].charAt(0) == '1'));
            MEMORY[i].setBit(30, new Bit(data[i].charAt(1) == '1'));
            MEMORY[i].setBit(29, new Bit(data[i].charAt(2) == '1'));
            MEMORY[i].setBit(28, new Bit(data[i].charAt(3) == '1'));
            MEMORY[i].setBit(27, new Bit(data[i].charAt(4) == '1'));
            MEMORY[i].setBit(26, new Bit(data[i].charAt(5) == '1'));
            MEMORY[i].setBit(25, new Bit(data[i].charAt(6) == '1'));
            MEMORY[i].setBit(24, new Bit(data[i].charAt(7) == '1'));
            MEMORY[i].setBit(23, new Bit(data[i].charAt(8) == '1'));
            MEMORY[i].setBit(22, new Bit(data[i].charAt(9) == '1'));
            MEMORY[i].setBit(21, new Bit(data[i].charAt(10) == '1'));
            MEMORY[i].setBit(20, new Bit(data[i].charAt(11) == '1'));
            MEMORY[i].setBit(19, new Bit(data[i].charAt(12) == '1'));
            MEMORY[i].setBit(18, new Bit(data[i].charAt(13) == '1'));
            MEMORY[i].setBit(17, new Bit(data[i].charAt(14) == '1'));
            MEMORY[i].setBit(16, new Bit(data[i].charAt(15) == '1'));
            MEMORY[i].setBit(15, new Bit(data[i].charAt(16) == '1'));
            MEMORY[i].setBit(14, new Bit(data[i].charAt(17) == '1'));
            MEMORY[i].setBit(13, new Bit(data[i].charAt(18) == '1'));
            MEMORY[i].setBit(12, new Bit(data[i].charAt(19) == '1'));
            MEMORY[i].setBit(11, new Bit(data[i].charAt(20) == '1'));
            MEMORY[i].setBit(10, new Bit(data[i].charAt(21) == '1'));
            MEMORY[i].setBit(9, new Bit(data[i].charAt(22) == '1'));
            MEMORY[i].setBit(8, new Bit(data[i].charAt(23) == '1'));
            MEMORY[i].setBit(7, new Bit(data[i].charAt(24) == '1'));
            MEMORY[i].setBit(6, new Bit(data[i].charAt(25) == '1'));
            MEMORY[i].setBit(5, new Bit(data[i].charAt(26) == '1'));
            MEMORY[i].setBit(4, new Bit(data[i].charAt(27) == '1'));
            MEMORY[i].setBit(3, new Bit(data[i].charAt(28) == '1'));
            MEMORY[i].setBit(2, new Bit(data[i].charAt(29) == '1'));
            MEMORY[i].setBit(1, new Bit(data[i].charAt(30) == '1'));
            MEMORY[i].setBit(0, new Bit(data[i].charAt(31) == '1'));
        }
    }

}
