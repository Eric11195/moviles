#include "getHashForJSONObject.hpp"
#include <jni.h>

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_game_testing_utils_HashUtils_getHashForJSONObject(JNIEnv *env, jclass clazz,
                                                      jstring json_string) {
    // TODO: implement getHash()
    return 0ll;
}