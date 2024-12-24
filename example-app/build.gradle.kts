plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.example.dagger.hilt.android.plugin)
    id("kotlin-kapt")
}

android {
    namespace = "ru.fabit.udf.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.fabit.udf.example"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {

    implementation(project(":viewcontroller-coroutines"))
    implementation(project(":viewcontroller-compose"))

    implementation(libs.example.hilt.android)
    implementation(libs.example.hilt.navigation)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.example.androidx.ui.tooling.preview)
    implementation(libs.example.androidx.material3)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit)
    debugImplementation(libs.example.androidx.ui.tooling)
    debugImplementation(libs.compose.ui.test)
    kapt(libs.example.hilt.android.compiler)
    implementation(libs.example.androidx.activity)
    implementation(libs.example.androidx.constraintlayout)

    implementation(libs.core.ktx)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}