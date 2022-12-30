plugins {
    java
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("io.izzel.taboolib") version "1.51" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven("https://repo.tabooproject.org/repository/releases")
        maven {
            url = uri("http://ptms.ink:8081/repository/releases/")
            isAllowInsecureProtocol = true
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}