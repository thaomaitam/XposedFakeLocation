// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

tasks.register<Exec>("buildLibxposedApi") {
    group = "libxposed"
    description = "Builds libxposed/api and publishes to mavenLocal"
    workingDir = layout.projectDirectory.dir("libxposed/api").asFile
    commandLine(
        if (System.getProperty("os.name").lowercase().contains("windows")) "gradlew.bat" else "./gradlew",
        ":api:publishApiPublicationToMavenLocal",
        "-x",
        ":checks:compileKotlin",
        "--no-daemon",
    )
}

tasks.register<Exec>("buildLibxposedService") {
    group = "libxposed"
    description = "Builds libxposed/service and publishes to mavenLocal"
    workingDir = layout.projectDirectory.dir("libxposed/service").asFile
    commandLine(
        if (System.getProperty("os.name").lowercase().contains("windows")) "gradlew.bat" else "./gradlew",
        ":interface:publishInterfacePublicationToMavenLocal",
        ":service:publishServicePublicationToMavenLocal",
        "--no-daemon",
    )
}

tasks.register("buildLibxposed") {
    group = "libxposed"
    description = "Builds both libxposed/api and libxposed/service"
    dependsOn("buildLibxposedApi", "buildLibxposedService")
}