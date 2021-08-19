package me.arasple.mc.trmenu.module.internal.hook.impl

import com.mojang.authlib.properties.Property
import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import net.skinsrestorer.api.SkinsRestorerAPI
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/1/27 14:12
 */
class HookSkinsRestorer : HookAbstract() {

    private val skinsRestorerAPI: SkinsRestorerAPI? =
        if (isHooked) {
            SkinsRestorerAPI.getApi()
        } else {
            null
        }
        get() {
            if (field == null) reportAbuse()
            return field
        }

    fun getPlayerSkinTexture(name: String): String? {
        skinsRestorerAPI?.let {
            val uuid = Bukkit.getPlayerExact(name)?.uniqueId?.toString() ?: return null
            if (it.getProfile(uuid) == null) {
                return null
            }

            val skinData = it.getProfile(name)
            return (skinData as Property).value
        }
        return null
    }

}