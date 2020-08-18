package me.arasple.mc.trmenu.modules.action.impl

import me.arasple.mc.trmenu.modules.configuration.property.Nodes
import me.arasple.mc.trmenu.modules.configuration.property.Property
import me.arasple.mc.trmenu.modules.configuration.serialize.ReactionSerializer
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.function.Reaction
import me.arasple.mc.trmenu.display.function.Reactions
import me.arasple.mc.trmenu.modules.action.Actions
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.modules.inputer.Catchers
import me.arasple.mc.trmenu.modules.inputer.InputCatcher
import me.arasple.mc.trmenu.modules.inputer.InputCatcher.clearCatcherMeta
import me.arasple.mc.trmenu.modules.inputer.InputCatcher.setCatcher
import me.arasple.mc.trmenu.utils.Utils
import org.bukkit.configuration.MemorySection
import org.bukkit.entity.Player


/**
 * @author Arasple
 * @date 2020/7/21 8:56
 */
class ActionCatcher : Action("(input)?(-)?catcher(s)?") {

    private var catcher: Catchers? = null

    override fun onExecute(player: Player) {
        if (InputCatcher.cooldown.isCooldown(player.name)) return

        catcher?.let {
            player.clearCatcherMeta()
            catcher?.let {
                player.setCatcher(it.run(player))
            }
        }
    }

    override fun setContent(any: Any) {
        if (any is MemorySection) {
            val stages = mutableListOf<Catchers.Stage>()

            any.getKeys(false).forEach {
                val section = Utils.asSection(any.get(it))

                if (section != null) {
                    val type = Catchers.Type.matchType(section.getString(Utils.getSectionKey(section, Property.CATCHER_TYPE), "CHAT"))
                    val before = ReactionSerializer.serializeReactions(section.get(Utils.getSectionKey(section, Property.CATCHER_BEFORE)))
                    val cancel = ReactionSerializer.serializeReactions(section.get(Utils.getSectionKey(section, Property.CATCHER_CANCEL)))
                    val reaction = ReactionSerializer.serializeReactions(section.get(Utils.getSectionKey(section, Property.CATCHER_REACTION)))

                    stages.add(Catchers.Stage(it, type, before, cancel, reaction))
                }
            }
            this.catcher = Catchers(Animated(stages.toTypedArray()))
        }
    }

    override fun setContent(content: String) {
        super.setContent(content)

        var stageType: Catchers.Type = Catchers.Type.CHAT
        var before = Reactions(listOf())
        var cancel = Reactions(listOf())

        var valid: List<Action> = listOf()
        var invalid: List<Action> = listOf()
        var requirement = ""

        Nodes.read(content).second.forEach { (key, value) ->
            when (key) {
                Nodes.TYPE -> stageType = Catchers.Type.matchType(value)
                Nodes.BEFORE -> before = Reactions(listOf(Reaction(-1, "", Actions.readActions(value.split(";")), listOf())))
                Nodes.CANCEL -> cancel = Reactions(listOf(Reaction(-1, "", Actions.readActions(value.split(";")), listOf())))
                Nodes.VALID -> valid = Actions.readActions(value.split(";"))
                Nodes.INVALID -> invalid = Actions.readActions(value.split(";"))
                Nodes.REQUIREMENT -> requirement = value
                else -> {
                }
            }
        }

        this.catcher = Catchers(Animated(arrayOf(Catchers.Stage("", stageType, before, cancel, Reactions(listOf(Reaction(-1, requirement, valid, invalid)))))))
    }

    override fun toString(): String = buildString {
        append(this@ActionCatcher.javaClass.simpleName.removePrefix("Action").toLowerCase())
        append(": ")
        append(catcher)
        append(buildString { options.forEach { append("<${it.key.name.toLowerCase()}: ${it.value}>") } })
    }

}