val taboolibVersion: String by project

plugins {
    id("org.gradle.java")
    id("org.gradle.maven-publish")
    kotlin("jvm") version "1.6.21" apply false
    id("io.izzel.taboolib") version "1.40" apply false
}

description = "Modern & Advanced Menu-Plugin for Minecraft Servers"

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://jitpack.io")
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://repo.tabooproject.org/repository/releases")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
    }
    dependencies {
        "compileOnly"(kotlin("stdlib"))
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}