package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.loader.PluginHandle
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.metrics.MetricsHandler
import me.arasple.mc.trmenu.modules.expression.Expressions
import me.arasple.mc.trmenu.utils.Msger
import me.arasple.mc.trmenu.utils.Skulls
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scheduler.BukkitWorker

/**
 * @author Arasple
 * @date 2020/3/7 12:36
 */
class CommandDebug : BaseSubCommand() {

    val description: YamlConfiguration = PluginHandle.getPluginDescription()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        if (sender is Player) {
            if (sender.hasMetadata("TrMenu:Debug")) {
                sender.removeMetadata("TrMenu:Debug", TrMenu.plugin)
                TLocale.sendTo(sender, "COMMANDS.DEBUG.OFF")
            } else {
                sender.setMetadata("TrMenu:Debug", FixedMetadataValue(TrMenu.plugin, ""))
                TLocale.sendTo(sender, "COMMANDS.DEBUG.ON")
            }
        } else {
            if (args.isNotEmpty()) {
                val menu = TrMenuAPI.getMenuById(args[0])
                if (menu != null) {
                    sender.sendMessage(menu.toString())
                    return
                }
                sender.sendMessage(
                    arrayOf(
                        "§3§l「§8--------------------------------------------------§3§l」",
                        "",
                        "§2Total Menus: §6"
                    )
                )
                Menu.getAllMenus().forEach { if (it.value.isNotEmpty()) sender.sendMessage("§r  §8▪ ${it.key}§8: §7${it.value.size}") }
                sender.sendMessage(
                    arrayOf(
                        "§2Cached Skulls: §6${Skulls.cache.size}",
                        "§2Cached Expressions: §6${Expressions.cachedParsed.size}",
                        "§2Running Tasks: §6${Bukkit.getScheduler().activeWorkers.stream().filter { t: BukkitWorker -> t.owner === TrMenu.plugin }.count() + Bukkit.getScheduler().pendingTasks.stream().filter { t: BukkitTask -> t.owner === TrMenu.plugin }.count()}",
                        "§2bStats: §3${MetricsHandler.B_STATS?.isEnabled}", "§2cStats: §3${MetricsHandler.C_STATS?.isEnabled}", "§2TabooLib: §f${PluginHandle.getVersion()}", "",
                        "§3TrMenu Built-Info: §b${description.getString("built-time")}§7, §3By §a${description.getString("built-by")}",
                        "",
                        "§3§l「§8--------------------------------------------------§3§l」"
                    )
                )
            } else TLocale.sendTo(sender, "COMMANDS.DEBUG.${if (Msger.debug()) "ON" else "OFF"}")
        }
    }

}