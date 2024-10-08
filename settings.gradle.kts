pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "UDF"
include(":viewcontroller-core")
include(":store-rxjava")
include(":store-coroutines")
include(":viewcontroller-compose")
include(":viewcontroller-coroutines")
include(":viewcontroller-view-rxjava")