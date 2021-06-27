plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "at.sunilson.doistillneedthisthing.androidApp"
        minSdk = 24
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-beta09"
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    aaptOptions {
        noCompress("tflite")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.room:room-runtime:2.3.0")
    annotationProcessor("androidx.room:room-compiler:2.3.0")

    val composeVersion = "1.0.0-beta09"
    val cameraxVersion = "1.1.0-alpha05"
    val orbitVersion = "3.1.1"

    implementation("androidx.work:work-runtime-ktx:2.7.0-alpha04")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0-beta02")
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation("androidx.activity:activity-compose:1.3.0-beta02")
    implementation("com.google.mlkit:object-detection:16.2.5")
    implementation("com.google.mlkit:image-labeling:17.0.4")
    implementation("com.google.mlkit:image-labeling-custom:16.3.1")
    implementation("com.google.dagger:hilt-android:$HILT_VERSION")
    kapt("com.google.dagger:hilt-compiler:$HILT_VERSION")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha08")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("com.google.accompanist:accompanist-insets:0.12.0")
    implementation("com.google.accompanist:accompanist-coil:0.12.0")
    implementation("com.google.accompanist:accompanist-permissions:0.12.0")
    implementation("com.google.android.material:compose-theme-adapter:$composeVersion")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha03")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.fragment:fragment-ktx:1.4.0-alpha03")
    implementation("com.airbnb.android:lottie-compose:1.0.0-beta07-1")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("dev.chrisbanes.insetter:insetter:0.5.0")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:1.0.0-alpha25")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    implementation("org.orbit-mvi:orbit-core:$orbitVersion")
    implementation("org.orbit-mvi:orbit-viewmodel:$orbitVersion")
    testImplementation("org.orbit-mvi:orbit-test:$orbitVersion")
    implementation("com.github.sunilson:android-kotlin-extensions:0.54")
}