val taboolib_version: String by project

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    install("common", "common-5")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolib_version
}