package com.example.game.testing.utils;

public class HashUtils {

    // Load the library on startup. The name MUST match the add_library name in CMakeLists.txt
    static {
        System.loadLibrary("hashLib");
    }

    // Declare the native method. The 'native' keyword tells Java to look in the C++ library.
    public native String getHash(String jsonString);
}