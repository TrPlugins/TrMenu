import trmenu.gradle.mavenConfigurate
import trmenu.gradle.taboolib

plugins {
    kotlin("jvm")
    `maven-publish`
}

val taboolibVersion: String by rootProject

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
}

dependencies {
    implementation(project(":assist"))

    compileOnly(kotlin("stdlib"))
    compileOnly("net.md-5:bungeecord-chat:1.18-R0.1-SNAPSHOT")
    taboolib("common", taboolibVersion)
    taboolib("common-5", taboolibVersion)
    taboolib("expansion-javascript", taboolibVersion)
    taboolib("module-chat", taboolibVersion)
    taboolib("module-configuration", taboolibVersion)
}

publishing(mavenConfigurate(artifactId = "action"))