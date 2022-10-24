import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    // all of subprojects
    rootProject.subprojects.map { it.path }.filter { it.startsWith(":project:") }.forEach {
        implementation(project(it))
    }
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
        exclude("META-INF/maven/**")
        exclude("META-INF/tf/**")
        exclude("module-info.java")
    }
    build {
        dependsOn(shadowJar)
    }
}