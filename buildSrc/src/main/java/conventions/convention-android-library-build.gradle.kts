import gradle.kotlin.dsl.accessors._0950e70c2749e1ef57eb981e05f5b097.implementation

plugins {
    `android-library`
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Versions.compileSdk
//    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }


        buildFeatures {
            dataBinding = true
            viewBinding = true
        }

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlin_coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlin_coroutines}")

    //nav
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.nav}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.nav}")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Versions.nav}")
    
    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-compiler:${Versions.hilt}")

    //dagger hilt test
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.hilt}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    //test
    //jvm-testsVersions
    testImplementation("androidx.arch.core:core-testing:${Versions.core_testing}")
    testImplementation("androidx.test:core-ktx:${Versions.test_core_ktx}")
    testImplementation("junit:junit:${Versions.junit}")
    testImplementation("com.google.truth:truth:${Versions.truth}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlin_coroutines}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlin_coroutines}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.mockito_kotlin}")
    testImplementation("org.mockito:mockito-core:${Versions.mockito_core}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.mockito:mockito-inline:${Versions.mockito_core}")
    testImplementation("androidx.room:room-testing:${Versions.room}")

}
