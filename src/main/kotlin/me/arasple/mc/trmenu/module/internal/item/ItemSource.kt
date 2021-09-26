package me.arasple.mc.trmenu.module.internal.item

import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.script.js.JavaScriptAgent
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/27 12:04
 */
object ItemSource {

    fun fromSource(session: MenuSession, string: String): ItemStack? {
        val identifier = string.split(":", "=", limit = 2)
        val name = identifier[0].replace("-", "").uppercase()
        val id = identifier[1]

        return when (name) {
            "HEADDATABASE", "HDB" -> {
                if (id.equals("RANDOM", true)) HookPlugin.getHeadDatabase().getRandomHead()
                else HookPlugin.getHeadDatabase().getHead(id)
            }
            "JAVASCRIPT", "JS" -> JavaScriptAgent.eval(session, id).asItemStack()
            "ORAXEN" -> HookPlugin.getOraxen().getItem(id)
            "ITEMSADDER", "IA" -> HookPlugin.getItemsAdder().getItem(id)
            "ZAPHKIEL", "ZL" -> HookPlugin.getZaphkiel().getItem(id)
            else -> null
        }
    }

}