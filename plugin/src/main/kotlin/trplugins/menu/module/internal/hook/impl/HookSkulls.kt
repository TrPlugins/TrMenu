package trplugins.menu.module.internal.hook.impl

import ca.tweetzy.skulls.Skulls
import ca.tweetzy.skulls.api.SkullsAPI
import trplugins.menu.module.internal.hook.HookAbstract
import trplugins.menu.util.bukkit.Heads
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem

/**
 * TrMenu
 * trmenu.HookSkulls
 *
 * @author Score2
 * @since 2022/01/06 20:57
 */
class HookSkulls : HookAbstract() {

    private val empty = buildItem(XMaterial.PLAYER_HEAD) { name = "UNKNOWN_${super.name.uppercase()}" }

    fun getSkull(id: String): ItemStack {
        return SkullsAPI.getSkullItemStack(id.toIntOrNull() ?: 1) ?: empty
    }

    fun getRandomSkull(): ItemStack {
        return SkullsAPI.getRandomSkull()?.itemStack ?: empty
    }

    fun getId(itemStack: ItemStack): String? {
        return Skulls.getSkullManager().skulls.find { it.texture == Heads.seekTexture(itemStack) }?.texture
    }

}