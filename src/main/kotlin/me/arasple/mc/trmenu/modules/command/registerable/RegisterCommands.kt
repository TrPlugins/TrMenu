package me.arasple.mc.trmenu.modules.command.registerable

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.module.command.TCommandHandler
import io.izzel.taboolib.module.command.lite.CommandBuilder
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.setArguments
import me.arasple.mc.trmenu.modules.conf.serialize.ReactionSerializer
import me.arasple.mc.trmenu.modules.display.function.Reactions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/28 15:46
 */
object RegisterCommands {

    val registered = mutableSetOf<String>()

    fun load() {
        registered.removeIf {
            Bukkit.getPluginCommand(it)?.unregister(TCommandHandler.getCommandMap())
            true
        }

        TrMenu.SETTINGS.getConfigurationSection("RegisterCommands")?.let { it ->
            for (main in it.getKeys(false)) {
                val section = it.getConfigurationSection(main) ?: continue
                val argument = section.getConfigurationSection("arguments")
                val reactions = ReactionSerializer.serializeReactions(section.get("execute"))
                val subReactions = mutableMapOf<String, Reactions>()
                argument?.getKeys(false)
                    ?.forEach { subReactions[it] = ReactionSerializer.serializeReactions(argument[it]) }
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
                            if (args.isNotEmpty()) {
                                subReactions[args[0]]?.let {
                                    if (args.size > 1) sender.setArguments(ArrayUtils.remove(args, 0))
                                    it.eval(sender)
                                }
                            } else {
                                if (args.isNotEmpty()) sender.setArguments(args)
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