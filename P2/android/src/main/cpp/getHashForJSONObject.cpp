#include "getHashForJSONObject.hpp"
#include <jni.h>

#include <string>
#include <unordered_map>

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_game_testing_utils_HashUtils_getHashForJSONObject(JNIEnv *env, jclass clazz, jstring json_string) {
    //from: https://stackoverflow.com/questions/8029121/how-to-hash-stdstring
    std::hash<jstring> hasher;
    size_t hash = hasher(json_string);
    return hash;
}