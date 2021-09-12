plugins {
    `maven-publish`
    id("java")
    id("io.izzel.taboolib") version "1.26"
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "me.arasple.mc.trmenu"
version = "3.0-PRE-24"
description = "Modern & Advanced Menu-Plugin for Minecraft Servers"

taboolib {
    install(
        "common",
        "common-5",
        "module-kether",
        "module-ui",
        "module-ui-receptacle",
        "module-lang",
        "module-database",
        "module-metrics",
        "module-nms",
        "module-chat",
        "module-nms-util",
        "module-configuration",
        "platform-bukkit"
    )

    description {
        contributors {
            name("Arasple")
        }
        dependencies {
            name("PlaceholderAPI").optional(true)
            name("Vault").optional(true)
            name("PlayerPoints").optional(true)
            name("HeadDatabase").optional(true)
            name("Oraxen").optional(true)
            name("SkinsRestorer").optional(true)
            name("ItemsAdder").optional(true)
            name("floodgate-bukkit").optional(true)
            name("FastScript").optional(true)
        }
    }

    relocate("net.wesjd.anvilgui", "${project.group}.module.internal.inputer.anvil")

    classifier = null
    version = "6.0.2-1"
}

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://jitpack.io")
}

dependencies {
    // Libraries
    compileOnly("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    compileOnly("org.apache.commons:commons-lang3:3.12.0")

    // Server Core
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms.core:v11604:11604:all")
    compileOnly("ink.ptms.core:v11600:11600:all")
    compileOnly("ink.ptms.core:v11200:11200:all")

    // Hook Plugins
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")

    compileOnly(fileTree("libs"))
}

tasks.shadowJar {
    dependencies {
        taboolib.modules.forEach { exclude(dependency("io.izzel:taboolib:${taboolib.version}:$it")) }
        exclude(dependency("com.google.code.gson:gson:2.8.6"))
        exclude(dependency("org.bstats:bstats-bukkit:1.5"))

        exclude("data")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/maven")
        exclude("lang")
        exclude("menus")
        exclude("*.yml")
        exclude("plugin.yml.old")
        exclude("me/arasple/mc/trmenu/module/internal")
    }
    relocate("taboolib", "${project.group}.taboolib")
    archiveClassifier.set("pure")
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("shadow") {
            shadow.component(this)
            groupId = "me.arasple"
        }
    }
    repositories {
        maven {
            url = uri("https://repo.iroselle.com/repository/maven-releases/")
            credentials {
                file("access.txt").also {
                    if (!it.exists()) return@credentials
                }.readLines().apply {
                    username = this[0]
                    password = this[1]
                }
            }
        }
    }
}