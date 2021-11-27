dependencyResolutionManagement {
 repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
 repositories {
     google()
     mavenCentral()
 }
}
rootProject.name = "Jobby"
include ("app")
include ("domain")
include ("infrastructure:data:datasource:network:findwork")
