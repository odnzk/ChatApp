plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.study.feature"
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

    implementation(libs.fragment.ktx)
    implementation(libs.retrofit)
    implementation(libs.pagination)
    implementation(libs.timber)

    implementation(libs.dagger2)
    kapt(libs.dagger2.compiler)

    implementation(libs.bundles.elmslie)
    implementation(libs.bundles.navigation)

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:components"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
    implementation(project(":core:database"))
}
