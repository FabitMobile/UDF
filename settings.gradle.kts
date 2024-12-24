pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "dagger.hilt.android.plugin" ->
                    useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
            }
        }
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
//include(":example-app")
