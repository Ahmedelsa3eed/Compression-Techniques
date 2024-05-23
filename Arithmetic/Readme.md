# User Manual

## Prerequisites
- You need to extract the executable or use the executable as is in the root directory of the project

### How to build executable
```bash
 g++ main.cpp OutStream.cpp InStream.cpp -o arithmetic
```

## How to Run Arithmetic Compressor
```bash
./arithmetic c </path/input/file>
```
where:
- `c` is the flag to compress the file
- `</path/input/file>` is the path to the file to be compressed (absolute or relative to the Arithmetic root directory)

example:
```bash
./arithmetic c ./assests/sample.txt
```

## How to Run Arithmetic Decompressor
```bash
./arithmetic d </path/compressed/file> </path/input/file>
```
where:
- `d` is the flag to decompress the file
- `</path/compressed/file>` is the path to the compressed file (absolute or relative to the Arithmetic root directory)
- `</path/input/file>` is the path to the input file (absolute or relative to the Arithmetic root directory)

example:
```bash
./arithmetic d ./assests/sample.AZIP ./assests/sample.txt
```
