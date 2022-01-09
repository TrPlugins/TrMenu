import trplugins.menu.gradle.mavenConfigurate
import trplugins.menu.gradle.taboolib

plugins {
    kotlin("jvm")
    `maven-publish`
}

val taboolibVersion: String by rootProject

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
}

dependencies {
    implementation(project(":assist"))

    compileOnly(kotlin("stdlib"))
    compileOnly("net.md-5:bungeecord-chat:1.18-R0.1-SNAPSHOT")
    taboolib("common", taboolibVersion)
    taboolib("common-5", taboolibVersion)
    taboolib("expansion-javascript", taboolibVersion)
    taboolib("module-chat", taboolibVersion)
    taboolib("module-configuration", taboolibVersion)
}

tasks.create("sourceJar") {
    sourceSets.main
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
            artifactId = "action"

        }
    }
}