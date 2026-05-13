@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("com.eygraber.conventions-detekt")
}

group = "io.github.nadeemiqbal"
version = "0.0.11"

android {
    namespace = "com.nadeem.responsiveui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    // Explicit hierarchy template — required because our manual `dependsOn`
    // for skikoTest below would otherwise disable the auto-applied default
    // (commonMain → appleMain → iosMain → iosArm64Main / iosSimulatorArm64Main).
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    jvm("desktop") {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    wasmJs {
        browser()
        nodejs()
    }

    // XCFramework assembly task: `./gradlew :responsive-ui:assembleResponsiveUIXCFramework`
    // produces a single .xcframework for Swift/ObjC consumers (SwiftPM / CocoaPods).
    val xcf = XCFramework("ResponsiveUI")
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ResponsiveUI"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
        }

        androidMain.dependencies {
            implementation(compose.ui)
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        // UI tests use compose.uiTest (Skiko-backed) which doesn't run on
        // Android plain JVM unit tests. Put UI tests in `skikoTest` so they
        // only run on Desktop / iOS / Wasm. Android unit tests stay focused
        // on pure-logic checks via `commonTest`. Android consumers still get
        // the same artifacts.
        val skikoTest by creating {
            dependsOn(commonTest.get())
            dependencies {
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }
        listOf("desktopTest", "iosArm64Test", "iosSimulatorArm64Test", "wasmJsTest").forEach { name ->
            named(name) { dependsOn(skikoTest) }
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = false)
    signAllPublications()

    coordinates(group.toString(), "responsive-ui", version.toString())

    pom {
        name.set("Responsive UI")
        description.set("A Kotlin Multiplatform Compose library that provides Flutter-like responsive layouts")
        inceptionYear.set("2024")
        url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("NadeemIqbal")
                name.set("Nadeem Iqbal")
                email.set("mr_nadeem_iqbal@yahoo.com")
                url.set("https://github.com/NadeemIqbal")
                organization.set("Nadeem Iqbal")
                organizationUrl.set("https://github.com/NadeemIqbal")
            }
        }

        scm {
            url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")
            connection.set("scm:git:git://github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
            developerConnection.set("scm:git:ssh://git@github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
        }
    }
}
