package me.arasple.mc.trmenu.api.action.impl

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.internal.inputer.Inputer
import me.arasple.mc.trmenu.util.collections.CycleList
import org.bukkit.configuration.MemorySection
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 20:15
 */
class ActionCatcher(private val inputer: Inputer) : AbstractAction() {

    override fun onExecute(player: Player, placeholderPlayer: Player) {
        inputer.startInput(player.getSession())
    }

    companion object {

        private val type = "type".toRegex()
        private val start = "before|start".toRegex()
        private val cancel = "cancel".toRegex()
        private val end = "after|end".toRegex()
        private val display = "display|name|title".toRegex()
        private val bookContent = "content|book".toRegex()
        private val itemLeft = "item-?left".toRegex()
        private val itemRight = "item-?reft".toRegex()

        private val name = "(input)?-?catchers?".toRegex()

        private val parser: (Any, ActionOption) -> AbstractAction = { value, _ ->
            val stages = mutableListOf<Inputer.Catcher>()
            if (value is MemorySection) {
                value.getKeys(false).forEach {
                    val map = Property.asSection(value[it])?.let { sec -> Property.toMap(sec) }
                    if (map != null) {
                        val type = Inputer.Type.of(Property.of(map, type, "CHAT"))
                        val start = Reactions.ofReaction(Property.of(map, start, Property.LIST))
                        val cancel = Reactions.ofReaction(Property.of(map, cancel, Property.LIST))
                        val end = Reactions.ofReaction(Property.of(map, end, Property.LIST))

                        val display = arrayOf(
                            Property.of(map, display, ""),
                            Property.of(map, bookContent, Property.LIST).joinToString("\n")
                        )

                        val items = arrayOf(
                            Property.of(map, itemLeft, ""),
                            Property.of(map, itemRight, ""),
                        )

                        stages.add(Inputer.Catcher(type, start, cancel, end, display, items))
                    }
                }
            } else {
                println(value.javaClass.simpleName)
            }

            ActionCatcher(Inputer(CycleList(stages)))
        }

        val registery = name to parser

    }

}