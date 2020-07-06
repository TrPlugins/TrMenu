package me.arasple.mc.trmenu.display.icon

import me.arasple.mc.trmenu.display.item.BaseLore
import me.arasple.mc.trmenu.display.item.BaseMaterial

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
class IconDisplay(val pages: Array<Int>, val slots: Array<Slot>, val name: Array<String>, val material: Array<BaseMaterial>, val lore: Array<BaseLore>) {

    class Slot(val position: Array<Int>)

}