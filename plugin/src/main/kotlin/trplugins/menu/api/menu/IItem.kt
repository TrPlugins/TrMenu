package trplugins.menu.api.menu

import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.display.item.Item
import trplugins.menu.module.display.item.Meta
import trplugins.menu.module.display.texture.Texture
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.buildItem
import taboolib.platform.util.isAir

/**
 * @author Arasple
 * @date 2021/1/25 13:00
 * @see Item
 */
interface IItem {

    /**
     * 生成材质物品
     */
    fun generate(session: MenuSession, texture: Texture, name: String?, lore: List<String>?, meta: Meta): ItemStack

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