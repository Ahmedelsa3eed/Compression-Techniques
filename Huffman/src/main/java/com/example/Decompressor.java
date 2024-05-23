package com.example;

import com.example.utils.trie.LeafNode;
import com.example.utils.trie.TrieNode;

import java.io.*;
import java.math.BigInteger;
import java.util.BitSet;

public class Decompressor {

    public static void decompressFile(String inputFilePath, String outputFilePath) throws IOException, ClassNotFoundException {
        try (InputStream fis = new FileInputStream(inputFilePath);
             InputStream inputStream = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(fis);
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFilePath))
        )
        {
            TrieNode root = (TrieNode) ois.readObject();
            byte[] noGroupsBytes = new byte[8];
            inputStream.read(noGroupsBytes);
            long noGroups = new BigInteger(noGroupsBytes).longValue();
            int noBytesRemaining = inputStream.read();
            byte[] uncompressedBytes = new byte[noBytesRemaining];
            if (noBytesRemaining > 0)
                inputStream.read(uncompressedBytes);

            TrieNode currNode = root;
            byte[] buffer = new byte[8192];
            BitSet bitSet;
            int bytesRead;
            long countGroups = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bitSet = BitSet.valueOf(buffer);
                for (int i = 0; i < bytesRead * 8; i++) {
                    if (bitSet.get(i))
                        currNode = currNode.right;
                    else
                        currNode = currNode.left;
                    if (currNode.getClass() == LeafNode.class) {
                        byte[] bytes = ((LeafNode) currNode).key;
                        outputStream.write(bytes);
                        countGroups++;
                        if (countGroups == noGroups) {
                            if (noBytesRemaining > 0)
                                outputStream.write(uncompressedBytes);
                            return;
                        }
                        currNode = root;
                    }
                }
            }
        }
    }
}
