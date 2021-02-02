package me.arasple.mc.trmenu.module.display.item

import me.arasple.mc.trmenu.module.display.MenuSession

/**
 * @author Arasple
 * @date 2021/1/24 18:50
 */
class Lore(val lore: List<String>) {

    fun parse(session: MenuSession): List<String> {
        return session.parse(lore)
    }

}