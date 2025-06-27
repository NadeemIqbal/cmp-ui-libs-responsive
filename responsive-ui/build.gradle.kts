import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.util.Base64

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    id("com.eygraber.conventions-detekt")
    `maven-publish`
    signing
}

group = "io.github.nadeemiqbal"
version = "1.0.3"

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

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
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
    }
}

// Create staging repository for bundle creation
val stagingDir = layout.buildDirectory.dir("staging-deploy")

publishing {
    publications.withType<MavenPublication> {
        groupId = "io.github.nadeemiqbal"
        version = "1.0.10" // Version with Central Portal signing
        
        pom {
            name.set("Responsive UI")
            description.set("A Kotlin Multiplatform Compose library that provides Flutter-like responsive layouts")
            url.set("https://github.com/NadeemIqbal/responsive-ui")
            
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
                connection.set("scm:git:git://github.com/NadeemIqbal/responsive-ui.git")
                developerConnection.set("scm:git:ssh://github.com/NadeemIqbal/responsive-ui.git")
                url.set("https://github.com/NadeemIqbal/responsive-ui")
            }
        }
    }
    
    repositories {
        maven {
            name = "Staging"
            url = stagingDir.get().asFile.toURI()
        }
    }
}

// Let Central Portal handle GPG signing automatically
// This ensures signatures are valid and trusted by Maven Central
// signing {
//     val signingKey = System.getenv("GPG_PRIVATE_KEY")
//     val signingPassword = System.getenv("GPG_PASSPHRASE")
//     val signingKeyId = System.getenv("GPG_KEY_ID")
//     
//     if (signingKey != null && signingPassword != null) {
//         println("Using GPG command line signing (key imported in workflow)")
//         println("GPG Key ID: $signingKeyId")
//         
//         // Use GPG command since we imported the key in the workflow
//         useGpgCmd()
//         
//         // Configure GPG signing with passphrase and key ID
//         extra["signing.gnupg.passphrase"] = signingPassword
//         if (signingKeyId != null) {
//             extra["signing.gnupg.keyName"] = signingKeyId
//         }
//     } else {
//         println("Using GPG command line for local development")
//         useGpgCmd()
//     }
//     sign(publishing.publications)
// }

// Signing tasks disabled - Central Portal handles signing
// tasks.withType<Sign>().configureEach {
//     val signingPassword = System.getenv("GPG_PASSPHRASE")
//     val signingKeyId = System.getenv("GPG_KEY_ID")
//     
//     if (signingPassword != null) {
//         doFirst {
//             println("Configuring signing task: ${this.name}")
//             println("Using GPG Key ID: $signingKeyId")
//         }
//     }
// }

// Task to create bundle for Central Portal
tasks.register<Zip>("createCentralPortalBundle") {
    dependsOn("publishAllPublicationsToStagingRepository")
    
    from(stagingDir)
    archiveFileName.set("central-bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("central-publishing"))
    
    doFirst {
        println("Creating Central Portal bundle (unsigned - Central Portal will sign)...")
    }
    
    doLast {
        println("Bundle created: ${archiveFile.get().asFile.absolutePath}")
    }
}

// Upload bundle to Central Portal
tasks.register<Exec>("uploadToCentralPortal") {
    dependsOn("createCentralPortalBundle")
    
    val bundleFile = layout.buildDirectory.file("central-publishing/central-bundle.zip")
    val username = findProperty("centralPortalUsername") as String? ?: System.getenv("CENTRAL_PORTAL_USERNAME")
    val password = findProperty("centralPortalPassword") as String? ?: System.getenv("CENTRAL_PORTAL_PASSWORD")
    
    if (username == null || password == null) {
        throw GradleException("Central Portal credentials not found. Set centralPortalUsername and centralPortalPassword in gradle.properties")
    }
    
    val credentials = "$username:$password".toByteArray().let { 
        Base64.getEncoder().encodeToString(it)
    }
    
    // Use curl to upload the bundle
    commandLine(
        "curl", "-X", "POST",
        "--header", "Authorization: Bearer $credentials",
        "--form", "bundle=@${bundleFile.get().asFile.absolutePath}",
        "--form", "publishingType=AUTOMATIC",
        "https://central.sonatype.com/api/v1/publisher/upload"
    )
    
    doFirst {
        println("Uploading unsigned bundle to Central Portal for automatic signing...")
        println("Bundle: ${bundleFile.get().asFile.absolutePath}")
    }
    
    doLast {
        println("Upload completed! Check status at: https://central.sonatype.com/publishing/deployments")
    }
}

// Main automated publishing task
tasks.register("publishToMavenCentral") {
    dependsOn("uploadToCentralPortal")
    description = "Publishes the library to Maven Central automatically via Central Portal with GPG signing"
    group = "publishing"
    
    doLast {
        println("✅ Library v1.0.10 published to Maven Central with Central Portal signing!")
        println("🔍 Monitor progress at: https://central.sonatype.com/publishing/deployments")
        println("🚀 Your library will be available on Maven Central after validation (usually 10-30 minutes)")
    }
}
