package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {

    @org.junit.jupiter.api.Test
    void testCompressionTextFile() {
        try {
            // compress the file
            String[] args = {"c", "src/test/resources/test1.txt", "4"};
            Runner.main(args);

            // decompress the file
            args = new String[]{"d", "src/test/resources/4.test1.txt.hc"};
            Runner.main(args);

            // compare the original file content with the decompressed file content
            String original = "src/test/resources/test1.txt";
            String decompressed = "src/test/resources/extracted.4.test1.txt";
            assertTrue(Files.readAllLines(Path.of(original)).equals(Files.readAllLines(Path.of(decompressed))));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void testCompressionPdfFile() {
        try {
            // compress the file
            String[] args = {"c", "src/test/resources/test1.pdf", "4"};
            Runner.main(args);

            // decompress the file
            args = new String[]{"d", "src/test/resources/4.test1.pdf.hc"};
            Runner.main(args);

            // compare the original file content with the decompressed file content

            String original = "src/test/resources/test1.pdf";
            String decompressed = "src/test/resources/extracted.4.test1.pdf";

            // compute the sha256 hash of the original file
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] originalHash = digest.digest(Files.readAllBytes(Path.of(original)));
            byte[] decompressedHash = digest.digest(Files.readAllBytes(Path.of(decompressed)));

            assertArrayEquals(originalHash, decompressedHash);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void testCompressionImageFile() {
        try {
            // compress the file
            String[] args = {"c", "src/test/resources/test1.jpg", "4"};
            Runner.main(args);

            // decompress the file
            args = new String[]{"d", "src/test/resources/4.test1.jpg.hc"};
            Runner.main(args);

            // compare the original file content with the decompressed file content

            String original = "src/test/resources/test1.jpg";
            String decompressed = "src/test/resources/extracted.4.test1.jpg";

            // compute the sha256 hash of the original file
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] originalHash = digest.digest(Files.readAllBytes(Path.of(original)));
            byte[] decompressedHash = digest.digest(Files.readAllBytes(Path.of(decompressed)));

            assertArrayEquals(originalHash, decompressedHash);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void testCompressionPdfFile2() {
        try {
            // compress the file
            String[] args = {"c", "src/test/resources/test2.pdf", "4"};
            Runner.main(args);

            // decompress the file
            args = new String[]{"d", "src/test/resources/4.test2.pdf.hc"};
            Runner.main(args);

            // compare the original file content with the decompressed file content

            String original = "src/test/resources/test2.pdf";
            String decompressed = "src/test/resources/extracted.4.test2.pdf";

            // compute the sha256 hash of the original file
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] originalHash = digest.digest(Files.readAllBytes(Path.of(original)));
            byte[] decompressedHash = digest.digest(Files.readAllBytes(Path.of(decompressed)));

            assertArrayEquals(originalHash, decompressedHash);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}