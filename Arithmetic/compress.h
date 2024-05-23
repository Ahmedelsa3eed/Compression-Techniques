#ifndef ARITHMETIC_CODING_COMPRESS_H
#define ARITHMETIC_CODING_COMPRESS_H

#include <iostream>
#include <cassert>
#include <bitset>
#include <chrono>
#include "util.h"
#include "OutStream.h"

#define EOF_SYMBOL 256

void compress(const string &input_filepath, const string &output_filepath, const array<u64, 258> &cf) {
    std::ifstream input_file(input_filepath, std::ios::binary);
    OutStream outStream(output_filepath);

    u64 total_cf = cf[EOF_SYMBOL + 1];
    assert(total_cf <= 0xffffffff);

    u32 lower_bound = 0, upper_bound = ~0;
    int underflow_counter = 0;

    char ch;
    u16 symbol;
    while (true) {
        if (input_file.get(ch)) {
            symbol = (u16)ch & 0xff;
        }
        else
            symbol = EOF_SYMBOL;

        u64 low64 = lower_bound & 0xffffffff;
        u64 up64 = upper_bound & 0xffffffff;

        u64 range = up64 + 1 - low64;
        u64 cf_low = cf[symbol];
        u64 cf_high = cf[symbol + 1];
        up64 = low64 + range * cf_high / total_cf - 1;
        low64 = low64 + range * cf_low / total_cf;

        assert(low64 < up64);
        lower_bound = static_cast<u32>(low64);
        upper_bound = static_cast<u32>(up64);
        assert(lower_bound < upper_bound);
        while (true) {
            if ((lower_bound >> 31) == (upper_bound >> 31)) {
                u32 b = (lower_bound >> 31);
                outStream.write_bit(b);
                outStream.write_bit(1 - b, underflow_counter);
                underflow_counter = 0;

                lower_bound <<= 1;

                upper_bound <<= 1;
                upper_bound |= 1;
            }
            else if (((lower_bound >> 30) & 1) == 1 && ((upper_bound >> 30) & 1) == 0) {
                underflow_counter++;

                lower_bound <<= 1;
                lower_bound &= ((1U << 31) - 1);

                upper_bound <<= 1;
                upper_bound |= (1U << 31);
                upper_bound |= 1;
            }
            else
                break;
            assert(lower_bound < upper_bound);
        }
//        cout << "lower_bound after shifts " << std::bitset<32>(lower_bound) << endl;
//        cout << "upper_bound after shifts " << std::bitset<32>(upper_bound) << endl;
        if (symbol == EOF_SYMBOL)
            break;
    }

    outStream.write_bit(0);
    outStream.write_bit(1);
    outStream.pad_with(1);
    outStream.close();
}

void run_compression(const string &input_filepath) {
    auto file_name_extension = get_file_name_and_extension(input_filepath);
    string compressed_filepath = file_name_extension.first + ".AZIP";

    auto start = chrono::high_resolution_clock::now();

    array<u64, 258> cf = compute_cumulative_freq(input_filepath);
    compress(input_filepath, compressed_filepath, cf);

    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> elapsed = end - start;

    cout << "Compression time: " << elapsed.count() << " ms\n";
    cout << "Compression ratio: " << static_cast<double>(get_file_size(compressed_filepath)) / static_cast<double>(get_file_size(input_filepath)) << "\n";
}

#endif