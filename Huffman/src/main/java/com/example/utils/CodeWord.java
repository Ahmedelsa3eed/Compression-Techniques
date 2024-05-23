package com.example.utils;

import java.util.BitSet;

public class CodeWord {
    public BitSet code;
    public short noBits;

    public CodeWord(BitSet c, short noBits) {
        code = c;
        this.noBits = noBits;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CodeWord w))
            return false;

        if (o == this)
            return true;

        return code.equals(w.code) && noBits == w.noBits;
    }
}
