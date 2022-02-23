package trplugins.menu.api.action.impl.send

import taboolib.common.platform.ProxyPlayer
import taboolib.common.util.replaceWithOrder
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.util.Regexs

/**
 * TrMenu
 * trplugins.menu.api.action.impl.send.Title
 *
 * @author Score2
 * @since 2022/02/14 10:32
 */
class Title(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(send)?-?(sub)?titles?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val title: String by contents
        val subTitle: String by contents
        val fadeIn: Int by contents
        val stay: Int by contents
        val fadeOut: Int by contents

        player.sendTitle(
            placeholderPlayer.parse(title),
            placeholderPlayer.parse(subTitle),
            fadeIn,
            stay,
            fadeOut
        )
    }

    override fun readContents(contents: Any): ActionContents {
        val actionContents = ActionContents()
        var content: String = contents.toString()
        val replacements = Regexs.SENTENCE.findAll(content).mapIndexed { index, result ->
            content = content.replace(result.value, "{$index}")
            index to result.groupValues[1].replace("\\s", " ")
        }.toMap().values.toTypedArray()

        val split = content.split(" ", limit = 5)

        var title by actionContents
            title = split.getOrElse(0) { "" }.replaceWithOrder(*replacements)
        var subTitle by actionContents
            subTitle = split.getOrElse(1) { "" }.replaceWithOrder(*replacements)
        var fadeIn by actionContents
            fadeIn = split.getOrNull(2)?.toIntOrNull() ?: 15
        var stay by actionContents
            stay = split.getOrNull(3)?.toIntOrNull() ?: 20
        var fadeOut by actionContents
            fadeOut = split.getOrNull(4)?.toIntOrNull() ?: 15

        return actionContents
    }
}