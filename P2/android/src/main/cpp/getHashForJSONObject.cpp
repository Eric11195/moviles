#include <jni.h>
#include <string>
#include <fstream>
#include <sstream>

// cpp hash is not deterministic. That's why we gotta use:
// FNV-1a https://en.wikipedia.org/wiki/Fowler–Noll–Vo_hash_function
// Murmur 3 https://en.wikipedia.org/wiki/MurmurHash
// Deterministic 64-bit FNV-1a hash with Murmur3 finalizer mixing
uint64_t computeDeterministicHash64(const std::string& data) {
    uint64_t hash = 14695981039346656037ULL; // FNV 64-bit offset basis
    const uint64_t prime = 1099511628211ULL; // FNV 64-bit prime

    for (unsigned char c : data) {
        hash ^= static_cast<uint64_t>(c);
        hash *= prime;
    }

    //https://github.com/aappleby/smhasher/blob/master/src/MurmurHash3.cpp#L81
    // Avalanche mixing: ensures changing 1 character scrambles all 64 bits
    hash ^= hash >> 33;
    hash *= 0xff51afd7ed558ccdULL;
    hash ^= hash >> 33;
    hash *= 0xc4ceb9fe1a85ec53ULL;
    hash ^= hash >> 33;

    return hash;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_example_game_testing_utils_HashUtils_getHashForJSONObject(
        JNIEnv *env, jclass clazz,
        jstring jFileContent) {

    // 1. Convert Java jstring to C++ std::string
    const char* fileContent = env->GetStringUTFChars(jFileContent, nullptr);
    if (fileContent == nullptr) {
        return 0    ; // OutOfMemoryError already thrown by JVM
    }
    std::string fileString(fileContent);
    env->ReleaseStringUTFChars(jFileContent, fileContent); // Free JNI memory to avoid leaks

    // 2. Append secret salt (kept hidden inside the compiled .so library)
    const std::string SECRET_SALT = "SecretmcdSalt_PayMeInsteadOfHackingMaGame;-;";
    std::string dataToHash = fileString + SECRET_SALT;

    // 3. Generate 64bit hash value
    uint64_t rawHash = computeDeterministicHash64(dataToHash);

    // 4. Return the hash value back to Kotlin/Java
    return static_cast<jlong>(rawHash);
}