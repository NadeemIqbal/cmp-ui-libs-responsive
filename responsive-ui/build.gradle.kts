plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    id("com.eygraber.conventions-detekt")
    `maven-publish`
    signing
}

// Apply Maven Central configuration
apply(from = "../maven-central-config.gradle.kts")



group = "com.github.nadeemiqbal"
version = "0.0.6"

android {
    namespace = "com.nadeem.responsiveui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm("desktop")
    
    // Enable JavaScript platform
    js(IR) {
        browser()
        binaries.executable()
        useCommonJs()
    }

    // Enable WebAssembly (optional - can be enabled if needed)
    // @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    // wasmJs {
    //     browser()
    //     binaries.executable()
    // }

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
    }
}



publishing {
    repositories {
        maven {
            name = "staging"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
    
    publications.withType<MavenPublication> {
        pom {
            name.set("Responsive UI")
            description.set("A Kotlin Multiplatform Compose library that provides Flutter-like responsive layouts")
            url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")
            
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            
            developers {
                developer {
                    id.set("NadeemIqbal")
                    name.set("Nadeem Iqbal")
                    email.set("mr_nadeem_iqbal@yahoo.com")
                }
            }
            
            scm {
                connection.set("scm:git:git://github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
                developerConnection.set("scm:git:ssh://github.com/NadeemIqbal/cmp-ui-libs-responsive.git")
                url.set("https://github.com/NadeemIqbal/cmp-ui-libs-responsive")
            }
        }
    }
}
