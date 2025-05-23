plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" // Match your Kotlin version
}

android {
    namespace = "com.example.flashfeed"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flashfeed"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ✅ Add Navigation for Jetpack Compose
    implementation(libs.androidx.navigation.compose)

    // ✅ WebView Dependency
    implementation(libs.androidx.webkit)

    // Accompanist Pager for Reels UI
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // Coil for Image Loading
    implementation(libs.coil.compose)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime) // or your desired version
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation (libs.androidx.material.icons.core)
    implementation (libs.androidx.material.icons.extended)

    implementation(libs.material) // or the latest version
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.lottie)
    implementation(libs.lottie.compose) // Add this line

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.appcompat)


}