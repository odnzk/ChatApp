pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
    }
}
rootProject.name = "ChatApp"
include(":app")
include(":core:ui")
include(":core:components")
include(":core:common")
include(":feature:chat")
include(":feature:users")
include(":feature:profile")
include(":feature:channels")
include(":feature:search")
include(":core:network")
include(":feature:auth")
