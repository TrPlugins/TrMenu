plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven(property("taboolibRepo").toString())
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}