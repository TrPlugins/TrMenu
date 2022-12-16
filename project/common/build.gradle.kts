val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    install("common", "common-5")
    install("platform-bukkit")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolibVersion
}

dependencies {
    api(project(":project:common-outside"))
    api(project(":project:module-invero"))
    api(project(":project:module-serialize"))
}