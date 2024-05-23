package test;

import LZW.Decoder;
import LZW.Encoder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static LZW.Encoder.calculateSHA256;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompressionTest {

    @Test
    public void testCompressDecompressFile1() throws IOException {
        Encoder encoder = new Encoder(256);
        encoder.compressFile("Project/LWZ_Encoding/src/toBecompressed.txt");
        System.out.println("Finished Encoding");

        System.out.println();

        Decoder decoder = new Decoder(256);
        decoder.decompressFile("Project/LWZ_Encoding/src/toBecompressed.txt.lzw", "Project/LWZ_Encoding/src/toBecompressed.de.txt");
        System.out.println("Finished Decoding");


        String decompressedHash = calculateSHA256("Project/LWZ_Encoding/src/toBecompressed.de.txt");
        String originalHash = calculateSHA256("Project/LWZ_Encoding/src/toBecompressed.txt");

        // Assert that the hashes are the same
        assertEquals(originalHash, decompressedHash);
    }

    @Test
    public void testCompressDecompressFile2() throws IOException {
        Encoder encoder = new Encoder(256);
        encoder.compressFile("Project/LWZ_Encoding/src/gbbct10.seq");
        System.out.println("Finished Encoding");

        System.out.println();

        Decoder decoder = new Decoder(256);
        decoder.decompressFile("Project/LWZ_Encoding/src/gbbct10.seq.lzw", "Project/LWZ_Encoding/src/gbbct10.seq.lzw.de.txt");
        System.out.println("Finished Decoding");


        String decompressedHash = calculateSHA256("Project/LWZ_Encoding/src/gbbct10.seq.lzw.de.txt");
        String originalHash = calculateSHA256("Project/LWZ_Encoding/src/gbbct10.seq");

        // Assert that the hashes are the same
        assertEquals(originalHash, decompressedHash);
    }

    @Test
    public void testCompressDecompressFile3() throws IOException {
        Encoder encoder = new Encoder(256);
        encoder.compressFile("Project/LWZ_Encoding/src/arabic_test.txt");
        System.out.println("Finished Encoding");

        System.out.println();

        Decoder decoder = new Decoder(256);
        decoder.decompressFile("Project/LWZ_Encoding/src/arabic_test.txt.lzw", "Project/LWZ_Encoding/src/arabic_test.de.txt");
        System.out.println("Finished Decoding");


        String decompressedHash = calculateSHA256("Project/LWZ_Encoding/src/arabic_test.de.txt");
        String originalHash = calculateSHA256("Project/LWZ_Encoding/src/arabic_test.txt");

        // Assert that the hashes are the same
        assertEquals(originalHash, decompressedHash);
    }
}
