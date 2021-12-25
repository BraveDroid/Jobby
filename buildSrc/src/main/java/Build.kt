object Build {
     private const val androidBuildToolsVersion = "7.0.4"

    const val androidBuildTools = "com.android.tools.build:gradle:$androidBuildToolsVersion"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"

    const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"

}
