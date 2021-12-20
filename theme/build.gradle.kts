plugins {
    id("convention-android-library-build")
    id("kotlin-android")
}
dependencies {
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
}
