rootProject.name = "EntourageApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:ui")
include(":core:navigation")
include(":features:auth")
include(":features:projects")
include(":features:calculators")
include(":features:userprofile")
include(":features:rooms")
include(":features:projectdocuments")
include(":features:gallery")
include(":core:database")
include(":core:network")
include(":features:estimates")
