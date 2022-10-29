val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    description {
        name(rootProject.name)
    }
    install("common", "platform-bukkit")
    install("module-nms")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolibVersion
}

dependencies {
    api(project(":project:module-invero-legacy"))
}