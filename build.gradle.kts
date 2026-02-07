plugins {
    `maven-publish`
    id("hytale-mod") version "0.+"
}

group = "com.junior.evandro"
version = "0.3.0"

repositories {
    mavenCentral()
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
}

dependencies {

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }

    withSourcesJar()
}

hytale {
    addAssetsDependency = true
    decompilePartialOnly = true
}

tasks.named<ProcessResources>("processResources") {
    var replaceProperties = mapOf(
        "name" to project.name,
        "group" to project.group,
        "author" to findProperty("author"),
        "version" to project.version,
    )

    filesMatching("manifest.json") {
        expand(replaceProperties)
    }

    inputs.properties(replaceProperties)
}
