import ctypes

lib = ctypes.cdll.LoadLibrary("libstr_print.so")

lib.print.argtypes = [ctypes.c_char_p]
lib.print.restype = None

# 此处只能传二进制
lib.print("Hello World!".encode())
