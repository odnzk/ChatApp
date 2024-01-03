import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val baseUrl: String = gradleLocalProperties(rootDir).getProperty("base_url")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.study.network"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
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
            buildConfigField("String", "BASE_URL", baseUrl)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.kotlinCompileOptions.jvmTarget.get()
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
    implementation(project(":core:auth"))
}
