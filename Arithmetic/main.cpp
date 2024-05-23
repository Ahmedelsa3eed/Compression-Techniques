#include "compress.h"
#include "decompress.h"

using namespace std;

int main(int argc, char *argv[]) {
    if (argc < 2)
        cerr << "mode c or d not found" << "\n";
    string flag = argv[1];
    if (flag == "c" && argc == 3)
        run_compression(argv[2]);
    else if (flag == "d" && argc == 4)
        run_decompression(argv[2], argv[3]);
    else
        cerr << "Invalid argument" << "\n";
    return 0;
}
