package com.example;

import com.example.utils.ByteArray;
import com.example.utils.CodeWord;
import com.example.utils.CodeWordFactory;
import com.example.utils.trie.TrieBuilder;
import com.example.utils.trie.TrieNode;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Compressor {
    public static final int BUFFER_SIZE = 8 * 1024;
    private static byte[] remainingBytes;
    private static byte noBytesRemaining;
    private static long noGroups;

    public static void compressFile(String inputFilePath, String outputFilePath, int n) throws IOException {
        Map<ByteArray, Integer> keyToFreqMap = constructFreqMap(inputFilePath, n);
        TrieNode root = TrieBuilder.buildTrie(keyToFreqMap);
        Map<ByteArray, CodeWord> keyToCodeWordMap = CodeWordFactory.buildKeyToCodeWordMap(root);
        writeInCompressedFile(inputFilePath, outputFilePath, keyToCodeWordMap, root, n);
    }


    private static Map<ByteArray, Integer> constructFreqMap(String filePath, int n) throws IOException {
        Map<ByteArray, Integer> map = new HashMap<>();
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE))
        {
            long fileSize = Files.size(Path.of(filePath));
            noGroups = fileSize / n;
            noBytesRemaining = (byte)(fileSize % n);
            remainingBytes = new byte[noBytesRemaining];
            byte[] buffer = new byte[n];
            // I have to make the buffer array of bytes bigger then parse each n bytes of buffer and encode them.
            long count = 0;
            while (count < noGroups && inputStream.read(buffer) != -1) {
                ByteArray key = new ByteArray(buffer.clone());
                map.merge(key, 1, Integer::sum);
                count++;
            }
            if (noBytesRemaining > 0)
                inputStream.read(remainingBytes);
        }
        return map;
    }

    private static void writeInCompressedFile(String inputFilePath, String outputFilePath, Map<ByteArray, CodeWord> keyToCodeWordMap, TrieNode root, int n) throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFilePath), BUFFER_SIZE);
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFilePath), BUFFER_SIZE);
             ObjectOutputStream os = new ObjectOutputStream(outputStream)
        )
        {
            os.writeObject(root);
            outputStream.write(ByteBuffer.allocate(8).putLong(noGroups).array());
            if (noBytesRemaining > 0) {
                outputStream.write(noBytesRemaining);
                outputStream.write(remainingBytes);
            }
            else
                outputStream.write(0);

            byte[] buffer = new byte[n];
            BitSet bitSet = new BitSet(8);
            int count = 0;
            long countGroups = 0;
            while (countGroups < noGroups && inputStream.read(buffer) != -1) {
                ByteArray key = new ByteArray(buffer);
                CodeWord codeWord = keyToCodeWordMap.get(key);
                BitSet code = codeWord.code;
                int i = codeWord.noBits - 1;
                while (i >= 0) {
                    if (code.get(i))
                        bitSet.set(count);
                    i--;
                    count++;
                    if (count == 8) {
                        if (bitSet.isEmpty())
                            outputStream.write(0);
                        else
                            outputStream.write(bitSet.toByteArray());
                        bitSet.clear();
                        count = 0;
                    }
                }
                countGroups++;
            }
            if (count > 0) {
                if (bitSet.isEmpty()) outputStream.write(0);
                else outputStream.write(bitSet.toByteArray());
            }
        }
    }
}
