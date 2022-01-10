import trplugins.build.taboolib

plugins {
    kotlin("jvm")
    java
    id("trplugins.build.publish")
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