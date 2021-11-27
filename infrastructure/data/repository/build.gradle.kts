
plugins {
    id("convention-android-library-build")
}


dependencies {
    implementation(project(":domain"))
    implementation(project(":infrastructure:data:datasource:network:findwork"))
}
