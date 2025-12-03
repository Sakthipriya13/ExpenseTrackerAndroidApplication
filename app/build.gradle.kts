plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.expensetrackerapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.expensetrackerapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        dataBinding = true
    }

    kapt {
        arguments {
            // This tells Room where to export the database schema
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Room runtime
    implementation (libs.androidx.room.runtime)

    // Room compiler (for annotation processing)
    kapt (libs.androidx.room.compiler.v261)   // for Kotlin
    // or
    annotationProcessor (libs.room.compiler)  // for Java

    // Room Kotlin Extensions and Coroutines support
    implementation (libs.androidx.room.ktx)

    // Optional: Testing Room database
    testImplementation (libs.androidx.room.testing)

    // Optional: Paging support with Room
    implementation (libs.androidx.room.paging)

    implementation("androidx.sqlite:sqlite-bundled:2.5.0")



}