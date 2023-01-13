val taboolibVersion: String by project
val pluginName: String = rootProject.name + "-Bungee"

plugins {
    id("io.izzel.taboolib") version ("1.53")
}

taboolib {
    install("common")
    install("platform-bungee")

    description {
        name = pluginName
        contributors { name("Arasple") }
    }

    classifier = null
    version = taboolibVersion
}

tasks.jar {
    archiveBaseName.set(pluginName)
}