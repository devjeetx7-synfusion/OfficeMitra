import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) localPropertiesFile.inputStream().use { localProperties.load(it) }

fun configValue(name: String): String? =
    (project.findProperty(name) as String?)?.trim()
        ?: localProperties.getProperty(name)?.trim()
        ?: System.getenv(name)?.trim()

val supabaseUrl = configValue("SUPABASE_URL") ?: "https://pznasehldwiyunfqydcu.supabase.co"
val supabasePublishableKey = configValue("SUPABASE_PUBLISHABLE_KEY") ?: "sb_publishable_-jn0-Cf7Oq9OCuxKVLHDhg_uxS5t6oU"
require(supabaseUrl.isNotBlank() && !supabaseUrl.any { it.isWhitespace() } && supabaseUrl.startsWith("https://") && supabaseUrl.endsWith(".supabase.co")) { "SUPABASE_URL must be the HTTPS Supabase project base URL without spaces or API paths." }
require(supabasePublishableKey.startsWith("sb_publishable_")) { "SUPABASE_PUBLISHABLE_KEY must be a Supabase publishable key." }

android {
    namespace = "com.officemitra.app"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.officemitra.app"
        minSdk = 26
        targetSdk = 34
        versionCode = project.findProperty("VERSION_CODE")?.toString()?.toIntOrNull() ?: 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
        buildConfigField("String", "SUPABASE_URL", "\"${supabaseUrl}\"")
        buildConfigField("String", "SUPABASE_PUBLISHABLE_KEY", "\"${supabasePublishableKey}\"")
    }
    signingConfigs {
        create("release") {
            val ksFile = configValue("KEYSTORE_FILE")
            val ksPassword = configValue("KEYSTORE_PASSWORD")
            val alias = configValue("KEY_ALIAS")
            val keyPass = configValue("KEY_PASSWORD")
            if (ksFile != null && ksPassword != null && alias != null && keyPass != null) {
                storeFile = file(ksFile)
                storePassword = ksPassword
                keyAlias = alias
                keyPassword = keyPass
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            if (configValue("KEYSTORE_FILE") != null) signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true; buildConfig = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
    packaging { resources { excludes += "/META-INDEX/AL2.0"; excludes += "/META-INDEX/LGPL2.1" } }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil.compose)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.supabase.gotrue)
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.storage)
    implementation(libs.supabase.realtime)
    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}