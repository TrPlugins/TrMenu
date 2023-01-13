val taboolibVersion: String by project

plugins {
    id("io.izzel.taboolib") version ("1.53")
}

taboolib {
    install("common")
    install("module-nms")
    install("platform-bukkit")

    description {
        name = rootProject.name

        contributors {
            name("Arasple")
            name("Score2")
        }
    }

    relocate("cc.trixey.invero", "$group.invero")

    classifier = null
    version = taboolibVersion
}

dependencies {
    taboo("cc.trixey.invero:framework-bukkit:1.0.0")
    taboo("cc.trixey.invero:framework-common:1.0.0")

    rootProject
        .childProjects["project"]
        ?.childProjects
        ?.forEach { taboo(it.value) }
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}