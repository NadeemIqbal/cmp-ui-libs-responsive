@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.nadeemiqbal"
version = "1.0.0"

android {
    namespace = "com.nadeem.responsiveui.testing"
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

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ResponsiveUITesting"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":responsive-ui"))
            implementation(compose.runtime)
            @OptIn(ExperimentalComposeLibrary::class)
            api(compose.uiTest)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(compose.material3)
        }
        val skikoTest by creating { dependsOn(commonTest.get()) }
        listOf("desktopTest", "iosArm64Test", "iosSimulatorArm64Test", "wasmJsTest").forEach { name ->
            named(name) { dependsOn(skikoTest) }
        }
    }
}

mavenPublishing {
    signAllPublications()
    publishToMavenCentral(automaticRelease = false)

    coordinates(group.toString(), "responsive-ui-testing", version.toString())

    pom {
        name.set("Responsive UI — Testing")
        description.set("Test helpers for io.github.nadeemiqbal:responsive-ui — inject a deterministic screen width into Compose UI tests.")
        inceptionYear.set("2026")
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
