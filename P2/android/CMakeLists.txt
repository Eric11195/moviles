# From https://www.techyourchance.com/android-ndk-tutorial/


# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

add_library( # Specifies the name of the library.
        hashLib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/getHashForJSONObject.cpp )

# This helper variable points to the location of the native sources
set(NATIVE_SRC_DIR src/main/cpp)

# Add specific source files to the library
target_sources(
        my-native-code
        PRIVATE
        ${NATIVE_SRC_DIR}/jni-facade.cpp
        ${NATIVE_SRC_DIR}/fibonacci/fibonacci.cpp
)
# Look for header files in these directories
target_include_directories(
        my-native-code
        PRIVATE
        ${NATIVE_SRC_DIR}/fibonacci/
)