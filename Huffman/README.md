# User Manual

## Prerequisites
- You need to extract the jar or use the jar as is in the root directory of the project

## How to Run Huffman Compressor
```bash
java -jar Huffman.jar c </path/input/file> <n>
```
where:
- `c` is the flag to compress the file
- `</path/input/file>` is the path to the file to be compressed (absolute or relative to the Huffman root directory)
- `<n>` is the number of bytes to operate on at a time

example:
```bash
java -jar Huffman.jar c src/main/resources/test1.txt 4
```

## How to Run Huffman Decompressor
```bash
java -jar Huffman.jar d </path/compressed/file>
```
where:
- `d` is the flag to decompress the file
- `</path/compressed/file>` is the path to the compressed file (absolute or relative to the Huffman root directory)

example:
```bash
java -jar Huffman.jar d src/main/resources/4.test1.txt.hc
```
