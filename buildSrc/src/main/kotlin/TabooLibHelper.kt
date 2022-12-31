import Versions.taboolib_version
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * TrMenu
 * .TabooLibHelper
 *
 * @author Score2
 * @since 2022/12/30 23:53
 */


fun DependencyHandler.tabooModule(module: String, im: ImportMethod = ImportMethod.API) {
    when (im) {
        ImportMethod.API -> add("compileOnly", "io.izzel.taboolib:$module:$taboolib_version")
        ImportMethod.IMPL -> add("implementation", "io.izzel.taboolib:$module:$taboolib_version")
    }
}
