#include <iostream>
#include "str_print.h"

extern "C" {
    void print(const char* text) {
        StrPrint cpp_instance;

        std::string str = text;
        
        // 调用 C++ 中的 print 函数
        cpp_instance.print(text);
    }
}
