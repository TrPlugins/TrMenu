package me.arasple.mc.trmenu.api.menu

import io.izzel.taboolib.util.item.ItemBuilder
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.display.item.Item
import me.arasple.mc.trmenu.module.display.item.Meta
import me.arasple.mc.trmenu.module.display.texture.Texture
import org.bukkit.inventory.ItemStack

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
        val builder = ItemBuilder(item)
        if (item.itemMeta != null) {
            name?.let { builder.name(it) }
            lore?.let { builder.lore(it) }
        }
        meta.flags(builder)
        meta.shiny(session, builder)

        if (meta.hasAmount()) builder.amount(meta.amount(session))
        val itemStack = builder.build()
        meta.nbt(session, itemStack)?.also { itemStack.itemMeta = it }
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