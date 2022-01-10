package trplugins.build

import gradle.kotlin.dsl.accessors._e98ba513b34f86980a981ef4cafb3d49.publishing
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
}

val archiveName = if (project == rootProject)
    rootProject.name.toLowerCase()
else
    "${rootProject.name.toLowerCase()}-${project.name.toLowerCase()}"

val sourceSets = extensions.getByName("sourceSets") as SourceSetContainer

task<Jar>("sourcesJar") {
    from(sourceSets.named("main").get().allSource)
    archiveClassifier.set("sources")
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
            artifactId = archiveName

            artifact(tasks["sourcesJar"])

            pom {
                allprojects.forEach {
                    repositories.addAll(it.repositories)
                }
            }
        }
    }
}
