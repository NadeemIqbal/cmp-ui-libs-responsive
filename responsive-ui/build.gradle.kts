plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("com.eygraber.conventions-detekt")
}

group = "io.github.nadeemiqbal"
version = "0.0.8"

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
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
                }
            }
        }
    }

    jvm("desktop") {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
                }
            }
        }
    }
    
    js(IR) {
        browser()
        nodejs()
    }

    // iOS targets - Commented out for Windows development
    // Uncomment when building on macOS
    /*
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ResponsiveUI"
            isStatic = true
        }
    }
    */

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
            }
        }
        
        androidMain {
            dependencies {
                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.foundation)
            }
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

mavenPublishing {
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
            }
        }
        
        scm {
            url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")
            connection.set("scm:git:git://github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
            developerConnection.set("scm:git:ssh://git@github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
        }
    }
}
