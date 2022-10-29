import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    rootProject.subprojects
        .map { it.path }
        .filter { it.startsWith(":project:") }
        .forEach {
            implementation(project(it))
        }
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
}