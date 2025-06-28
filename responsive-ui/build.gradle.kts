plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    id("com.eygraber.conventions-detekt")
    `maven-publish`
    signing
}

// Maven Central publishing configuration
val stagingDir = layout.buildDirectory.dir("staging-deploy")

// GPG signing configuration
signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY_GRADLE") ?: System.getenv("GPG_PRIVATE_KEY")?.replace("\\\\n", "\n")
    val signingPassword = System.getenv("GPG_PASSPHRASE")
    val signingKeyId = System.getenv("GPG_KEY_ID")
    
    if (signingKey != null && signingPassword != null) {
        println("✅ Using in-memory GPG signing with key ID: $signingKeyId")
        if (signingKeyId != null) {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        } else {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
    } else {
        println("⚠️ Using GPG command line for local development")
        useGpgCmd()
    }
    sign(publishing.publications)
}

// Task to create bundle for Central Portal
tasks.register<Zip>("createCentralPortalBundle") {
    dependsOn("publishAllPublicationsToStagingRepository")
    
    from(stagingDir)
    archiveFileName.set("central-bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("central-publishing"))
    
    doLast {
        println("Bundle created: ${archiveFile.get().asFile.absolutePath}")
    }
}

// Upload bundle to Central Portal using curl
tasks.register<Exec>("uploadToCentralPortal") {
    dependsOn("createCentralPortalBundle")
    
    val bundleFile = layout.buildDirectory.file("central-publishing/central-bundle.zip")
    val username = System.getenv("CENTRAL_PORTAL_USERNAME")
    val password = System.getenv("CENTRAL_PORTAL_PASSWORD")
    
    if (username == null || password == null) {
        throw GradleException("Central Portal credentials not found")
    }
    
    // Use curl with basic auth directly instead of manually encoding
    commandLine(
        "curl", "-X", "POST",
        "--user", "$username:$password",
        "--form", "bundle=@${bundleFile.get().asFile.absolutePath}",
        "--form", "publishingType=AUTOMATIC",
        "https://central.sonatype.com/api/v1/publisher/upload"
    )
    
    doLast {
        println("Upload completed! Check status at: https://central.sonatype.com/publishing/deployments")
    }
}

// Main automated publishing task
tasks.register("publishToMavenCentral") {
    dependsOn("uploadToCentralPortal")
    description = "Publishes the library to Maven Central automatically"
    group = "publishing"
    
    doLast {
        println("✅ Library published to Maven Central!")
    }
}



group = "io.github.nadeemiqbal"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm("desktop") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
    }
    
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
        // Add Javadoc jar for all publications
        val javadocJar = tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            // Create empty javadoc jar as placeholder
            from(layout.buildDirectory.dir("docs/javadoc"))
        }
        artifact(javadocJar)
        
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
