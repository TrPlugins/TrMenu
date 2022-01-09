package trplugins.menu.gradle

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import java.net.URI

/**
 * TrMenu
 * trmenu.build.Publish
 *
 * @author Score2
 * @since 2022/01/09 12:41
 */

fun Project.mavenConfigurate(
    groupId: String? = null,
    artifactId: String? = null,
    version: String? = null
) = Action<PublishingExtension> {
    repositories {
        maven {
            url = URI("https://repo.mcage.cn/repository/trplugins/")
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
            groupId ?: run { this.groupId = groupId }
            artifactId ?: run { this.artifactId = artifactId }
            version ?: run { this.version = version }
        }
    }
}

fun DependencyHandler.taboolib(module: String, version: String) =
    this.add("compileOnly", "io.izzel:taboolib:$version:$module")