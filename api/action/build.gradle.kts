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

dependencies {
    api(project(":common"))

    compileOnly("net.md-5:bungeecord-chat:1.18-R0.1-SNAPSHOT")
}

tasks.tabooRelocateJar { onlyIf { false } }