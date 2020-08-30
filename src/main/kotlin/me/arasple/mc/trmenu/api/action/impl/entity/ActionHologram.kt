package me.arasple.mc.trmenu.api.action.impl.entity

import io.izzel.taboolib.module.hologram.THologram
import me.arasple.mc.trmenu.api.action.base.Action
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/8/3 11:18
 */
class ActionHologram : Action("hologram") {

    override fun onExecute(player: Player) {
        THologram.create(
                player.eyeLocation.add(0.0, 0.6, 0.0),
                ""
        )
                .flash(
                        flash("§3༺ §b༒ §3༻"),
                        1
                )
                .toAll()
                .deleteOn(40)
    }

    private fun flash(string: String): List<String> {
        var format = ""
        return mutableListOf<String>().let { list ->
            string.toCharArray().forEach {
                format += it
                list.add(format)
            }
            list
        }
    }

}