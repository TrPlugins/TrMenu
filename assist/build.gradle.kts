import trplugins.menu.gradle.mavenConfigurate
import trplugins.menu.gradle.taboolib

plugins {
    kotlin("jvm")
    java
    `maven-publish`
}

val taboolibVersion: String by rootProject

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("com.google.code.gson:gson:2.8.7")
    taboolib("common", taboolibVersion)
    taboolib("common-5", taboolibVersion)
    taboolib("expansion-javascript", taboolibVersion)
    taboolib("module-chat", taboolibVersion)
    taboolib("module-lang", taboolibVersion)
    taboolib("module-configuration", taboolibVersion)
}

publishing(mavenConfigurate(artifactId = "assist"))