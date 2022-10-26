val taboolib_version: String by project

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    description {
        name(rootProject.name)
    }
    install("common", "platform-bungee")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolib_version
}

dependencies {
    api(project(":project:common"))
}