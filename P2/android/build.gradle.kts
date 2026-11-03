plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.android"

    // Restored SDK 36.1 configuration required by :androidEngine
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.android"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        named("main") {
            val rootAssetsDir = rootProject.file("assets").absolutePath
            println("[GRADLE ASSETS] Registering source asset directory: $rootAssetsDir")
            assets.directories.add(rootAssetsDir)
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":androidEngine"))
    implementation(project(":engine"))
    implementation(project(":game"))
    implementation(project(":utils"))
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.play.services.ads)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    implementation("com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk:1.6.0")
}