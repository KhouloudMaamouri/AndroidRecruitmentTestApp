pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Android RecruitmentTest App"
include(":app")
include(":data")
include(":core")
include(":domain")
include(":feature")
include(":feature:albums")
include(":feature:album-detail")
include(":database")
include(":domain:album")
include(":data:albums")
include(":core:database")
include(":core:designsystem")
include(":core:network")
include(":database:impl")
include(":core:ui")
include(":core:common")
include(":core:mvi")
include(":core:logger")
include(":core:analytics")
include(":data:favorites")
include(":domain:favs")
