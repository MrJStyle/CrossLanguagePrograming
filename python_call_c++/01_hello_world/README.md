# 通过 ctypes 调用 C++ lib

## 1.1 C++ 代码
作为示例，实现一个打印字符串的功能。为了模拟实际的工业场景，对以下代码进行编译，分别生成动态库 libstr_print_cpp.so、静态库libstr_print_cpp.a

```c++
// str_print.h

#pragma once
#include <string>

class StrPrint {
public:
    void print(const std::string& text);
};
```


```C++
// str_print.cpp

#include<iostream>
#include "str_print.h"

void StrPrint::print(const std::string& text) {
    std:: cout << text << std::endl;
}
```

```C++
// c_wrapper.cpp
// 需要对C++库进行封装，改造成对外提供C语言格式的接口。

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
```

## 1.2 生成动态库
为了支持Python与Java的跨语言调用，我们需要对封装好的接口生成动态库，生成动态库的方式有以下三种

* 方式1: 源码依赖方式，将c_wrapper和C++代码一起编译生成libstr_print.so。这种方式业务方只需要依赖一个so，使用成本较小，但是需要获取到源码。对于一些现成的动态库，可能不适用。
    ```shell
    g++ -o libstr_print.so str_print.cpp c_wrapper.cpp -fPIC -shared
    ```

* 方式2: 动态链接方式，这种方式生成的libstr_print.so，发布时需要携带上其依赖库libstr_print_cpp.so。 这种方式，业务方需要同时依赖两个so，使用的成本相对要高，但是不必提供原动态库的源码。
    ```shell
    g++ -o libstr_print.so c_wrapper.cpp -fPIC -shared -L. -lstr_print_cpp
    ```

* 方式3: 静态链接方式，这种方式生成的libstr_print.so，发布时无需携带上libstr_print_cpp.so。 这种方式，业务方只需依赖一个so，不必依赖源码，但是需要提供静态库。
    ```shell
    g++ c_wrapper.cpp libstr_print_cpp.a -fPIC -shared -o libstr_print.so
    ```


## 1.3 Python接入代码
Python标准库自带的ctypes可以实现加载C的动态库的功能，使用方法如下：

```python
import ctypes

lib = ctypes.cdll.LoadLibrary("libstr_print.so")

lib.print.argtypes = [ctypes.c_char_p]
lib.print.restype = None

# 此处只能传二进制
lib.print("Hello World!".encode())
```

