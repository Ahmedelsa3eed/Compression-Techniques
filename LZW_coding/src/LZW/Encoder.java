package LZW;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encoder {
    private Map<String, Integer> dictionary;
    private final int initialDictSize;
    private int dictSize;

    public Encoder(int dictSize) {
        this.initialDictSize = dictSize;
        initializeDictionary();
    }

    private void initializeDictionary() {
        this.dictSize = initialDictSize;
        this.dictionary = new HashMap<>();
        for (int i = 0; i < initialDictSize; i++) {
            this.dictionary.put("" + (char) i, i);
        }
    }

    public List<Integer> encode(String data) {
        initializeDictionary();
        String current = "";
        List<Integer> encoded = new ArrayList<>();
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        for (byte b : dataBytes) {
            int c = b & 0xFF;
            String currentPlusC = current + (char) c;
            if (dictionary.containsKey(currentPlusC)) {
                current = currentPlusC;
            } else {
                encoded.add(dictionary.get(current));
                dictionary.put(currentPlusC, dictSize++);
                current = "" + (char) c;
            }
        }
        if (!current.isEmpty()) {
            encoded.add(dictionary.get(current));
        }
        return encoded;
    }

    public List<Byte> toByteArray(List<Integer> intList) {
        List<Byte> byteList = new ArrayList<>();
        for (Integer num : intList) {
            byteList.add((byte) ((num >> 8) & 0xFF));
            byteList.add((byte) (num & 0xFF));
        }
        return byteList;
    }

    public void compressFile(String filePath) {
        final int CHUNK_SIZE = 256 * 1024; // 256 KB
        Path path = Paths.get(filePath);
        String outputFilePath = filePath + ".lzw";

        long startTime = System.currentTimeMillis();

        try (FileInputStream fis = new FileInputStream(path.toFile());
             FileOutputStream fos = new FileOutputStream(outputFilePath)) {

            int n = 1;
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                String data = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                List<Integer> encodedData = encode(data);
                List<Byte> byteArray = toByteArray(encodedData);

                byte[] sizeBytes = ByteBuffer.allocate(4).putInt(byteArray.size()).array();
                fos.write(sizeBytes);
                for (Byte b : byteArray) {
                    fos.write(b);
                }
            }
            System.out.println("Chuncks Number: "+n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Compression Time: "+ (System.currentTimeMillis()-startTime));
    }

    public static void main(String[] args) throws Exception {
        String filePath = "AWS_Cassandra_Whitepaper.pdf";
        String originalHash = calculateSHA256(filePath);

        Encoder encoder = new Encoder(256);
        encoder.compressFile(filePath);
        System.out.println("Finished Encoding");

        Decoder decoder = new Decoder(256);
        String decompressedPath = "AWS_Cassandra_Whitepaper_de.pdf";
//        decoder.decompressFile(filePath + ".lzw", decompressedPath);
        System.out.println("Finished Decoding");

        String decompressedHash = calculateSHA256(decompressedPath);

        if (originalHash.equals(decompressedHash)) {
            System.out.println("Hashes match. Compression and decompression successful.");
        } else {
            System.out.println("Hashes do not match. Compression and decompression failed.");
        }
    }

    public static String calculateSHA256(String filePath) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}