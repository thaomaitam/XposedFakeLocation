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
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal {
            content {
                includeGroup("io.github.libxposed")
            }
        }
        maven { url = uri("https://api.xposed.info/") }
        maven { url = uri("https://repo.lsposed.org/") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "XposedFakeLocation"
include(":app")
include(":libxposed-compat")
