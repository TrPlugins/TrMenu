package trplugins.menu.module.internal.hook.impl

import net.skinsrestorer.api.SkinsRestorerAPI
import trplugins.menu.module.internal.hook.HookAbstract

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
            if (it.getSkinData(name) == null) {
                return null
            }

            val skinData = it.getSkinData(name)
            if (skinData != null) {
                return skinData.value
            }
        }
        return null
    }

}