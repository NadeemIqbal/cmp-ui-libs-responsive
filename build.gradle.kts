import com.eygraber.conventions.tasks.deleteRootBuildDirWhenCleaning
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
  dependencies {
    classpath(libs.buildscript.android)
    classpath(libs.buildscript.androidCacheFix)
    classpath(libs.buildscript.compose.compiler)
    classpath(libs.buildscript.compose.jetbrains)
    classpath(libs.buildscript.detekt)
    classpath(libs.buildscript.dokka)
    classpath(libs.buildscript.kotlin)
    classpath(libs.buildscript.publish)
  }
}

plugins {
  base
  alias(libs.plugins.conventions)
  alias(libs.plugins.kotlinx.binaryCompatibilityValidator)

  // this is necessary to avoid the plugins to be loaded multiple times
  // in each subproject's classloader
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.composeMultiplatform) apply false
  alias(libs.plugins.composeCompiler) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.vanniktech.mavenPublish) apply false
}

apiValidation {
  // Only validate the library, not the sample app.
  ignoredProjects += listOf("example")
  // Klib (Wasm/iOS) ABI dumps are configured under KLib block.
  @OptIn(kotlinx.validation.ExperimentalBCVApi::class)
  klib {
    enabled = true
  }
}

deleteRootBuildDirWhenCleaning()

gradleConventionsDefaults {
  android {
    sdkVersions(
      compileSdk = libs.versions.android.compileSdk,
      targetSdk = libs.versions.android.targetSdk,
      minSdk = libs.versions.android.minSdk,
    )
  }

  detekt {
    plugins(
      libs.detektEygraber.formatting,
      libs.detektEygraber.style,
    )
  }

  kotlin {
    jvmTargetVersion = JvmTarget.JVM_11
    explicitApiMode = ExplicitApiMode.Strict
  }
}

gradleConventionsKmpDefaults {
  webOptions = webOptions.copy(
    isNodeEnabled = false,
    isBrowserEnabled = true,
  )

  targets(
    KmpTarget.Android,
    KmpTarget.Ios,
    KmpTarget.Jvm,
    KmpTarget.WasmJs,
  )
}

//tasks.register("clean", Delete::class) {
//  delete(rootProject.buildDir)
//}
