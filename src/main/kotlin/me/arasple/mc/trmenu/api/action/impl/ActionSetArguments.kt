package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.util.Regexs
import me.arasple.mc.trmenu.util.collections.Variables
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/5 8:55
 */
class ActionSetArguments(content: String, option: ActionOption) : AbstractAction(content, option) {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        val args = Variables(baseContent, Regexs.SENTENCE) {
            it[1]
        }.element.flatMap {
            val v = parse(placeholderPlayer, it.value)
            if (it.isVariable) listOf(v)
            else v.split(" ")
        }.filterNot { it.isBlank() }

        Metadata.setBukkitMeta(player, "FORCE_ARGS")
        player.getSession().arguments = args.toTypedArray()
    }

    companion object {

        private val name = "set-?arg(ument)?s?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            ActionSetArguments(value.toString(), option)
        }

        val registery = name to parser

    }

}