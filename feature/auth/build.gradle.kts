@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    id("kotlin-kapt")
}

android {
    namespace = "com.odnzk.auth"
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }
    kotlinOptions {
        jvmTarget = libs.versions.kotlinCompileOptions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)

    // Architecture
    implementation(libs.bundles.elmslie)
    // DI
    implementation(libs.dagger2)
    kapt(libs.dagger2.compiler)
    // Compose
    implementation(libs.bundles.compose)
    platform(libs.compose.bom)

    implementation(libs.test.mockk) // for mocking Screen's stores
    testImplementation(libs.junit)
    testImplementation(libs.test.core)
    testImplementation(libs.test.coroutines)

    implementation(project(":core:common"))
    implementation(project(":core:components"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
    implementation(project(":core:ui"))
    implementation(project(":core:ui-compose"))
}