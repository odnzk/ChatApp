import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val username: String = gradleLocalProperties(rootDir).getProperty("username")
val password: String = gradleLocalProperties(rootDir).getProperty("password")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
}

android {
    namespace = "com.study.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "USERNAME", username)
            buildConfigField("String", "PASSWORD", password)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core)

    implementation(libs.retrofit)
    implementation(libs.okHttp)
    implementation(libs.httpLoggigInterceptor)
    implementation(libs.coroutines.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.gson)
}
