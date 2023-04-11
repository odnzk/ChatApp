plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.study.tinkoff"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.study.tinkoff"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.coroutines.android)
    implementation(libs.fragment.ktx)
    debugImplementation(libs.leak.canary)
    implementation(libs.timber)

    implementation(libs.bundles.navigation)
    implementation(libs.bundles.elmslie)

    implementation(project(":core:ui"))
    implementation(project(":core:components"))
    implementation(project(":core:common"))
    implementation(project(":feature:channels"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:search"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:users"))
}
