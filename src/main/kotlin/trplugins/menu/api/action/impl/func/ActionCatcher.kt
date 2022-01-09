package trplugins.menu.api.action.impl.func

import trplugins.menu.api.action.base.AbstractAction
import trplugins.menu.api.action.base.ActionOption
import trplugins.menu.module.conf.prop.Property
import trplugins.menu.module.internal.inputer.Inputer
import trplugins.menu.util.collections.CycleList
import org.bukkit.entity.Player
import trplugins.menu.api.action.InternalAction
import trplugins.menu.api.action.base.ActionDesc
import trplugins.menu.api.action.ofReaction
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.module.display.session

/**
 * @author Arasple
 * @date 2021/1/31 20:15
 */
class ActionCatcher(private val inputer: Inputer) : InternalAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        inputer.startInput(player.session())
    }

    companion object : ActionDesc {

        val type = "type".toRegex()
        val start = "before|start".toRegex()
        val cancel = "cancel".toRegex()
        val end = "after|end".toRegex()
        val display = "display|name|title".toRegex()
        val bookContent = "content|book".toRegex()
        val itemLeft = "item-?left".toRegex()
        val itemRight = "item-?right".toRegex()


        override val name = "(input)?-?catchers?".toRegex()

        override val parser: (Any, ActionOption) -> AbstractAction = { value, _ ->
            val stages = mutableListOf<Inputer.Catcher>()
            if (value is Map<*, *>) {
                value.forEach {
                    val section = Property.asSection(it.value)
                    if (section != null) {
                        val id = it.key.toString()
                        val type = Inputer.Type.of(section.getString(Property.getSectionKey(section, type)) ?: "CHAT")

                        val start =
                            Reactions.ofReaction(Property.asAnyList(section[Property.getSectionKey(section, start, "start")]))
                        val cancel =
                            Reactions.ofReaction(Property.asAnyList(section[Property.getSectionKey(section, cancel, "cancel")]))
                        val end =
                            Reactions.ofReaction(Property.asAnyList(section[Property.getSectionKey(section, end, "end")]))

                        val display = arrayOf(
                            section.getString(Property.getSectionKey(section, display, "display")) ?: "TrMenu Catcher",
                            section.getStringList(Property.getSectionKey(section, display, "display")).joinToString("\n"),
                        )

                        val items = arrayOf(
                            section.getString(Property.getSectionKey(section, itemLeft)) ?: "left",
                            section.getString(Property.getSectionKey(section, itemRight)) ?: "right"
                        )

                        stages += Inputer.Catcher(id, type, start, cancel, end, display, items, section)
                    }
                }
            }
            ActionCatcher(Inputer(CycleList(stages)))
        }

    }

}