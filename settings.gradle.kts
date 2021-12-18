dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Jobby"
include(":app")
include(":app-companion")
include(":domain")
include(":infrastructure:data:datasource:network:findwork")
include(":infrastructure:data:repository")
include(":infrastructure:data:datasource:auth")
include(":logger")
include(":core")
include(":theme")
include(":presentation:features:login")
