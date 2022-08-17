package trplugins.menu.api.action.impl.func

import taboolib.common.platform.ProxyPlayer
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.api.action.base.ActionBase
import trplugins.menu.api.action.base.ActionContents
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.module.display.session
import trplugins.menu.module.internal.inputer.Inputer
import trplugins.menu.util.collections.CycleList
import trplugins.menu.util.conf.Property

/**
 * TrMenu
 * trplugins.menu.api.action.impl.func.Catcher
 *
 * @author Arasple
 * @since 2022/02/14 13:08
 */
class Catcher(handle: ActionHandle) : ActionBase(handle) {

    override val regex = "(input)?-?catchers?".toRegex()

    val type = "type".toRegex()
    val start = "before|start".toRegex()
    val cancel = "cancel".toRegex()
    val end = "after|end".toRegex()
    val display = "display|name|title".toRegex()
    val bookContent = "content|book".toRegex()
    val itemLeft = "item-?left".toRegex()
    val itemRight = "item-?right".toRegex()

    override fun onExecute(contents: ActionContents, player: ProxyPlayer, placeholderPlayer: ProxyPlayer) {
        val inputer: Inputer by contents
        inputer.startInput(player.session())
    }

    override fun readContents(contents: Any): ActionContents {
        val actionContents = ActionContents()
        val stages = mutableListOf<Inputer.Catcher>()
        var inputer: Inputer by actionContents
        if (contents is Map<*, *>) {
            contents.forEach {
                val section = Property.asSection(it.value)
                if (section != null) {
                    val id = it.key.toString()
                    val type = Inputer.Type.of(section.getString(Property.getSectionKey(section, type)) ?: "CHAT")

                    val start =
                        Reactions.ofReaction(handle, Property.asAnyList(section[Property.getSectionKey(section, start, "start")]))
                    val cancel =
                        Reactions.ofReaction(handle, Property.asAnyList(section[Property.getSectionKey(section, cancel, "cancel")]))
                    val end =
                        Reactions.ofReaction(handle, Property.asAnyList(section[Property.getSectionKey(section, end, "end")]))

                    val display = arrayOf(
                        section.getString(Property.getSectionKey(section, display, "display")) ?: "TrMenu Catcher",
                        section.getStringList(Property.getSectionKey(section, display, "display")).joinToString("\n"),
                    )

                    val items = arrayOf(
                        section.getString(Property.getSectionKey(section, itemLeft)),
                        section.getString(Property.getSectionKey(section, itemRight))
                    )

                    stages += Inputer.Catcher(id, type, start, cancel, end, display, items, section)
                }
            }
        }
        inputer = Inputer(CycleList(stages))
        return actionContents
    }

}