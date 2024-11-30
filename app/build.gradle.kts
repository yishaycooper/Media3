plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.media3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.media3"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Media3
//    implementation(libs.androidx.media3.exoplayer)
//    implementation(libs.androidx.media3.ui)
//    implementation(libs.androidx.media3.common)
//    implementation(libs.androidx.media3.exoplayer.hls)
//    implementation (libs.androidx.media3.session)



    implementation (libs.media3.exoplayer)
    implementation (libs.media3.ui)
    implementation (libs.androidx.media3.common)
    implementation (libs.androidx.media3.exoplayer.hls)
    implementation (libs.media3.session)
    implementation (libs.androidx.media)
    implementation(libs.androidx.media3.datasource.okhttp) // MediaItem
    implementation(libs.androidx.legacy.support.v4) // Needed MediaSessionCompat.Token

}