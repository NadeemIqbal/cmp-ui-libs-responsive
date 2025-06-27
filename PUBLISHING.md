# Automated Publishing to Maven Central

This project supports **one-command automated publishing** to Maven Central using two approaches:

## 🚀 **Recommended: GitHub Actions (Automated)**

**Best for:** Production releases, team collaboration, reliable signing

### Setup (One-time)
1. Set up 4 GitHub secrets (see [`setup-github-secrets.md`](setup-github-secrets.md))
2. Push a version tag

### Usage
```bash
# Create and push a version tag
git tag v1.0.4
git push origin v1.0.4
```

**Benefits:**
- ✅ Reliable GPG signing (no local agent issues)
- ✅ Consistent Linux environment
- ✅ Automatic releases
- ✅ Full audit trail
- ✅ Works from any machine

## 🛠️ **Alternative: Local Publishing**

**Best for:** Development testing, quick local builds

### Setup
```bash
# Add to gradle.properties
centralPortalUsername=dQHYGU/e
centralPortalPassword=guP5PjsS10GpjIUl8anOoHF3xFuUgiTanWl0xDGDIe5I
```

### Usage
```bash
./gradlew publishToMavenCentral
```

**Note:** Local GPG signing may have issues on Windows. Use GitHub Actions for reliable production publishing.

## 📋 **What Gets Published**

- ✅ Android AAR + sources + javadoc
- ✅ Desktop JAR + sources + javadoc  
- ✅ WasmJS KLIB + sources + kotlin resources
- ✅ All artifacts GPG signed
- ✅ Complete Maven metadata

## 🔍 **Monitoring**

- **GitHub Actions:** Check Actions tab for workflow progress
- **Central Portal:** https://central.sonatype.com/publishing/deployments
- **Maven Central:** Library available after validation (10-30 minutes)

## 🎯 **Troubleshooting**

### Local GPG Issues
If you see "Missing signature" errors locally:
1. Use GitHub Actions instead (recommended)
2. Or fix GPG agent: `gpgconf --kill gpg-agent && gpgconf --launch gpg-agent`

### Validation Failures
- **Missing signatures:** Use GitHub Actions for reliable signing
- **Missing artifacts:** All platforms now generate sources/javadoc correctly
- **POM issues:** Metadata is automatically generated with required fields

## 📦 **Maven Coordinates**

```kotlin
implementation("io.github.nadeemiqbal:responsive-ui:1.0.4")
```

## 🎉 **Success!**

Your Kotlin Multiplatform library is now automatically published to Maven Central with:
- ✅ One-command publishing
- ✅ Proper GPG signatures
- ✅ Complete artifacts for all platforms
- ✅ Professional Maven metadata 