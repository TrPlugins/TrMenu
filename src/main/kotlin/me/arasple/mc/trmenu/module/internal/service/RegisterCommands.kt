package me.arasple.mc.trmenu.module.internal.service

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.module.command.TCommandHandler
import io.izzel.taboolib.module.command.lite.CommandBuilder
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.conf.prop.Property
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/28 15:46
 */
@Deprecated("Plan to develop a new plugin to replace this feature")
object RegisterCommands {

    private val registered = mutableSetOf<String>()

    private fun ofReaction(any: Any?): Reactions {
        return Reactions.ofReaction(Property.asList(any))
    }

    fun load() {

        registered.removeIf {
            Bukkit.getPluginCommand(it)?.unregister(TCommandHandler.getCommandMap())
            true
        }

        TrMenu.SETTINGS.getConfigurationSection("RegisterCommands")?.let { it ->
            for (main in it.getKeys(false)) {
                val section = it.getConfigurationSection(main) ?: continue
                val argument = section.getConfigurationSection("arguments")
                val reactions = ofReaction(section["execute"])
                val subReactions = mutableMapOf<String, Reactions>()
                argument?.getKeys(false)?.forEach { subReactions[it] = ofReaction(argument[it]) }

                CommandBuilder
                    .create(main, TrMenu.plugin)
                    .aliases(*section.getStringList("aliases").toTypedArray())
                    .tab { _, args ->
                        val keys = argument?.getKeys(false)
                        return@tab if (args.size == 1) {
                            keys?.filter { it.startsWith(args[0]) }
                        } else keys?.toList()
                    }
                    .execute { sender, args ->
                        if (sender is Player) {
                            val session = MenuSession.getSession(sender)

                            if (args.isNotEmpty()) {
                                subReactions[args[0]]?.let {
                                    if (args.size > 1) session.arguments = ArrayUtils.remove(args, 0)
                                    it.eval(sender)
                                }
                            } else {
                                if (args.isNotEmpty()) session.arguments = args
                                reactions.eval(sender)
                            }
                        }
                    }
                    .forceRegister()
                    .permission(section.getString("permission"))
                    .build()

                registered.add(main)
            }
        }
    }

}