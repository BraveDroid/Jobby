val androidBuildToolsVersion = "7.0.3"
val kotlinVersion = "1.5.21"

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
     `kotlin-dsl-precompiled-script-plugins`

}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:${androidBuildToolsVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
}
