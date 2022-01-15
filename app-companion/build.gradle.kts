plugins {
    id("convention-app-build")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
    id("kotlin-android")
}

android {

    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
     }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":logger"))
    implementation(project(":infrastructure:data:datasource:auth"))
    implementation(project(":infrastructure:data:datasource:network:findwork"))
    implementation(project(":infrastructure:data:repository"))
    implementation(project(":presentation:features:login"))
    implementation(project(":theme"))

    //compose
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.activity:activity-compose:${Versions.activity_compose}")

    //appcompat
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("androidx.annotation:annotation:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    //compose test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")

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
