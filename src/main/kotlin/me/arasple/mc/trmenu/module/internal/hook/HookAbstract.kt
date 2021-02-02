package me.arasple.mc.trmenu.module.internal.hook

import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/1/26 22:02
 */
abstract class HookAbstract {

    val name: String = javaClass.simpleName.substring(4)

    val plugin = Bukkit.getPluginManager().getPlugin(name)

    val isHooked: Boolean by lazy {
        plugin != null && plugin.isEnabled
    }

    fun reportAbuse() {
        TLocale.sendToConsole("Plugin.Dependency.Abuse", name)
    }

}