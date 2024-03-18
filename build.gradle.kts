// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
    id("com.android.library") version "8.1.1" apply false
    id("com.android.application") version "8.1.1" apply false
    id("maven-publish")
}

apply(from = "./dependencies.gradle")
