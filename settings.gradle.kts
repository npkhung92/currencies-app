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

rootProject.name = "CurrencyApplication"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":datasource")
include(":currency:currency-data")
include(":currency:currency-presentation")
include(":currency:currency-domain")
include(":currency:currency-ui")
include(":core:core-presentation")
include(":core:core-ui")
include(":core:core-data")
