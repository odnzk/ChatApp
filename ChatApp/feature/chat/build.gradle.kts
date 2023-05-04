plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.study.chat"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(mapOf("clearPackageData" to "true"))
        consumerProguardFiles("consumer-rules.pro")
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    // Architecture
    implementation(libs.bundles.elmslie)
    // DI
    implementation(libs.dagger2)
    kapt(libs.dagger2.compiler)
    // Additional
    implementation(libs.fragment.ktx)
    implementation(libs.pagination)
    implementation(libs.bundles.navigation)
    implementation(libs.timber)
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.test.core)
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

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:components"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
    implementation(project(":core:database"))
}
