# Maven Central Publishing Setup

This guide explains how to set up Maven Central publishing for your Kotlin Multiplatform library.

## Prerequisites

1. **Maven Central Account**: Sign up at [central.sonatype.com](https://central.sonatype.com/)
2. **GPG Key**: Create a GPG key for signing artifacts
3. **GitHub Repository**: Your code should be in a public GitHub repository

## Step 1: Create Maven Central Account

1. Go to [central.sonatype.com](https://central.sonatype.com/)
2. Sign up with your GitHub account
3. Verify your namespace (e.g., `io.github.yourusername`)

## Step 2: Generate GPG Key

```bash
# Generate a new GPG key
gpg --full-generate-key

# Choose RSA and RSA (default)
# Key size: 4096
# Expiration: 0 (never expires) or set your preferred expiration
# Enter your name and email (should match your GitHub account)

# List your keys to get the key ID
gpg --list-secret-keys --keyid-format LONG

# Export your public key (replace YOUR_KEY_ID with actual key ID)
gpg --armor --export YOUR_KEY_ID

# Export your private key
gpg --armor --export-secret-keys YOUR_KEY_ID
```

## Step 3: Upload Public Key to Key Servers

```bash
# Upload to multiple key servers
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
```

## Step 4: Set Up GitHub Secrets

In your GitHub repository, go to Settings > Secrets and variables > Actions, and add these secrets:

### Required Secrets:

1. **`MAVEN_CENTRAL_USERNAME`**: Your Central Portal username
2. **`MAVEN_CENTRAL_PASSWORD`**: Your Central Portal password
3. **`SIGNING_KEY_ID`**: Your GPG key ID (short format, last 8 characters)
4. **`SIGNING_PASSWORD`**: Your GPG key passphrase
5. **`GPG_KEY_CONTENTS`**: Your private GPG key (full ASCII-armored output)

### How to get these values:

```bash
# Get your GPG key ID (use the short format - last 8 characters)
gpg --list-secret-keys --keyid-format LONG

# For SIGNING_KEY_ID, use the last 8 characters of your key ID
# For example, if your key ID is ABCD1234EFGH5678, use EFGH5678

# For GPG_KEY_CONTENTS, export your private key
gpg --armor --export-secret-keys YOUR_KEY_ID
# Copy the entire output including -----BEGIN PGP PRIVATE KEY BLOCK----- and -----END PGP PRIVATE KEY BLOCK-----
```

## Step 5: Publishing

### Automatic Publishing (Recommended)

1. Create a new release on GitHub:
   - Go to your repository
   - Click "Releases" → "Create a new release"
   - Create a new tag (e.g., `v0.0.8`)
   - Add release notes
   - Click "Publish release"

2. The GitHub Action will automatically:
   - Build all platform variants
   - Sign the artifacts
   - Upload to Maven Central
   - Create the release

### Manual Publishing

You can also trigger publishing manually:

1. Go to Actions tab in your GitHub repository
2. Select "Publish to Maven Central" workflow
3. Click "Run workflow"
4. Enter the version number (e.g., `0.0.8`)
5. Click "Run workflow"

## Step 6: Verify Publication

1. Check the GitHub Actions logs for any errors
2. Visit [central.sonatype.com](https://central.sonatype.com/) and check your deployments
3. Your library should be available on Maven Central within 10-30 minutes
4. You can search for it at [search.maven.org](https://search.maven.org/)

## Using Your Published Library

Once published, users can add your library to their projects:

### Kotlin Multiplatform

```kotlin
// In your build.gradle.kts
dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:0.0.8")
}
```

### Android

```kotlin
// In your app's build.gradle.kts
dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:0.0.8")
}
```

## Troubleshooting

### Common Issues:

1. **GPG Signing Fails**: Ensure your GPG key is properly formatted and the passphrase is correct
2. **Namespace Verification**: Make sure your group ID matches your verified namespace on Central Portal
3. **Missing Sources/Javadoc**: The Vanniktech plugin automatically generates these
4. **Upload Timeout**: Large artifacts may take time to upload; be patient

### Debug Tips:

1. Check GitHub Actions logs for detailed error messages
2. Verify all secrets are correctly set
3. Test GPG signing locally first:
   ```bash
   echo "test" | gpg --clearsign --batch --yes --passphrase "YOUR_PASSPHRASE" --pinentry-mode loopback
   ```

## Security Notes

- Never commit your private GPG key or passwords to your repository
- Use GitHub Secrets for all sensitive information
- Regularly rotate your GPG keys and passwords
- Consider using a dedicated GPG key for CI/CD

## Support

- [Maven Central Documentation](https://central.sonatype.org/publish/publish-guide/)
- [Vanniktech Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin)
- [GPG Documentation](https://gnupg.org/documentation/) 