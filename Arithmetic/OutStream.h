#ifndef ARITHMETIC_CODING_OUTSTREAM_H
#define ARITHMETIC_CODING_OUTSTREAM_H

#include <fstream>
#include "types.h"

using namespace std;

class OutStream {
private:
    ofstream output_file;
    u8 byte;
    u8 count_bits;
    void flush_byte();
public:
    explicit OutStream(const string &filepath);
    void write_bit(u8 bit);
    void write_bit(u8 bit, int count);
    void pad_with(u8 bit);
    void close();
};


#endif //ARITHMETIC_CODING_OUTSTREAM_H
