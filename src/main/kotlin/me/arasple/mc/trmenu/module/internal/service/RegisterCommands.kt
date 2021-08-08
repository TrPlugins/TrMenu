package me.arasple.mc.trmenu.module.internal.service

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.display.MenuSession
import org.apache.commons.lang3.ArrayUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command
import taboolib.common.platform.registerCommand
import taboolib.common.platform.unregisterCommand
import taboolib.module.configuration.SecuredFile

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
                val subReactions = mutableMapOf<String, Reactions>()
                argument?.getKeys(false)?.forEach {
                    subReactions[it] = ofReaction(argument[it])
                }
                registered.add(main)
                command(
                    name = main ?: continue,
                    permission = section.getString("permission") ?: "",
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