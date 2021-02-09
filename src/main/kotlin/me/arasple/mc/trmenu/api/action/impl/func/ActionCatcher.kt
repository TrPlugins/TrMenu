package me.arasple.mc.trmenu.api.action.impl.func

import me.arasple.mc.trmenu.api.action.base.AbstractAction
import me.arasple.mc.trmenu.api.action.base.ActionOption
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.internal.inputer.Inputer
import me.arasple.mc.trmenu.util.collections.CycleList
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
            if (value is Map<*, *>) {
                println("LoadCatcher from $value")
                value.forEach {
                    val section = Property.asSection(value[it])
                    println("part: $it, section $section")
                    if (section != null) {
                        val type = Inputer.Type.of(section.getString(Property.getSectionKey(section, type), "CHAT")!!)
                        val start = Reactions.ofReaction(section.getList(Property.getSectionKey(section, start)))
                        val cancel = Reactions.ofReaction(section.getList(Property.getSectionKey(section, cancel)))
                        val end = Reactions.ofReaction(section.getList(Property.getSectionKey(section, end)))

                        val display = arrayOf(
                            section.getString(Property.getSectionKey(section, display), "")!!,
                            section.getStringList(Property.getSectionKey(section, display)).joinToString("\n"),
                        )

                        val items = arrayOf(
                            section.getString(Property.getSectionKey(section, itemLeft), "")!!,
                            section.getString(Property.getSectionKey(section, itemRight), "")!!
                        )

                        stages.add(Inputer.Catcher(type, start, cancel, end, display, items))
                    } else {
                        println("[X] Null")
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