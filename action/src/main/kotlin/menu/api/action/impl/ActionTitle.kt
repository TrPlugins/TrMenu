package menu.api.action.impl

import taboolib.common.util.replaceWithOrder
import trmenu.api.action.base.AbstractAction
import trmenu.api.action.base.ActionOption
import trmenu.util.Regexs
import taboolib.common.platform.ProxyPlayer
import trmenu.api.action.base.ActionDesc

/**
 * @author Arasple
 * @date 2021/1/29 18:01
 * ` `
 */
class ActionTitle(
    val title: String,
    val subTitle: String,
    val fadeIn: Int = 15,
    val stay: Int = 20,
    val fadeOut: Int = 15,
    option: ActionOption
) : AbstractAction(option = option) {

    override fun onExecute(player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        player.sendTitle(
            parse(placeholderPlayer, title),
            parse(placeholderPlayer, subTitle),
            fadeIn,
            stay,
            fadeOut
        )
    }

    companion object : ActionDesc {

        override val name = "(send)?-?(sub)?titles?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, option ->
            var content: String = value.toString()
            val replacements = Regexs.SENTENCE.findAll(content).mapIndexed { index, result ->
                content = content.replace(result.value, "{$index}")
                index to result.groupValues[1].replace("\\s", " ")
            }.toMap().values.toTypedArray()

            val split = content.split(" ", limit = 5)

            ActionTitle(
                split.getOrElse(0) { "" }.replaceWithOrder(*replacements),
                split.getOrElse(1) { "" }.replaceWithOrder(*replacements),
                split.getOrNull(2)?.toIntOrNull() ?: 15,
                split.getOrNull(3)?.toIntOrNull() ?: 20,
                split.getOrNull(4)?.toIntOrNull() ?: 15,
                option
            )
        }

    }

}