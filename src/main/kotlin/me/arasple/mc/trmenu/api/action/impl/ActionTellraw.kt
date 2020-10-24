package me.arasple.mc.trmenu.api.action.impl

import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.api.action.base.Action
import me.arasple.mc.trmenu.util.Hex
import me.arasple.mc.trmenu.util.Msger
import me.arasple.mc.trmenu.util.Utils
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:55
 */
class ActionTellraw : Action("tellraw|json") {

    private var rawJson: String? = null
    private val tellraw = TellrawJson.create()

    override fun onExecute(player: Player) = player.spigot().sendMessage(
        *ComponentSerializer.parse(
            Msger.replace(
                player,
                if (Strings.nonEmpty(rawJson)) rawJson else tellraw!!.toRawMessage()
            ).replace(
                "{&}", "&"
            )
        )
    )

    override fun setContent(content: String) {
        var text = Hex.colorify(content)
        if (Utils.isJson(text)) {
            rawJson = text
            return
        }
        text = text.replace('?', '&')
        Variables(text).find().variableList.forEach { part ->
            if (part.text == text) return
            else if (!part.isVariable) tellraw.append(part.text)
            else {
                val args = part.text.split('&', limit = 2).toMutableList().also {
                    tellraw.append(it[0])
                    it.removeAt(0)
                }
                args.firstOrNull()?.split('&')?.forEach { it ->
                    it.split('&').forEach {
                        val event = it.split(':', '=', limit = 2)
                        if (event.size >= 2) event[1].let { value ->
                            when (event[0].toLowerCase()) {
                                "hover" -> tellraw.hoverText(value)
                                "suggest" -> tellraw.clickSuggest(value)
                                "command" -> tellraw.clickCommand(value)
                                "url" -> tellraw.clickOpenURL(value)
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}