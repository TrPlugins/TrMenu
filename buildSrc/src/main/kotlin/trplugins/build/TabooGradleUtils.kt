package trplugins.build

import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * TrMenu
 * trmenu.build.Publish
 *
 * @author Score2
 * @since 2022/01/09 12:41
 */

fun DependencyHandler.taboolib(dependency: String) =
    this.taboolib(dependency.substringBeforeLast(":"), dependency.substringAfterLast(":"))

fun DependencyHandler.taboolib(module: String, version: String) =
    this.add("implementation", "io.izzel:taboolib:$version:$module")

fun DependencyHandler.taboolibCompileOnly(dependency: String) =
    this.taboolibCompileOnly(dependency.substringBeforeLast(":"), dependency.substringAfterLast(":"))

fun DependencyHandler.taboolibCompileOnly(module: String, version: String) =
    this.add("compileOnly", "io.izzel:taboolib:$version:$module")