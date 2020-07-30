package me.arasple.mc.trmenu.modules.hook

import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import skinsrestorer.bukkit.SkinsRestorer
import skinsrestorer.shared.utils.SkinsRestorerAPI


/**
 * @author Arasple
 * @date 2020/7/25 13:25
 */
object HookSkinsRestorer {

    private const val PLUGIN_NAME = "SkinsRestorer"
    private var IS_HOOKED = false
    private var SKINS_RESTORER: Plugin? = null
    private var SKINS_RESTORER_API: SkinsRestorerAPI? = null

    fun isHooked() = IS_HOOKED

    fun init() {
        SKINS_RESTORER = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME)
        IS_HOOKED = SKINS_RESTORER?.isEnabled ?: false
        if (IS_HOOKED) {
            TLocale.sendToConsole("PLUGIN.HOOKED", PLUGIN_NAME)
            SKINS_RESTORER_API = (SKINS_RESTORER as SkinsRestorer).skinsRestorerBukkitAPI
        }
    }

    fun getSkin(name: String): String {
        if (isHooked()) {
            return SKINS_RESTORER_API?.getProfile(SKINS_RESTORER_API!!.getUUID(name)).toString()
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
        return ""
    }

}