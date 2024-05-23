#ifndef ARITHMETIC_CODING_DECOMPRESS_H
#define ARITHMETIC_CODING_DECOMPRESS_H

#include <iostream>
#include <cassert>
#include <bitset>
#include <chrono>
#include "util.h"
#include "OutStream.h"
#include "InStream.h"

#define EOF_SYMBOL 256

u16 get_symbol(const array<u64, 258> &cf, u64 scaled_symbol) {
    u16 symbol = 0;
    while (scaled_symbol >= cf[symbol + 1]) {
        symbol++;
    }
    return symbol;
}

void decompress(const string &input_filepath, const string &output_filepath, const array<u64, 258> &cf) {
    InStream inStream(input_filepath);
    std::ofstream output_file(output_filepath, std::ios::binary);

    u64 total_cf = cf[EOF_SYMBOL + 1];
    assert(total_cf <= 0xffffffff);

    u32 lower_bound = 0, upper_bound = ~0;
    u64 encoded_bits = inStream.read_bits(32);

    while (true) {
        u64 low64 = lower_bound & 0xffffffff;
        u64 up64 = upper_bound & 0xffffffff;
        u64 encoded64 = encoded_bits & 0xffffffff;

        u64 range = up64 - low64 + 1;
        u64 scaled_symbol = ((encoded64 - low64 + 1) * total_cf - 1) / range;

        u16 symbol = get_symbol(cf, scaled_symbol);
        if (symbol == EOF_SYMBOL)
            break;

        output_file.put((char) symbol);

        u64 cf_low = cf[symbol];
        u64 cf_high = cf[symbol + 1];

        up64 = low64 + range * cf_high / total_cf - 1;
        low64 = low64 + range * cf_low / total_cf;

        lower_bound = static_cast<u32>(low64);
        upper_bound = static_cast<u32>(up64);
        encoded_bits = (u32) encoded64;
        assert(lower_bound < upper_bound);
        while (true) {
            if ((lower_bound >> 31) == (upper_bound >> 31)) {
                lower_bound <<= 1;

                upper_bound <<= 1;
                upper_bound |= 1;

                encoded_bits <<= 1;
                encoded_bits |= inStream.read_bit();
            }
            else if (((lower_bound >> 30) & 1) == 1 && ((upper_bound >> 30) & 1) == 0) {
                lower_bound <<= 1;
                lower_bound &= ((1U << 31) - 1);

                upper_bound <<= 1;
                upper_bound |= (1U << 31);
                upper_bound |= 1;

                u32 msb = encoded_bits >> 31;
                u32 rest = encoded_bits & (0x3fffffff);
                encoded_bits = (msb << 31) | (rest << 1) | inStream.read_bit();
            }
            else
                break;
            assert(lower_bound < upper_bound);
        }
    }
}

void run_decompression(const string &compressed_filepath, const string &original_filepath) {
    auto file_name_extension = get_file_name_and_extension(original_filepath);
    string decompressed_file_path = file_name_extension.first + "_decompressed" + "." + file_name_extension.second;

    auto start = chrono::high_resolution_clock::now();

    array<u64, 258> cf = compute_cumulative_freq(original_filepath);
    decompress(compressed_filepath, decompressed_file_path, cf);

    auto end = chrono::high_resolution_clock::now();

    chrono::duration<double, milli> elapsed = end - start;

    cout << "Decompression time: " << elapsed.count() << " ms\n";
}

#endif