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

    classifier = null
    version = taboolibVersion
}

dependencies {
    compileOnly("ink.ptms.core:v11903:11903:universal")

    rootProject
        .childProjects["project"]
        ?.childProjects
        ?.forEach { taboo(it.value) }
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}