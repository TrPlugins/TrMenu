import trplugins.menu.gradle.mavenConfigurate

val taboolibVersion: String by project

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.izzel.taboolib") version "1.34"
    `maven-publish`
}

description = "Modern & Advanced Menu-Plugin for Minecraft Servers"

taboolib {
    install(
        "common",
        "common-5",
        "expansion-javascript",
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
            name("Skulls").optional(true)
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
    relocate("trplugins.menu", group.toString().toLowerCase())

    classifier = null
    version = taboolibVersion
}

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    taboo(project(":action")) { isTransitive = false }
    taboo(project(":assist")) { isTransitive = false }

    // Libraries
    compileOnly(kotlin("stdlib"))
    compileOnly("org.apache.commons:commons-lang3:3.12.0")

    // Server Core
    compileOnly("ink.ptms.core:v11701:11701-minimize:mapped")
    compileOnly("ink.ptms.core:v11701:11701-minimize:universal")
    compileOnly("ink.ptms.core:v11604:11604")

    // Hook Plugins
    compileOnly("me.clip:placeholderapi:2.10.9") { isTransitive = false }
    compileOnly("ink.ptms:Zaphkiel:1.6.0") { isTransitive = false }
    compileOnly("ca.tweetzy:skulls:2.7.2") { isTransitive = false }
    compileOnly("net.skinsrestorer:skinsrestorer-api:14.1.10") { isTransitive = false }
    compileOnly("com.github.Th0rgal:Oraxen:-SNAPSHOT") { isTransitive = false }
    compileOnly("org.black_ixx:playerpoints:3.1.1") { isTransitive = false }
    compileOnly("com.github.MilkBowl:VaultAPI:-SNAPSHOT") { isTransitive = false }

    compileOnly(fileTree("libs"))
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.mcage.cn/repository/trplugins/")
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
            artifactId = rootProject.name.toLowerCase()
        }
    }
}