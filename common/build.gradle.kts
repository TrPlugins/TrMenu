import java.util.UUID

val taboolibVersion: String by rootProject

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    description {
        name(rootProject.name)
    }
    install(
        "common",
        "common-5",
        "expansion-javascript",
        "module-chat",
        "module-lang",
        "module-configuration",
    )
    options(
        "skip-minimize",
        "keep-kotlin-module",
        "skip-taboolib-relocate",
    )
    classifier = null
    version = taboolibVersion
}

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.8.7")
    compileOnly("com.electronwill.night-config:core:3.6.5")
}
tasks.tabooRelocateJar { onlyIf { false } }