package trplugins.menu.module.internal.service

import trplugins.menu.TrMenu
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.module.conf.prop.Property
import trplugins.menu.module.display.MenuSession
import org.bukkit.entity.Player
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.common.platform.function.unregisterCommand
import trplugins.menu.api.action.eval
import trplugins.menu.api.action.ofReaction

/**
 * @author Arasple
 * @date 2020/7/28 15:46
 */
object RegisterCommands {

    private val registered = mutableSetOf<String>()

    private fun ofReaction(any: Any?): Reactions {
        return Reactions.ofReaction(Property.asAnyList(any))
    }

    fun load() {
        registered.removeIf {
            unregisterCommand(it)
            true
        }

        TrMenu.SETTINGS.getConfigurationSection("RegisterCommands")?.let { it ->
            for (main in it.getKeys(false)) {
                val section = it.getConfigurationSection(main) ?: continue
                val argument = section.getConfigurationSection("arguments")
                val reactions = ofReaction(section["execute"])
                val permission = section.get("permission")?.toString()
                val subReactions = mutableMapOf<String, Reactions>()
                argument?.getKeys(false)?.forEach {
                    subReactions[it] = ofReaction(argument[it])
                }
                registered.add(main)
                command(
                    name = main ?: continue,
                    permission = permission ?: "",
                    permissionDefault = if (permission == null) PermissionDefault.TRUE else PermissionDefault.FALSE,
                    aliases = section.getStringList("aliases") ?: listOf()
                ) {
                    dynamic(optional = true) {
                        suggestion<Player>(uncheck = true) { _, _ ->
                            argument?.getKeys(false)?.toList()
                        }
                        execute<Player> { player, context, argument ->
                            val args = if (argument.contains(" ")) argument.split(" ") else listOf(argument)
                            val session = MenuSession.getSession(player)
                            if (args.isNotEmpty()) {
                                subReactions[args[0]]?.let {
                                    if (args.size > 1) session.arguments = args.toMutableList().also { it.removeAt(0) }.toTypedArray()
                                    it.eval(player)
                                }
                            } else {
                                session.arguments = args.toTypedArray()
                                reactions.eval(player)
                            }
                        }
                    }
                    execute<Player> { player, context, argument ->
                        val session = MenuSession.getSession(player)
                        session.arguments = arrayOf()
                        reactions.eval(player)
                    }
                }
            }
        }
    }

}