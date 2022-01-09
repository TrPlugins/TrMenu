package trplugins.menu.api.menu

import trplugins.menu.module.display.MenuSession
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/24 11:39
 */
interface ITexture {

    fun generate(session: MenuSession): ItemStack

}