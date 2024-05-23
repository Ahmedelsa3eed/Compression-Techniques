#include <iostream>
#include "OutStream.h"

OutStream::OutStream(const string &filepath) {
    output_file = ofstream(filepath, std::ios::binary);
    byte = 0;
    count_bits = 0;
}

void OutStream::flush_byte() {
    output_file.put((char) byte);
//    output_file.flush();
    byte = 0;
    count_bits = 0;
}

void OutStream::write_bit(u8 bit) {
    byte <<= 1;
    byte |= bit;
    count_bits++;
    if (count_bits == 8) {
        flush_byte();
    }
}

void OutStream::write_bit(u8 bit, int count) {
    for (int i = 0; i < count; i++)
        write_bit(bit);
}

void OutStream::pad_with(u8 bit) {
    while (count_bits != 0) {
        write_bit(bit);
    }
}

void OutStream::close() {
    output_file.close();
}
