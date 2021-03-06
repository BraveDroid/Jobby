plugins {
    id("convention-kotlin-library-build")
    id("kotlinx-serialization")
}
dependencies{
    implementation(project(":domain"))
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.kotlinx_serialization_converter}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}")
}
