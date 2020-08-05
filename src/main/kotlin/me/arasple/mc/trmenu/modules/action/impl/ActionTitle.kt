package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.configuration.property.Nodes
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Msger
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
        // title: &3&l解锁成功 &a&l请继续操作 20 60 20

        Nodes.read(text).second.let { map ->
            if (map.isEmpty() || map.none { TITLE_NODES.contains(it.key) }) {
                text.split(" ").let {
                    title = replaceWithSpaces(it.getOrElse(0) { "" })
                    subTitle = replaceWithSpaces(it.getOrElse(1) { "" })
                    fadeIn = NumberUtils.toInt(it.getOrElse(2) { "10" }, 10)
                    stay = NumberUtils.toInt(it.getOrElse(3) { "35" }, 35)
                    fadeOut = NumberUtils.toInt(it.getOrElse(4) { "10" }, 10)
                }
            } else {
                map.forEach { (key, value) ->
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
    }

    companion object {

        val TITLE_NODES = setOf(Nodes.TITLE, Nodes.SUBTITLE, Nodes.FADEIN, Nodes.STAY, Nodes.FADEOUT)

    }

}