import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":project:common"))
    implementation(project(":project:module-invero-common"))
    implementation(project(":project:module-serialize"))
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}