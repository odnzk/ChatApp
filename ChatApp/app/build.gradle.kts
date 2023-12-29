import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val username: String = gradleLocalProperties(rootDir).getProperty("username")
val password: String = gradleLocalProperties(rootDir).getProperty("password")
val baseUrl: String = gradleLocalProperties(rootDir).getProperty("base_url")

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.study.tinkoff"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.study.tinkoff"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
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
        debug {
            buildConfigField("String", "USERNAME", username)
            buildConfigField("String", "PASSWORD", password)
            buildConfigField("String", "BASE_URL", baseUrl)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "11"
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

    implementation(libs.coroutines.android)
    implementation(libs.coil)
    implementation(libs.fragment.ktx)
    implementation(libs.timber)
    implementation(libs.dagger2)
    kapt(libs.dagger2.compiler)

    implementation(libs.bundles.navigation)
    implementation(libs.bundles.elmslie)

    implementation(project(":core:ui"))
    implementation(project(":core:components"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:auth"))
    implementation(project(":core:database"))
    implementation(project(":feature:channels"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:users"))
}
