import trplugins.build.taboolib

plugins {
    kotlin("jvm")
    id("trplugins.build.publish")
}

val taboolibVersion: String by rootProject

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://repo.opencollab.dev/maven-snapshots//")
}

dependencies {
    implementation(project(":assist"))

    compileOnly(kotlin("stdlib"))
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-20210611.090701-17")
    compileOnly("ink.ptms.core:v11701:11701-minimize:universal")
    compileOnly("ink.ptms.core:v11600:11600-minimize")
    compileOnly("ink.ptms.core:v11200:11200-minimize")
    compileOnly("ink.ptms.core:v11400:11400-minimize")

    compileOnly("org.geysermc.floodgate:api:2.1.1-SNAPSHOT")

    taboolib("common", taboolibVersion)
    taboolib("module-nms", taboolibVersion)
    taboolib("platform-bukkit", taboolibVersion)
}