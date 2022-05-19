plugins {
    id("io.izzel.taboolib")
    id("trplugins.build.publish")
}

val taboolibVersion: String by rootProject

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
    api(project(":api:common"))

    compileOnly("net.md-5:bungeecord-chat:1.18-R0.1-SNAPSHOT")
}