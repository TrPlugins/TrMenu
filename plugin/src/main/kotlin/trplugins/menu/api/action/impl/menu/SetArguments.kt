package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.module.display.session
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.util.Regexs
import trplugins.menu.util.collections.Variables

/**
 * TrMenu
 * trplugins.menu.api.action.impl.menu.SetArguments
 *
 * @author Score2
 * @since 2022/02/14 12:23
 */
class SetArguments(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "set-?arg(ument)?s?".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val baseContent = contents.stringContent()
        val args = Variables(baseContent, Regexs.SENTENCE) {
            it[1]
        }.element.flatMap {
            val v = it.value.parse(placeholderPlayer)
            if (it.isVariable) listOf(v)
            else v.split(" ")
        }.filterNot { it.isBlank() }

        Metadata.setBukkitMeta(player.cast(), "FORCE_ARGS")
        player.session().arguments = args.toTypedArray()
    }
}