package LZW;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Decoder {
    private Map<Integer, String> dictionary;
    private int dictSize;
    private final int initialDictSize;

    public Decoder(int dictSize) {
        this.initialDictSize = dictSize;
        initializeDictionary();
    }

    private void initializeDictionary() {
        this.dictSize = initialDictSize;
        this.dictionary = new HashMap<>();
        for (int i = 0; i < initialDictSize; i++) {
            this.dictionary.put(i, "" + (char) i);
        }
    }

    public String decode(byte[] data) {
        initializeDictionary();
        StringBuilder output = new StringBuilder();
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        int current = byteBuffer.getShort() & 0xFFFF;
        int prev;
        output.append(dictionary.get(current));
        while (byteBuffer.hasRemaining()) {
            prev = current;
            current = byteBuffer.getShort() & 0xFFFF;
            String entry;
            if (dictionary.containsKey(current)) {
                entry = dictionary.get(current);
            } else {
                entry = dictionary.get(prev) + dictionary.get(prev).charAt(0);
            }
            output.append(entry);
            dictionary.put(dictSize++, dictionary.get(prev) + entry.charAt(0));
        }
        return output.toString();
    }

    public void decompressFile(String filePath, String outputFilePath) {

        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(filePath);
             FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            int n = 1;
            while (true) {
                // Read the size of the next chunk
                byte[] chunkSizeBytes = new byte[4];
                if (fis.read(chunkSizeBytes) != 4) break;
                int chunkSize = ByteBuffer.wrap(chunkSizeBytes).getInt();

                if (chunkSize == 0) break;

                // Read the chunk data
                byte[] buffer = new byte[chunkSize];
                if (fis.read(buffer) != chunkSize) break;

                // Decode the chunk
                String decodedOutput = decode(buffer);
                for (int i = 0; i < decodedOutput.length(); i++) {
                    fos.write(decodedOutput.charAt(i));
                }

            }
            System.out.println("Chuncks Number: "+n);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Compression Time: "+ (System.currentTimeMillis()-startTime));
    }

    public static void main(String[] args) {
        Decoder decoder = new Decoder(256);
        decoder.decompressFile("compressed_file.lzw", "decompressed_output.txt");
    }
}
