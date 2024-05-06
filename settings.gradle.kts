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
include(":store-rxjava")
include(":store-coroutines")
include(":viewcontroller-compose")
include(":viewcontroller-view-rxjava")
