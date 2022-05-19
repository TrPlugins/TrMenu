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

dependencies {
    compileOnly("com.google.code.gson:gson:2.8.7")
    compileOnly("com.electronwill.night-config:core:3.6.5")
}