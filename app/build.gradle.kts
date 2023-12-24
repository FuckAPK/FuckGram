plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "org.baiyu.fuckgram"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.baiyu.fuckgram"
        minSdk = 26
        targetSdk = 34
        versionCode = 15
        versionName = "4.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    implementation("androidx.preference:preference-ktx:1.2.1")
}
