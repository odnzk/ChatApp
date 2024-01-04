plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.study.profile"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = libs.versions.kotlinCompileOptions.jvmTarget.get()
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)

    // Architecture
    implementation(libs.bundles.elmslie)
    // DI
    implementation(libs.dagger2)
    kapt(libs.dagger2.compiler)
    // Additional
    implementation(libs.coil)
    implementation(libs.fragment.ktx)
    implementation(libs.bundles.navigation)
    // Test
    testImplementation(libs.test.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.test.coroutines)

    implementation(project(":core:components"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
}
