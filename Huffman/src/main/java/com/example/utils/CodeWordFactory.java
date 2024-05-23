package com.example.utils;

import com.example.utils.trie.LeafNode;
import com.example.utils.trie.TrieNode;

import java.util.*;

public class CodeWordFactory {
    public static Map<ByteArray, CodeWord> buildKeyToCodeWordMap(TrieNode root) {
        StringBuilder sb = new StringBuilder();
        Map<ByteArray, CodeWord> map = new HashMap<>();
        traverseTrie(root, sb, map);
        return map;
    }

    private static void traverseTrie(TrieNode root, StringBuilder sb, Map<ByteArray, CodeWord> map) {
        if (root instanceof LeafNode) {
            short noBits = sb.length() == 0 ? 1 : (short) sb.length();
            map.put(new ByteArray(((LeafNode) root).key), new CodeWord(createBitSet(sb.toString()), noBits));
            return;
        }

        if (root.left != null) {
            sb.append(0);
            traverseTrie(root.left, sb, map);
            sb.deleteCharAt(sb.length() - 1);
        }

        if (root.right != null) {
            sb.append(1);
            traverseTrie(root.right, sb, map);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public static BitSet createBitSet(String s) {
        BitSet bitSet = new BitSet(s.length());
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(s.length() - 1  - i) == '1')
                bitSet.set(i);
        }
        return bitSet;
    }


}
