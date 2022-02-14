package trplugins.menu.api.action.impl.menu

import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import trplugins.menu.api.TrMenuAPI
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.api.event.MenuOpenEvent
import trplugins.menu.module.internal.data.Metadata

/**
 * TrMenu
 * trplugins.menu.api.action.impl.inventory.Open
 *
 * @author Score2
 * @since 2022/02/14 11:14
 */
class Open(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "opens?|(open)?-?gui|(tr)?menu".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val args = contents.stringContent().parseContentSplited(placeholderPlayer, " ")
        val split = args[0].split(":")
        val arguments = if (args.size == 1) arrayOf() else args.toTypedArray().copyOfRange(1, args.size)
        val menu = TrMenuAPI.getMenuById(split[0]) ?: return
        val page = split.getOrNull(1)?.toIntOrNull() ?: 0

        menu.open(player.cast(), page, MenuOpenEvent.Reason.PLAYER_COMMAND) {
            if (!Metadata.byBukkit(player.cast(), "FORCE_ARGS") || arguments.isNotEmpty()) {
                if (arguments.isEmpty()) {
                    it.arguments = it.implicitArguments.clone()
                    it.implicitArguments = arrayOf()
                } else {
                    it.arguments = arguments
                }
            }
        }
    }

}