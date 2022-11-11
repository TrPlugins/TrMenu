val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib")
}

dependencies {
    api(project(":project:common"))
    api(project(":project:implementation-bukkit"))
}

taboolib {
    description {
        name(rootProject.name)
    }
    install("common")
    install("platform-bukkit")
    install("module-nms")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolibVersion
}