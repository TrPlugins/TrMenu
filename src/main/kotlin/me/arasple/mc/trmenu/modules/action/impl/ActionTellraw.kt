package me.arasple.mc.trmenu.modules.action.impl

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Utils
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/8 21:55
 */
class ActionTellraw : Action("tellraw|json") {

    private var rawJson: String? = null
    private val tellraw = TellrawJson.create()

    override fun onExecute(player: Player) = player.spigot().sendMessage(*ComponentSerializer.parse(Msger.replace(player, if (Strings.nonEmpty(rawJson)) rawJson.toString() else tellraw!!.toRawMessage())))

    override fun setContent(content: String) {
        var text = content
        text = TLocale.Translate.setColored(text)
        if (Utils.isJson(text)) {
            rawJson = text
            return
        }
        Variables(text).find().variableList.forEach { i ->
            if (i.text == text) return
            else if (!i.isVariable) tellraw.append(i.text)
            else {
                val args = i.text.split('?', limit = 2).toTypedArray().also { tellraw.append(it[0]) }
                ArrayUtils.remove(args, 0).firstOrNull()?.split('&', '?')?.forEach { it ->
                    it.split('&').forEach {
                        val event = it.split(':', '=')
                        if (event.size >= 2) {
                            event[1].let { value ->
                                when (event[0].toLowerCase()) {
                                    "hover" -> tellraw.hoverText(value)
                                    "suggest" -> tellraw.clickSuggest(value)
                                    "command" -> tellraw.clickCommand(value)
                                    "url" -> tellraw.clickOpenURL(value)
                                    else -> { }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}