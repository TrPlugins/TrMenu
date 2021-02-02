package me.arasple.mc.trmenu.api.menu

import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/24 11:39
 */
interface ITexture {

    fun generate(session: MenuSession): ItemStack

}