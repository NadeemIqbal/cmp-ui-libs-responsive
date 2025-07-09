// plugins {
//     alias(libs.plugins.kotlinMultiplatform)
//     alias(libs.plugins.androidLibrary)
//     alias(libs.plugins.composeCompiler)
//     alias(libs.plugins.composeMultiplatform)
//     alias(libs.plugins.vanniktech.mavenPublish)
//     id("com.eygraber.conventions-detekt")
// }

// group = "io.github.nadeemiqbal"
// version = "0.0.8"

// android {
//     namespace = "com.nadeem.responsiveui"
//     compileSdk = libs.versions.android.compileSdk.get().toInt()

//     defaultConfig {
//         minSdk = libs.versions.android.minSdk.get().toInt()
//     }

//     compileOptions {
//         sourceCompatibility = JavaVersion.VERSION_11
//         targetCompatibility = JavaVersion.VERSION_11
//     }
// }

// kotlin {
//     androidTarget {
//         publishLibraryVariants("release")
//         compilations.all {
//             compileTaskProvider.configure {
//                 compilerOptions {
//                     jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
//                 }
//             }
//         }
//     }

//     jvm("desktop") {
//         compilations.all {
//             compileTaskProvider.configure {
//                 compilerOptions {
//                     jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
//                 }
//             }
//         }
//     }

//     js(IR) {
//         browser()
//         nodejs()
//     }

//     // iOS targets - Commented out for Windows development
//     // Uncomment when building on macOS
//     /*
//     listOf(
//         iosX64(),
//         iosArm64(),
//         iosSimulatorArm64()
//     ).forEach { iosTarget ->
//         iosTarget.binaries.framework {
//             baseName = "ResponsiveUI"
//             isStatic = true
//         }
//     }
//     */

//     sourceSets {
//         commonMain {
//             dependencies {
//                 implementation(compose.runtime)
//                 implementation(compose.foundation)
//                 implementation(compose.material)
//                 implementation(compose.material3)
//             }
//         }

//         androidMain {
//             dependencies {
//                 implementation(compose.ui)
//                 implementation(compose.material)
//                 implementation(compose.material3)
//                 implementation(compose.foundation)
//             }
//         }

//         val desktopMain by getting {
//             dependencies {
//                 implementation(compose.desktop.currentOs)
//             }
//         }

//         val jsMain by getting {
//             dependencies {
//                 implementation(compose.html.core)
//             }
//         }

//         commonTest {
//             dependencies {
//                 implementation(libs.kotlin.test)
//             }
//         }
//     }
// }

// // mavenPublishing {
// //     publishToMavenCentral()

// //     // Only sign when signing keys are available (in CI/CD)
// //     if (project.hasProperty("signingInMemoryKeyId") ||
// //         System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyId") != null ||
// //         project.findProperty("RELEASE_SIGNING_ENABLED") == "true") {
// //         signAllPublications()
// //     }

// //     coordinates(group.toString(), "responsive-ui", version.toString())

// //     pom {
// //         name.set("Responsive UI")
// //         description.set("A Kotlin Multiplatform Compose library that provides Flutter-like responsive layouts")
// //         inceptionYear.set("2024")
// //         url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")

// //         licenses {
// //             license {
// //                 name.set("The Apache License, Version 2.0")
// //                 url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
// //                 distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
// //             }
// //         }

// //         developers {
// //             developer {
// //                 id.set("NadeemIqbal")
// //                 name.set("Nadeem Iqbal")
// //                 email.set("mr_nadeem_iqbal@yahoo.com")
// //                 url.set("https://github.com/NadeemIqbal")
// //             }
// //         }

// //         scm {
// //             url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")
// //             connection.set("scm:git:git://github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
// //             developerConnection.set("scm:git:ssh://git@github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
// //         }
// //     }
// // }



// mavenPublishing {
//     publishToMavenCentral()

//     // ALWAYS sign when publishing to Maven Central (the plugin handles the conditional logic)
//     signAllPublications()

//     // Alternative: Only sign when keys are available
//     // if (project.hasProperty("signingInMemoryKeyId") ||
//     //     project.hasProperty("signingInMemoryKey") ||
//     //     System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyId") != null ||
//     //     System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey") != null) {
//     //     signAllPublications()
//     // }

//     coordinates(group.toString(), "responsive-ui", version.toString())

//     pom {
//         name.set("Responsive UI")
//         description.set("A Kotlin Multiplatform Compose library that provides Flutter-like responsive layouts")
//         inceptionYear.set("2024")
//         url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")

//         licenses {
//             license {
//                 name.set("The Apache License, Version 2.0")
//                 url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                 distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//             }
//         }

//         developers {
//             developer {
//                 id.set("NadeemIqbal")
//                 name.set("Nadeem Iqbal")
//                 email.set("mr_nadeem_iqbal@yahoo.com")
//                 url.set("https://github.com/NadeemIqbal")
//             }
//         }

//         scm {
//             url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")
//             connection.set("scm:git:git://github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
//             developerConnection.set("scm:git:ssh://git@github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
//         }
//     }
// }




plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("com.eygraber.conventions-detekt")
}

// Opt-in for experimental wasmJs DSL
@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

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

    wasmJs {
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

    // Configure all targets for consistent publication
    targets.all {
        mavenPublication {
            // Ensure consistent artifact naming and publication setup
            // The vanniktech plugin will handle signing automatically
        }
    }

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

        val wasmJsMain by getting {
            dependencies {
                // Note: compose.html.core doesn't support wasmJs yet
                // Using basic compose dependencies available for wasmJs
                implementation(compose.runtime)
                implementation(compose.foundation)
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
    // Configure for Maven Central
    publishToMavenCentral(automaticRelease = false)

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

// Signing is handled automatically by the vanniktech plugin when credentials are available

// Debug task to troubleshoot signing issues
tasks.register("debugSigning") {
    doLast {
        println("=== Signing Configuration Debug ===")
        println("Has signingInMemoryKeyId property: ${project.hasProperty("signingInMemoryKeyId")}")
        println("Has signingInMemoryKey property: ${project.hasProperty("signingInMemoryKey")}")
        println("Has signingInMemoryKeyPassword property: ${project.hasProperty("signingInMemoryKeyPassword")}")

        println("\nEnvironment variables:")
        println("ORG_GRADLE_PROJECT_signingInMemoryKeyId: ${System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyId") != null}")
        println("ORG_GRADLE_PROJECT_signingInMemoryKey: ${System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey") != null}")
        println("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword: ${System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword") != null}")

        println("\nPublications:")
        publishing.publications.forEach { pub ->
            println("Publication: ${pub.name}")
            if (pub is MavenPublication) {
                println("  Group: ${pub.groupId}")
                println("  Artifact: ${pub.artifactId}")
                println("  Version: ${pub.version}")
            }
        }
    }
}
