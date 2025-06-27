# GitHub Secrets Setup for Automated Publishing

To enable automated publishing to Maven Central via GitHub Actions, you need to set up the following secrets in your GitHub repository.

## 🔧 Required GitHub Secrets

Go to your GitHub repository → Settings → Secrets and variables → Actions → New repository secret

### 1. GPG_PRIVATE_KEY
Copy the ENTIRE GPG private key output from the command above:
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

lQdGBGhboFEBEAC04SbCjT3cqjhKNoSOuw6cYWu7UyMGN3JgktyftzZF837T6+8d
[... full key content ...]
=UCPR
-----END PGP PRIVATE KEY BLOCK-----
```

**Important:** Copy the ENTIRE output including the header and footer lines.

### 2. GPG_PASSPHRASE
Your GPG key passphrase (the password you set when creating the key)

### 3. CENTRAL_PORTAL_USERNAME
Your Central Portal username: `dQHYGU/e`

### 4. CENTRAL_PORTAL_PASSWORD  
Your Central Portal password: `guP5PjsS10GpjIUl8anOoHF3xFuUgiTanWl0xDGDIe5I`

## 🚀 How to Use

### Option 1: Manual Trigger
1. Go to Actions tab in your GitHub repository
2. Select "Publish to Maven Central" workflow
3. Click "Run workflow"
4. Enter the version number (e.g., `1.0.4`)
5. Click "Run workflow"

### Option 2: Git Tag (Recommended)
```bash
# Create and push a version tag
git tag v1.0.4
git push origin v1.0.4
```

This will automatically trigger the publishing workflow.

## 🔍 Monitoring

- Check the Actions tab for workflow progress
- Monitor Central Portal: https://central.sonatype.com/publishing/deployments
- Library will be available on Maven Central after validation (10-30 minutes)

## ✅ Benefits of GitHub Actions Approach

- ✅ Reliable GPG signing (no local agent issues)
- ✅ Consistent environment
- ✅ Automatic releases
- ✅ Full audit trail
- ✅ Works from any machine

## 🎯 What This Solves

The GitHub Actions approach eliminates the **12 "Missing signature" failures** you saw by:
- ✅ Using reliable in-memory GPG signing
- ✅ Consistent Linux environment (no Windows GPG agent issues)
- ✅ Proper key management through GitHub secrets
- ✅ All artifacts will be properly signed

## 🔧 Quick Setup Commands

```bash
# 1. The GPG key is already exported above - copy it to GitHub secrets
# 2. Set up the 4 GitHub secrets
# 3. Test the workflow
git tag v1.0.4
git push origin v1.0.4
```

## 📋 GitHub Secrets Summary

| Secret Name | Value |
|-------------|-------|
| `GPG_PRIVATE_KEY` | The entire GPG private key block (see above) |
| `GPG_PASSPHRASE` | Your GPG key password |
| `CENTRAL_PORTAL_USERNAME` | `dQHYGU/e` |
| `CENTRAL_PORTAL_PASSWORD` | `guP5PjsS10GpjIUl8anOoHF3xFuUgiTanWl0xDGDIe5I` |

## 🎯 Next Steps

1. **Set up the 4 GitHub secrets above**
2. **Test with:** `git tag v1.0.4 && git push origin v1.0.4`
3. **Monitor:** Actions tab and Central Portal
4. **Result:** Your library will be published automatically with proper GPG signatures!

**Expected outcome:** ✅ 0 failed validations, ✅ All artifacts signed, ✅ Published to Maven Central! 