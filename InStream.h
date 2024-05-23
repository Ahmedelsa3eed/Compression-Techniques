#ifndef ARITHMETIC_CODING_INSTREAM_H
#define ARITHMETIC_CODING_INSTREAM_H

#include <fstream>
#include <cassert>
#include "types.h"

using namespace std;

class InStream {
private:
    ifstream input_file;
    int count_bits;
    u8 byte;
    bool done;
    void read_byte();
public:
    explicit InStream(const string &filepath);
    u32 read_bits(int no_bits);
    u32 read_bit();
    void close();
};


#endif //ARITHMETIC_CODING_INSTREAM_H
