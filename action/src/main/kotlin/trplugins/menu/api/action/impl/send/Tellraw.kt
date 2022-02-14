package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import taboolib.module.chat.HexColor
import taboolib.module.chat.TellrawJson
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.util.collections.Variables
import trplugins.menu.util.jsonParser

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.Tellraw
 *
 * @author Score2
 * @since 2022/02/14 11:07
 */
class Tellraw(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(tell(raw)?|json)s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        player.sendRawMessage(contents.stringContent().parseContent(placeholderPlayer))
    }

    private val matcher = "<(.+?)>".toRegex()

    override fun readContents(contents: Any): ActionContents {
        val raw = contents.toString()
        val json: String
        if (kotlin.runCatching { jsonParser.parse(raw) }.isSuccess) {
            json = raw
        } else {
            val tellraw = TellrawJson()

            Variables(raw, matcher) { it[1] }.element.forEach { result ->

                if (result.isVariable) {
                    val splits = result.value.split("@")
                    tellraw.append(HexColor.translate(splits[0]))

                    splits.mapNotNull {
                        val keyValue = it.split("=", ":", limit = 2)
                        if (keyValue.size >= 2)
                            keyValue[0] to keyValue[1]
                        else null
                    }.forEach {
                        val (type, content) = it
                        when (type.lowercase()) {
                            "hover" -> tellraw.hoverText(content.replace("\\n", "\n"))
                            "suggest" -> tellraw.suggestCommand(content)
                            "command", "execute" -> tellraw.runCommand(content)
                            "url", "open_url" -> tellraw.openURL(content)
                        }
                    }
                } else tellraw.append(HexColor.translate(result.value))
            }
            json = tellraw.toRawMessage()
        }

        return ActionContents(json)
    }

}