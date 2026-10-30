plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.android"
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
tasks.register<Copy>("copyAssets") {
    from(rootProject.file("assets"))
    into(layout.projectDirectory.dir("src/main/assets"))

    // Always execute, even if Gradle considers it up-to-date.
    outputs.upToDateWhen { false }
}

tasks.named("preBuild") {
    dependsOn("copyAssets")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}