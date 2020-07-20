package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Nodes
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/28 19:46
 */
class ActionTitle(var title: String, var subTitle: String, var fadeIn: Int, var stay: Int, var fadeOut: Int) : Action("(send)?(-)?(sub)?title(s)?") {

    constructor() : this("", "", 10, 35, 10)

    override fun onExecute(player: Player) = TLocale.Display.sendTitle(
        player,
        Msger.replace(player, title),
        Msger.replace(player, subTitle),
        fadeIn, stay, fadeOut
    )

    override fun setContent(content: String) {
        val text = content
        Nodes.read(text).second.forEach { (key, value) ->
            when (key) {
                Nodes.TITLE -> title = value
                Nodes.SUBTITLE -> subTitle = value
                Nodes.FADEIN -> fadeIn = NumberUtils.toInt(value, 10)
                Nodes.STAY -> stay = NumberUtils.toInt(value, 35)
                Nodes.FADEOUT -> fadeOut = NumberUtils.toInt(value, 10)
                else -> {
                }
            }
        }
    }

}