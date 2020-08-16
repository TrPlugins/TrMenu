package me.arasple.mc.trmenu.modules.hook

import com.Zrips.CMI.CMI
import com.Zrips.CMI.Containers.CMIUser
import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.Bukkit
import org.bukkit.entity.Player


/**
 * @author Arasple
 * @date 2020/7/28 16:50
 */
object HookCMI {

    private const val PLUGIN_NAME = "CMI"
    private var IS_HOOKED = false

    fun isHooked() = IS_HOOKED

    fun init() {
        IS_HOOKED = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)?.isEnabled ?: false
        if (isHooked()) {
            TLocale.sendToConsole("PLUGIN.HOOKED", PLUGIN_NAME)
        }
    }

    fun getUser(player: Player): CMIUser? {
        return if (isHooked()) CMI.getInstance().playerManager.getUser(player) else null
    }

}