plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.spotless)
  alias(libs.plugins.detekt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kapt)
  alias(libs.plugins.hilt)
  alias(libs.plugins.molecule)
}

android {
  namespace = "com.adjectivemonk2.compose"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.adjectivemonk2.compose"
    minSdk = 31
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(libs.core.ktx)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.activity.compose)
  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.ui.graphics)
  implementation(libs.ui.tooling.preview)
  implementation(libs.material3)
  implementation(libs.moshi)
  implementation(libs.material3.window.size)
  ksp(libs.moshi.codegen)
  implementation(libs.hilt)
  kapt(libs.hilt.compiler)
  implementation(libs.coil.compose)

  implementation(libs.okhttp)
  implementation(libs.retrofit)
  implementation(libs.retrofit.moshi)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
  androidTestImplementation(platform(libs.compose.bom))
  androidTestImplementation(libs.ui.test.junit4)

  debugImplementation(libs.ui.tooling)
  debugImplementation(libs.ui.test.manifest)
  debugImplementation(libs.leakCanary)
}

spotless {
  kotlin {
    targetExclude("build/generated/**/*.kt")
    ktlint(libs.versions.ktlint.get())
      .setEditorConfigPath("$rootDir/.editorconfig")
  }
}

detekt {
  allRules = true
  buildUponDefaultConfig = true
  config.setFrom("${rootDir}/detekt.yml")
  parallel = true
}

kapt {
  correctErrorTypes = true
}

kotlin {
  jvmToolchain(18)
}
