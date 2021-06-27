import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }

    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

sqldelight {
    database("Database") { // This will be the name of the generated database class.
        packageName = "at.sunilson.doistillneedthisthing"
        dialect = "sqlite:3.25"
    }
}

kapt {
    correctErrorTypes = true
}

kotlin {
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("com.michael-bull.kotlin-result:kotlin-result:1.1.11")
                api("com.michael-bull.kotlin-result:kotlin-result-coroutines:1.1.11")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.0")
                implementation("com.squareup.sqldelight:runtime:1.5.0")
                implementation("io.github.aakira:napier:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
                implementation("com.squareup.sqldelight:android-driver:1.5.0")
                implementation("com.google.android.material:material:1.3.0")
                implementation("com.google.dagger:hilt-android:$HILT_VERSION")
                configurations["kapt"].dependencies.add(project.dependencies.create("com.google.dagger:hilt-compiler:$HILT_VERSION"))
                configurations["kapt"].dependencies.add(project.dependencies.create("androidx.hilt:hilt-compiler:1.0.0"))
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:1.5.0")
            }
        }
        val iosTest by getting
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)