cmake_minimum_required(VERSION 3.22.1)
project(MyHashProject)

add_library(
        hashLib
        SHARED
        src/main/cpp/getHashForJSONObject.cpp
)

# Conditional logic: Check if we are building for Android or Desktop
if(CMAKE_SYSTEM_NAME STREQUAL "Android")
    # 1. ANDROID BUILD: The NDK provides jni.h automatically.
    target_include_directories(hashLib PRIVATE src/main/cpp)
else()
    # 2. WINDOWS DESKTOP BUILD: Explicitly find and include JDK headers.
    find_package(JNI REQUIRED)
    target_include_directories(hashLib PRIVATE
            src/main/cpp
            ${JNI_INCLUDE_DIRS}
    )
endif()