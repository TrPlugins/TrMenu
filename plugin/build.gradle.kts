import Versions.taboolib_version

plugins {
    id("io.izzel.taboolib")
}

taboolib {
    install(
        "common",
        "common-5",
        "module-configuration",
        "module-nms",
        "module-nms-util",
        "module-kether",
        "platform-bukkit",
        "platform-bungee",
    )

    classifier = null
    version = taboolib_version
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
}

dependencies {
    rootProject.subprojects
        .map { it.path }
        .filter { it.startsWith(":project:") }
        .forEach {
            taboo(project(it))
        }
}

/*tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
}

tasks.jar {
    archiveFileName.set(rootProject.name)
}*/
/*

gradle.buildFinished {
    File(buildDir, "libs").listFiles().first().let {
        it.copyTo(File("F:\\Testing\\Purpur 1.19.2\\plugins\\${it.name}"), true)
    }
}*/
