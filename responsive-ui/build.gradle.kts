plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
}

android {
  namespace = "com.nadeem.responsiveui"
}

kotlin {
  defaultKmpTargets(project)

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  jvm("desktop") {
    compilerOptions {
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
  }

  js(IR) {
    browser()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
      }
    }

    val androidMain by getting {
      dependencies {
        // Compose BOM ensures all versions are aligned
//        implementation(platform("androidx.compose:compose-bom:2025.05.00")) // New way
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.material:material")
        implementation("androidx.compose.foundation:foundation")
      }
    }

    val jsMain by getting {
      dependencies {
        implementation(compose.html.core)
      }
    }

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.common)
      }
    }

    val iosMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
  }
}

//compose {
////  html {
////    // You can add settings like outputDir, etc. here
////  }
//}
