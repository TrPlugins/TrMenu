package menu.module.internal.migrate.plugin

import trmenu.module.internal.hook.HookAbstract

/**
 * @author Arasple
 * @date 2021/1/27 12:14
 */
abstract class MigratePlugin(override val name: String) : HookAbstract() {

    abstract fun migrate()

    override fun getPluginName(): String {
        return name
    }

}