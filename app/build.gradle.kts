import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

val baseUrl: String = gradleLocalProperties(rootDir).getProperty("base_url")

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.services)
    alias(libs.plugins.detekt)
    alias(libs.plugins.firebase.crashlutics.gradle.plugin)
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
            configure<CrashlyticsExtension> {
                nativeSymbolUploadEnabled = true
                strippedNativeLibsDir = "build/intermediates/stripped_native_libs/release/out/lib"
                unstrippedNativeLibsDir ="build/intermediates/merged_native_libs/release/out/lib"
            }
        }
        debug {
            buildConfigField("String", "BASE_URL", baseUrl)
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
    packaging {
        resources {
            excludes += "/META-INF/*"
        }
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

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

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
    implementation(project(":feature:auth"))
}
