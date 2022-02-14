package trplugins.menu.api.action.impl.send

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.submit
import taboolib.common.util.replaceWithOrder
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.internal.script.FunctionParser
import trplugins.menu.util.Regexs

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.Bossbar
 *
 * @author Score2
 * @since 2022/02/14 12:28
 */
class Bossbar(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(send)?-?boss(bar)?s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val barContent: String by contents
        val color: String by contents
        val style: String by contents
        val stay: Int by contents

        val bossBar = Bukkit.createBossBar(
            FunctionParser.parse(placeholderPlayer.cast(), barContent),
            BarColor.valueOf(color),
            BarStyle.valueOf(style)
        )
        bossBar.addPlayer(player.cast())
        submit(delay = System.currentTimeMillis() + (stay * 50L)) {
            bossBar.removePlayer(player.cast())
        }
    }

    override fun readContents(contents: Any): ActionContents {
        val actionContents = ActionContents()
        var content: String = contents.toString()
        val replacements = Regexs.SENTENCE.findAll(content).mapIndexed { index, result ->
            content = content.replace(result.value, "{$index}")
            index to result.groupValues[1].replace("\\s", " ")
        }.toMap().values.toTypedArray()

        val split = content.split(" ", limit = 4)

        var barContent by actionContents
            barContent = split.getOrElse(0) { "" }.replaceWithOrder(*replacements)
        var color by actionContents
            color = split.getOrElse(1) { "white" }.replaceWithOrder(*replacements)
        var style by actionContents
            style = split.getOrElse(2) { "solid" }.replaceWithOrder(*replacements)
        var stay by actionContents
            stay = split.getOrNull(3)?.toIntOrNull() ?: 15

        return actionContents
    }
}