package me.arasple.mc.trmenu.api.menu

import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.display.item.Item
import me.arasple.mc.trmenu.module.display.item.Meta
import me.arasple.mc.trmenu.module.display.texture.Texture
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem

/**
 * @author Arasple
 * @date 2021/1/25 13:00
 * @see Item
 */
interface IItem {

    /**
     * 生成材质物品
     */
    fun generate(session: MenuSession, texture: Texture, name: String?, lore: List<String>?, meta: Meta): ItemStack {
        val item = texture.generate(session)

        if (item.type == Material.AIR || item.type.name.endsWith("_AIR")) {
            return item
        }

        val itemStack = buildItem(item) {
            if (item.itemMeta != null) {
                name?.let { this.name = it }
                lore?.let { this.lore.addAll(it) }
            }
            meta.flags(this)
            meta.shiny(session, this)

            if (meta.hasAmount()) this.amount = meta.amount(session)
        }
        meta.nbt(session, itemStack)?.also {
            itemStack.itemMeta = it
        }
        return itemStack
    }

    /**
     * 更新材质
     */
    fun updateTexture(session: MenuSession)

    /**
     * 更新物品显示名称
     */
    fun updateName(session: MenuSession)

    /**
     * 更新物品显示 Lore
     */
    fun updateLore(session: MenuSession)

}