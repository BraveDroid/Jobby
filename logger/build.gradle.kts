plugins {
    id("convention-android-library-build")
}

dependencies {
    implementation(project(":domain"))
    //log & debug
    implementation ("com.jakewharton.timber:timber:${Versions.timber}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}")
    implementation ("com.facebook.stetho:stetho:${Versions.stetho}")
    implementation ("com.facebook.stetho:stetho-okhttp3:${Versions.stethoOkhttp3}")
}
