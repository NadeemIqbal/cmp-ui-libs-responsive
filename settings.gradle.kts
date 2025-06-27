import com.eygraber.conventions.Env
import com.eygraber.conventions.repositories.addCommonRepositories

pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-nodejs/maven")
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-nodejs/maven")
    maven("https://jitpack.io") // Added for remote dependency
  }
}

plugins {
  id("com.eygraber.conventions.settings") version "0.0.86"
  id("com.gradle.develocity") version "4.0.2"
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}


rootProject.name = "responsive-ui-sample"
include(":responsive-ui")
include(":example")
include(":example-remote")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

develocity {
  buildScan {
    termsOfUseUrl = "https://gradle.com/terms-of-service"
    publishing.onlyIf { Env.isCI }
    if(Env.isCI) {
      termsOfUseAgree = "yes"
    }
  }
}
