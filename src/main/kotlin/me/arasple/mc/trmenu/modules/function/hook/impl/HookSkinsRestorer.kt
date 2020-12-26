package me.arasple.mc.trmenu.modules.function.hook.impl

import com.mojang.authlib.properties.Property
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.function.hook.HookInstance
import org.bukkit.Bukkit
import skinsrestorer.bukkit.SkinsRestorer
import skinsrestorer.shared.utils.SkinsRestorerAPI

/**
 * @author Arasple
 * @date 2020/8/27 20:27
 */
class HookSkinsRestorer : HookInstance() {

    private lateinit var api: SkinsRestorerAPI

    override fun getDepend(): String {
        return "SkinsRestorer"
    }

    override fun initialization() {
        val plugin = (Bukkit.getPluginManager().getPlugin(getDepend()) as SkinsRestorer)
        if (!plugin.isBungeeEnabled) {
            api = plugin.skinsRestorerBukkitAPI
        }
    }

    fun getSkinTextureValue(name: String): String? {
        try {
            if (!api.hasSkin(name)) return null
            val skinName = api.getSkinName(name)
            val skinData = api.getSkinData(skinName)
            return (skinData as Property).value
        } catch (e: Throwable) {
            TLocale.sendToConsole("ERRORS.HOOK", getDepend())
            e.printStackTrace()
            return null
        }
    }

}