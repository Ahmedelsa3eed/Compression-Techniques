//
// Created by Dell on 21/05/2024.
//

#ifndef ARITHMETIC_CODING_UTIL_H
#define ARITHMETIC_CODING_UTIL_H

#include <iostream>
#include <fstream>
#include <map>
#include <vector>
#include "types.h"

using namespace std;

array<u64, 258> compute_cumulative_freq(const string &filepath) {
    std::ifstream input_file(filepath, std::ios::binary);

    // Check if the file was opened successfully
    if (!input_file) {
        std::cerr << "Unable to open file" << std::endl;
        exit(1);
    }

    char ch;
    int freq[257] = {0};
    while (input_file.get(ch)) {
        freq[(u64) ch & 0xff]++;
    }
    freq[256] = 1;

    input_file.close();
    array<u64, 258> cumulative_freq{};
    cumulative_freq.fill(0);
    for (int i = 1; i < cumulative_freq.size(); i++)
        cumulative_freq[i] = cumulative_freq[i - 1] + freq[i - 1];
    return cumulative_freq;
}

pair<string, string> get_file_name_and_extension(const string &filepath) {
    size_t pos = filepath.find_last_of('.');
    string extension;
    string filename = filepath;
    if (pos != string::npos) {
        extension = filepath.substr(pos + 1);
        filename = filepath.substr(0, pos);
    }
    return {filename, extension};
}

long long get_file_size(const string& filepath) {
    std::ifstream file(filepath, std::ios::binary | std::ios::ate);
    if (!file.is_open()) {
        std::cerr << "Error opening file: " << filepath << std::endl;
        return -1;
    }
    return file.tellg();
}

#endif //ARITHMETIC_CODING_UTIL_H
