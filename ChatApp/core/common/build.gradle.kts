plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
}
dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.dagger2)
}
