package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.ArrayUtil
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.configuration.MenuLoader
import me.arasple.mc.trmenu.modules.migrate.impl.MigraterTrMenu
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.io.File


/**
 * @author Arasple
 * @date 2020/7/23 21:28
 */
class CommandMigrate : BaseSubCommand() {

    private val folder = File(TrMenu.plugin.dataFolder, "migrated")

    override fun getArguments() = arrayOf(
        Argument("From Plugin", true) {
            listOf("TrMenuV1")
        },
        Argument("File/Dir Name", true) {
            getFolderFiles()
        }
    )

    override fun getType(): CommandType = CommandType.CONSOLE

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val file = File(TrMenu.plugin.dataFolder, ArrayUtil.arrayJoin(args, 1))

        if (!file.exists() || (!file.isDirectory && !file.name.endsWith(".yml"))) {
            TLocale.sendTo(sender, "MIGRATE.UNKNOWN-FILE", file.name)
            return
        }

        val files = MenuLoader.grabMenuFiles(file)

        if (files.isEmpty()) {
            TLocale.sendTo(sender, "MIGRATE.EMPTY-FILE")
            return
        }

        when (args[0].toLowerCase()) {
            "trmenuv1" -> {
                Tasks.task(true) {
                    var count = 0
                    TLocale.sendTo(sender, "MIGRATE.PROCESSING", files.size)
                    files.forEach {
                        try {
                            MigraterTrMenu(it).run().save(File(folder, it.name))
                            count++
                        } catch (e: Throwable) {
                            TLocale.sendToConsole("MIGRATE.ERROR", it.name, e.message, e.stackTrace.map { it.toString() + "\n" })
                        }
                    }
                    if (count > 0)
                        TLocale.sendTo(sender, "MIGRATE.LOAD-SUCCESS", count)
                    else if (files.size - count > 0)
                        TLocale.sendTo(sender, "MIGRATE.LOAD-ERROR", files.size - count)
                }
            }
            else -> TLocale.sendTo(sender, "MIGRATE.UNSUPPORTED-PLUGIN", args[0])
        }
    }

    private fun getFolderFiles() =
        TrMenu.plugin.dataFolder.listFiles()?.map {
            if (!it.name.matches("lang|menus|migrated|settings.yml".toRegex()) && (it.isDirectory || it.name.endsWith(".yml")))
                it.name
            else
                ""
        }


}