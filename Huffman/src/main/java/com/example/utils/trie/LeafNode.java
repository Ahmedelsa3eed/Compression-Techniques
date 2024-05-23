package com.example.utils.trie;

import java.util.Arrays;

public class LeafNode extends TrieNode {
    public byte[] key;
    public LeafNode(byte[] key, int freq) {
        super(freq);
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LeafNode leaf))
            return false;

        if (o == this)
            return true;

        boolean isEqual = true;
        if (freq != leaf.freq || !Arrays.equals(key, leaf.key))
            return false;

        if (left != null)
            isEqual = left.equals(leaf.left);

        if (!isEqual)
            return false;

        if (right != null)
            isEqual = right.equals(leaf.right);

        return isEqual;
    }
}
