// Maven Central Publishing Configuration
// This file contains the complete setup for publishing to Maven Central
// Keep this for future reference when GPG signing issues are resolved

// GPG signing is required for Maven Central
signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("GPG_PASSPHRASE")
    val signingKeyId = System.getenv("GPG_KEY_ID")
    
    if (signingKey != null && signingPassword != null) {
        println("✅ Using in-memory GPG signing with imported key")
        println("GPG Key ID: $signingKeyId")
        
        // Use in-memory signing to avoid GPG agent issues
        useInMemoryPgpKeys(signingKey, signingPassword)
        
        // Set the key ID if provided
        if (signingKeyId != null) {
            extra["signing.keyId"] = signingKeyId
        }
    } else {
        println("⚠️ Using GPG command line for local development")
        useGpgCmd()
    }
    sign(publishing.publications)
}

// Configure signing tasks
tasks.withType<Sign>().configureEach {
    val signingPassword = System.getenv("GPG_PASSPHRASE")
    val signingKeyId = System.getenv("GPG_KEY_ID")
    
    if (signingPassword != null) {
        doFirst {
            println("🔐 Signing ${this.name} with key: $signingKeyId")
        }
    }
}

// Create staging repository for bundle creation
val stagingDir = layout.buildDirectory.dir("staging-deploy")

// Task to create bundle for Central Portal
tasks.register<Zip>("createCentralPortalBundle") {
    dependsOn("publishAllPublicationsToStagingRepository")
    
    from(stagingDir)
    archiveFileName.set("central-bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("central-publishing"))
    
    doFirst {
        println("Creating Central Portal bundle with GPG signatures...")
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
        println("Uploading signed bundle to Central Portal...")
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
        println("✅ Library published to Maven Central with GPG signatures!")
        println("🔍 Monitor progress at: https://central.sonatype.com/publishing/deployments")
        println("🚀 Your library will be available on Maven Central after validation (usually 10-30 minutes)")
    }
} 