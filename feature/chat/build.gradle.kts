plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "com.study.chat"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
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
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
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
    implementation(libs.timber)
    // Test
    testImplementation(libs.junit)
    testImplementation(libs.test.core)
    androidTestImplementation(libs.test.mockk)
    androidTestImplementation(libs.junit.android)
    androidTestImplementation(libs.kaspresso)
    debugImplementation(libs.test.fragments)
    androidTestImplementation(libs.okhttp.mockserver)
    androidTestImplementation(libs.test.coroutines)
    androidTestImplementation(libs.test.runner)
    androidTestUtil(libs.test.orhestrator)
    androidTestImplementation(libs.room.ktx)
    androidTestImplementation(libs.room)
    androidTestImplementation(libs.room.pagination)
    androidTestImplementation(libs.pagination)
    kaptAndroidTest(libs.room.compiler)
    debugImplementation(libs.test.monitor)
    // Database
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)
    // Pagination
    implementation(libs.bundles.pagination)

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:components"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
}
