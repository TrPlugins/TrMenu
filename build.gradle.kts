import java.text.SimpleDateFormat

plugins {
    id("java")
    id("io.izzel.taboolib") version "1.12"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

group = "me.arasple.mc.trmenu"
version = "3.0-PRE-10"
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
            name("PlaceholderAPI")
            name("Vault")
            name("PlayerPoints")
            name("HeadDatabase")
            name("Oraxen")
            name("SkinsRestorer")
            name("ItemsAdder")
            name("floodgate-bukkit")
            name("FastScript")
        }
    }

    relocate("net.wesjd.anvilgui", "${project.group}.module.internal.inputer.anvil")

    version = "6.0.0-pre15"
}


repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    compileOnly("org.apache.commons:commons-lang3:3.12.0")
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly("ink.ptms.core:v11604:11604:all")
    compileOnly("ink.ptms.core:v11600:11600:all")
    compileOnly("ink.ptms.core:v11200:11200:all")
    compileOnly("io.izzel.taboolib:TabooLibKotlin:1.0.78")
    implementation("me.clip:placeholderapi:2.10.9")
    implementation("net.wesjd:anvilgui:1.4.0-SNAPSHOT")
    compileOnly(fileTree("libs"))
}