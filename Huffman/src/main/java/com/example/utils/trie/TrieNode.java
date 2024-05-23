package com.example.utils.trie;

import java.io.Serializable;

public class TrieNode implements Serializable {
    public int freq;
    public TrieNode left;
    public TrieNode right;

    public TrieNode() {
    }

    public TrieNode(int freq) {
        this.freq = freq;
        left = null;
        right = null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TrieNode t))
            return false;

        if (o == this)
            return true;

        boolean isEqual = true;
        if (freq != t.freq)
            return false;

        if (left != null)
            isEqual = left.equals(t.left);

        if (!isEqual)
            return false;

        if (right != null)
            isEqual = right.equals(t.right);

        return isEqual;
    }
}
