import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val username: String = gradleLocalProperties(rootDir).getProperty("username")
val password: String = gradleLocalProperties(rootDir).getProperty("password")
val baseUrl: String = gradleLocalProperties(rootDir).getProperty("base_url")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.study.network"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    @Suppress("UnstableApiUsage")
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
            buildConfigField("String", "BASE_URL", baseUrl)
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

    // DI
    implementation(libs.dagger2)
    kapt(libs.dagger2.compiler)
    // Network
    implementation(libs.retrofit)
    implementation(libs.okHttp)
    implementation(libs.httpLoggigInterceptor)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.gson)
    implementation(libs.coil)
    // Additional
    implementation(libs.coroutines.core)
    implementation(libs.timber)

    implementation(project(":core:common"))
}
