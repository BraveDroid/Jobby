plugins {
    kotlin("jvm")
    `java-library`
}

dependencies {
//kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlin_coroutines}")
    implementation("javax.inject:javax.inject:1")

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
