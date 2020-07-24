package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.configuration.property.Property
import me.arasple.mc.trmenu.configuration.serialize.ReactionSerializer
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.function.Reaction
import me.arasple.mc.trmenu.display.function.Reactions
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.action.impl.menu.ActionClose
import me.arasple.mc.trmenu.modules.catcher.InputCatcher
import me.arasple.mc.trmenu.modules.catcher.InputCatchers
import me.arasple.mc.trmenu.configuration.property.Nodes
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.configuration.MemorySection
import org.bukkit.entity.Player


/**
 * @author Arasple
 * @date 2020/7/21 8:56
 */
class ActionCatcher : Action("catcher") {

    private var catcher: InputCatcher? = null

    override fun onExecute(player: Player) {
        catcher?.let {
            InputCatchers.callCatcher(player, it)
            ActionClose().onExecute(player)
        }
    }

    override fun setContent(any: Any) {
        if (any is MemorySection) {
            val stages = mutableListOf<InputCatcher.Stage>()
            any.getKeys(false).forEach {
                val section = any.getConfigurationSection(it)
                val type = InputCatcher.Type.matchType(section?.getString(Utils.getSectionKey(section, Property.CATCHER_TYPE), "CHAT"))
                val before = ReactionSerializer.serializeReactions(section?.get(Utils.getSectionKey(section, Property.CATCHER_BEFORE)))
                val cancel = ReactionSerializer.serializeReactions(section?.get(Utils.getSectionKey(section, Property.CATCHER_BEFORE)))
                val reaction = ReactionSerializer.serializeReactions(section?.get(Utils.getSectionKey(section, Property.CATCHER_REACTION)))
                stages.add(InputCatcher.Stage(it, type, before, cancel, reaction))
            }
            this.catcher = InputCatcher(Animated(stages.toTypedArray()))
        }
    }

    override fun setContent(content: String) {
        super.setContent(content)

        var stageType: InputCatcher.Type = InputCatcher.Type.CHAT
        var before = Reactions(listOf())
        var cancel = Reactions(listOf())

        var valid: List<Action> = listOf()
        var invalid: List<Action> = listOf()
        var requirement = ""

        Nodes.read(content).second.forEach { (key, value) ->
            when (key) {
                Nodes.TYPE -> stageType = InputCatcher.Type.matchType(value)
                Nodes.BEFORE -> before = Reactions(listOf(Reaction(-1, "", Actions.readActions(value.split(";")), listOf())))
                Nodes.CANCEL -> cancel = Reactions(listOf(Reaction(-1, "", Actions.readActions(value.split(";")), listOf())))
                Nodes.VALID -> valid = Actions.readActions(value.split(";"))
                Nodes.INVALID -> invalid = Actions.readActions(value.split(";"))
                Nodes.REQUIREMENT -> requirement = value
                else -> {
                }
            }
        }

        this.catcher = InputCatcher(Animated(arrayOf(InputCatcher.Stage("", stageType, before, cancel, Reactions(listOf(Reaction(-1, requirement, valid, invalid)))))))
    }

}