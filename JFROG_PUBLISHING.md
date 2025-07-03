# JFrog Artifactory Publishing Guide

This guide explains how to publish your Kotlin Multiplatform library to JFrog Artifactory.

## 🚀 **Quick Start**

### 1. Set Up JFrog Artifactory Credentials

#### Option A: Environment Variables (Recommended for CI/CD)
```bash
export JFROG_ARTIFACTORY_URL="https://your-company.jfrog.io/artifactory/your-repo"
export JFROG_USERNAME="your-username"
export JFROG_PASSWORD="your-api-key-or-password"
```

#### Option B: Local gradle.properties
Add to your local `gradle.properties` file:
```properties
jfrog.username=your-username
jfrog.password=your-api-key-or-password
```

### 2. Update Repository URL
Replace `https://your-company.jfrog.io/artifactory/your-repo` in the configuration with your actual JFrog Artifactory repository URL.

### 3. Publish to JFrog Artifactory
```bash
# Publish to JFrog Artifactory
./gradlew publishToJFrogArtifactory
```

## 🔧 **Configuration Options**

### JFrog Artifactory Types

#### 1. **JFrog Platform Cloud** (Recommended)
- URL format: `https://your-company.jfrog.io/artifactory/your-repo`
- Modern cloud solution
- Best for new projects

#### 2. **On-Premise JFrog Artifactory**
- URL format: `https://your-domain.com/artifactory/your-repo`
- For organizations with self-hosted Artifactory

#### 3. **JFrog Artifactory OSS**
- Free version with basic features
- Good for open-source projects

### Repository Types
- **Maven Repository**: For general Maven artifacts
- **Gradle Repository**: Optimized for Gradle metadata
- **Generic Repository**: For any file types

## 📋 **What Gets Published**

The following artifacts will be published to JFrog Artifactory:

- ✅ **Android AAR** + sources + javadoc
- ✅ **Desktop JAR** + sources + javadoc  
- ✅ **JavaScript KLIB** + sources + kotlin resources
- ✅ **All artifacts GPG signed** (if signing is enabled)
- ✅ **Complete Maven metadata**

## 🎯 **Usage Examples**

### Consuming from JFrog Artifactory

#### In build.gradle.kts (Kotlin DSL)
```kotlin
repositories {
    maven {
        url = uri("https://your-company.jfrog.io/artifactory/your-repo")
        credentials {
            username = "your-username"
            password = "your-password"
        }
    }
}

dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:0.0.7")
}
```

#### In build.gradle (Groovy DSL)
```groovy
repositories {
    maven {
        url 'https://your-company.jfrog.io/artifactory/your-repo'
        credentials {
            username 'your-username'
            password 'your-password'
        }
    }
}

dependencies {
    implementation 'io.github.nadeemiqbal:responsive-ui:0.0.7'
}
```

## 🔒 **Security Best Practices**

### API Keys vs Passwords
- **Recommended**: Use JFrog API keys instead of passwords
- Generate API keys in JFrog Artifactory UI: User Menu → Edit Profile → API Key

### Environment Variables
- Never commit credentials to version control
- Use environment variables or secure credential storage
- For CI/CD, use secure environment variables

### Gradle Properties
- Add `gradle.properties` to `.gitignore`
- Use different credentials for different environments

## 🚦 **CI/CD Integration**

### GitHub Actions Example
```yaml
- name: Publish to JFrog Artifactory
  env:
    JFROG_ARTIFACTORY_URL: ${{ secrets.JFROG_ARTIFACTORY_URL }}
    JFROG_USERNAME: ${{ secrets.JFROG_USERNAME }}
    JFROG_PASSWORD: ${{ secrets.JFROG_PASSWORD }}
  run: ./gradlew publishToJFrogArtifactory
```

### GitLab CI Example
```yaml
publish_jfrog:
  script:
    - ./gradlew publishToJFrogArtifactory
  variables:
    JFROG_ARTIFACTORY_URL: $JFROG_ARTIFACTORY_URL
    JFROG_USERNAME: $JFROG_USERNAME
    JFROG_PASSWORD: $JFROG_PASSWORD
```

## 🔍 **Verification**

After publishing, verify the artifacts in your JFrog Artifactory:

1. **Web UI**: Go to your JFrog Artifactory URL
2. **Navigate**: Artifacts → Browse → your-repo → io → github → nadeemiqbal → responsive-ui
3. **Check**: All platform artifacts and metadata are present

## 🎉 **Available Gradle Tasks**

| Task | Description |
|------|-------------|
| `publishToJFrogArtifactory` | Publish to JFrog Artifactory |
| `publishAllPublicationsToJFrogArtifactoryRepository` | Low-level JFrog publishing task |

## 🛠️ **Troubleshooting**

### Common Issues

#### 1. **Authentication Failed**
```
* What went wrong: Could not publish publication 'maven' to repository 'JFrogArtifactory'
```
**Solution**: Verify your credentials and repository URL

#### 2. **Repository Not Found**
```
* What went wrong: Could not HEAD 'https://your-company.jfrog.io/artifactory/your-repo'
```
**Solution**: Check repository name and permissions

#### 3. **Missing Artifacts**
**Solution**: Ensure all target platforms are properly configured

### Debug Mode
```bash
./gradlew publishToJFrogArtifactory --info --stacktrace
```

## 📞 **Support**

- **JFrog Documentation**: https://www.jfrog.com/confluence/
- **JFrog Community**: https://jfrog.com/community/
- **Gradle Publishing Guide**: https://docs.gradle.org/current/userguide/publishing_maven.html 