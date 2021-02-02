package me.arasple.mc.trmenu.module.internal.hook.impl

import com.mojang.authlib.properties.Property
import me.arasple.mc.trmenu.module.internal.hook.HookAbstract
import skinsrestorer.bukkit.SkinsRestorer
import skinsrestorer.shared.utils.SkinsRestorerAPI

/**
 * @author Arasple
 * @date 2021/1/27 14:12
 */
class HookSkinsRestorer : HookAbstract() {

    private val skinsRestorerAPI: SkinsRestorerAPI? =
        if (isHooked && !(plugin as SkinsRestorer).isBungeeEnabled) plugin.skinsRestorerBukkitAPI else null
        get() {
            if (field == null) reportAbuse()
            return field
        }

    fun getPlayerSkinTexture(name: String): String? {
        skinsRestorerAPI?.let {
            if (!it.hasSkin(name)) return null

            val skinName = it.getSkinName(name)
            val skinData = it.getSkinData(skinName)
            return (skinData as Property).value
        }

        return null
    }

}