import trplugins.build.taboolib

plugins {
    kotlin("jvm")
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
    compileOnly("com.electronwill.night-config:core:3.6.5")
    taboolib("common", taboolibVersion)
    taboolib("common-5", taboolibVersion)
    taboolib("expansion-javascript", taboolibVersion)
    taboolib("module-chat", taboolibVersion)
    taboolib("module-lang", taboolibVersion)
    taboolib("module-configuration", taboolibVersion)
}