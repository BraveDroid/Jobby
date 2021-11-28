import utils.Factory
plugins {
    id("convention-app-build")
}

Factory.createHelloWord("from Build Gradle").also {
    println(it)
}

android {

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

configurations.all {
    resolutionStrategy { force("androidx.test:core:${Versions.test_core_ktx}") }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":infrastructure:data:datasource:network:findwork"))
    implementation(project(":infrastructure:data:repository"))

}
