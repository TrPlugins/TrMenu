plugins {
    `maven-publish`
    id("java")
    id("io.izzel.taboolib") version "1.30"
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
}

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
        "module-database-mongodb",
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
            name("Score2")
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

    classifier = null
    version = "6.0.3-8"
}

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://jitpack.io")
}

dependencies {
    // Libraries
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
    compileOnly("ink.ptms:Zaphkiel:1.6.0")

    compileOnly(fileTree("libs"))
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.iroselle.com/repository/maven-releases/")
            credentials {
                username = project.findProperty("user").toString()
                password = project.findProperty("password").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = "me.arasple"
        }
    }
}
