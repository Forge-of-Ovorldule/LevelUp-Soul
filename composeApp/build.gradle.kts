import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "LevelUp-Soul"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "LevelUp-Soul"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "LevelUp-Soul.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting
        val androidMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx.v1120)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.json)
            implementation(libs.kotlinx.serialization.json)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bignum)
            implementation(libs.haze)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.kotlinx.serialization.json.v190)
        }
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.serialization.json.v180)
        }
    }
}

val keystoreProperties: Properties = Properties()
val keystoreFile: File = rootProject.file("composeApp/src/androidMain/keystore.properties")
if (keystoreFile.exists()) {
    FileInputStream(keystoreFile).use { keystoreProperties.load(it) }
}

val appVersionCode: Int = 10
val appVersionName: String = "1.2.0"

android {
    namespace = "fireforestsoul.levelupsoul"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    lint {
        checkReleaseBuilds = false
    }
    defaultConfig {
        applicationId = "fireforestsoul.levelupsoul"
        minSdk = libs.versions.android.minSdk.get().toInt()
        //noinspection OldTargetApi
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = appVersionCode
        versionName = appVersionName
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("src/androidMain/keystore.jks")
            storePassword = "xxx"
            keyAlias = "xxx"
            keyPassword = "xxx"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

base {
    archivesName.set("LevelUp-Soul-$appVersionName")
}

dependencies {
    debugImplementation(libs.ui.tooling)
}

compose.desktop {
    application {
        mainClass = "fireforestsoul.levelupsoul.MainKt"

        nativeDistributions {
            targetFormats(
                *when (org.gradle.internal.os.OperatingSystem.current()) {
                    org.gradle.internal.os.OperatingSystem.LINUX -> arrayOf(
                        TargetFormat.Deb,
                        TargetFormat.AppImage,
                    )

                    org.gradle.internal.os.OperatingSystem.MAC_OS -> arrayOf(
                        TargetFormat.Dmg,
                        TargetFormat.Pkg
                    )

                    else -> arrayOf( //Windows
                        TargetFormat.Msi,
                        TargetFormat.Exe
                    )
                }
            )

            packageName = "LevelUpSoul"
            packageVersion = "1.2.0"

            linux {
                iconFile.set(project.file("src/desktopMain/resources/app_icon.png"))
            }
            macOS {
                iconFile.set(project.file("src/desktopMain/resources/app_icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/app_icon.ico"))
            }
        }
    }
}

compose.resources {
    packageOfResClass = "fireforestsoul.levelupsoul"
}