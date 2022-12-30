import Versions.taboolib_version

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    description {
        name(rootProject.name)
    }
    install("common")
    install("platform-bungee")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolib_version
}

dependencies {
    api(project(":project:common"))
}