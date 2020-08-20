package me.arasple.mc.trmenu.modules.function.hook

import com.mojang.authlib.properties.Property
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

    fun getSkinTextureValue(name: String): String? {
        if (isHooked()) {
            try {
                if (!SKINS_RESTORER_API!!.hasSkin(name)) return null
                val skinName = SKINS_RESTORER_API!!.getSkinName(name)
                val skinData = SKINS_RESTORER_API!!.getSkinData(skinName)
                return (skinData as Property).value
            } catch (e: Throwable) {
                return null
            }
        } else {
            TLocale.sendToConsole("ERRORS.HOOK", PLUGIN_NAME)
        }
        return ""
    }

}