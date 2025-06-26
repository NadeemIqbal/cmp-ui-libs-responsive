plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
}

android {
  namespace = "com.nadeem.responsiveui"
  compileSdk = 35
}

kotlin {
  androidTarget()

  js(IR) {
    browser()
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material)
      }
    }

    androidMain {
      dependencies {
        // Compose BOM ensures all versions are aligned
//        implementation(platform("androidx.compose:compose-bom:2025.05.00")) // New way
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.material:material")
        implementation("androidx.compose.foundation:foundation")
      }
    }

    jsMain {
      dependencies {
        implementation(compose.html.core)
      }
    }

    commonTest {
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
