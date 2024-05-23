package LZW;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // Example byte array
        byte[] byteArray = {1, 2, 3};

        // Convert byte[] to Byte[] using streams
        Byte[] byteObjectArray = IntStream.range(0, byteArray.length)
                .mapToObj(i -> byteArray[i])
                .toArray(Byte[]::new);

        // Print Byte array
        for (Byte b : byteObjectArray) {
            System.out.print(b + " ");
        }
        // Output: 1 2 3
    }
}
