val taboolib_version: String by project

plugins {
    id("io.izzel.taboolib") version "1.40"
}

taboolib {
    install("common", "common-5")
    options("skip-minimize", "keep-kotlin-module")
    classifier = null
    version = taboolib_version
}