package me.arasple.mc.trmenu.api.action.impl

import taboolib.common.util.replaceWithOrder
import taboolib.common.platform.function.submit
import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.util.Regexs
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player

class ActionBossbar(
    val barContent: String,
    val color: String,
    val style: String,
    val stay: Int,
    option: ActionOption
) : AbstractAction(option = option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {

        val bossBar = Bukkit.createBossBar(
            parse(placeholderPlayer, barContent),
            BarColor.valueOf(color),
            BarStyle.valueOf(style)
        )
        bossBar.addPlayer(player)
        submit(delay = System.currentTimeMillis() + (stay * 50L)) {
            bossBar.removePlayer(player)
        }
    }

    companion object {

        private val name = "(send)?-?boss(bar)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            var content: String = value.toString()
            val replacements = Regexs.SENTENCE.findAll(content).mapIndexed { index, result ->
                content = content.replace(result.value, "{$index}")
                index to result.groupValues[1].replace("\\s", " ")
            }.toMap().values.toTypedArray()

            val split = content.split(" ", limit = 4)

            ActionBossbar(
                split.getOrElse(0) { "" }.replaceWithOrder(*replacements),
                split.getOrElse(1) { "white" }.replaceWithOrder(*replacements),
                split.getOrElse(2) { "solid" }.replaceWithOrder(*replacements),
                split.getOrNull(3)?.toIntOrNull() ?: 15,
                option
            )
        }

        val registry = name to parser
    }
}