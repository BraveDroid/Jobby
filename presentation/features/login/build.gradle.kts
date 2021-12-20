
plugins {
    id("convention-android-library-build")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
}

android {

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies{
    implementation(project(":theme"))
    implementation(project(":domain"))

    implementation("androidx.annotation:annotation:1.2.0")

    //androidx
    implementation("androidx.core:core-ktx:${Versions.core_ktx}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
//android core
    implementation ("androidx.fragment:fragment-ktx:1.3.2")
    implementation ("androidx.fragment:fragment:1.3.2")


    //lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}")

    //ui
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

}
