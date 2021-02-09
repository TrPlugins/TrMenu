package me.arasple.mc.trmenu.module.internal.hook

import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * @author Arasple
 * @date 2021/1/26 22:02
 */
abstract class HookAbstract {

    val name by lazy { getPluginName() }

    val plugin: Plugin? = Bukkit.getPluginManager().getPlugin(name)

    val isHooked by lazy {
        plugin != null && plugin.isEnabled
    }

    open fun getPluginName(): String {
        return javaClass.simpleName.substring(4)
    }

    fun checkHooked(): Boolean {
        return if (isHooked) true else false.also { reportAbuse() }
    }

    fun reportAbuse() {
        TLocale.sendToConsole("Plugin.Dependency.Abuse", name)
    }

}