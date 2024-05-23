#include <iostream>
#include "InStream.h"

InStream::InStream(const string &filepath) {
    input_file = ifstream(filepath, std::ios::binary);
    count_bits = 8;
    byte = 0;
    done = false;
}

void InStream::read_byte() {
    char ch;
    if (!input_file.get(ch)) {
        done = true;
        count_bits = 0;
        return;
    }
    byte = (u8) ch;
    count_bits = 0;
}

u32 InStream::read_bits(int no_bits) {
    u32 bits = 0;
    for (int i = 0; i < no_bits; i++)
        bits |= (read_bit() << (no_bits - 1 - i));
    return bits;
}

u32 InStream::read_bit() {
    if (count_bits == 8)
        read_byte();
    else if (done)
        return 1;
    count_bits++;
    u32 bit = (byte >> (8 - count_bits)) & 1;
    assert(bit == 1 || bit == 0);
    return bit;
}

void InStream::close() {
    input_file.close();
}
