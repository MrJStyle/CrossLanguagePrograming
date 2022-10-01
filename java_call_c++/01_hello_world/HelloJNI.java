public class HelloJNI {
    static {
        // Load native library hello.dll (Windows) or libhello.so (Unixes) or libhello.jnilib
        //  at runtime
        // This library contains a native method called sayHello()
        System.loadLibrary("hello");
    }

    private native void sayHello();

    public static void main(String[] args) {
        new HelloJNI().sayHello();
    }
}
