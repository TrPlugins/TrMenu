package me.arasple.mc.trmenu.commands.sub

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.ArrayUtil
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.sendLocale
import me.arasple.mc.trmenu.modules.configuration.MenuLoader
import me.arasple.mc.trmenu.modules.migrate.MigrateLegacy
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
            listOf("Legacy", "DeluxeMenus")
        },
        Argument("File/Dir Name", true) {
            getFolderFiles()
        }
    )

    override fun getType(): CommandType = CommandType.CONSOLE

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        when (args[0].toLowerCase()) {
            "trmenuv1", "legacy" -> {
                Tasks.task(true) {
                    val file = File(TrMenu.plugin.dataFolder, ArrayUtil.arrayJoin(args, 1))

                    if (!file.exists() || (!file.isDirectory && !file.name.endsWith(".yml"))) {
                        sender.sendLocale("MIGRATE.UNKNOWN-FILE", file.name)
                        return@task
                    }

                    val files = MenuLoader.grabMenuFiles(file)
                    if (files.isEmpty()) {
                        sender.sendLocale("MIGRATE.EMPTY-FILE")
                        return@task
                    }

                    var count = 0
                    sender.sendLocale("MIGRATE.PROCESSING", files.size)
                    files.forEach {
                        try {
                            MigrateLegacy.run(it).save(File(folder, it.name))
                            count++
                        } catch (e: Throwable) {
                            TLocale.sendToConsole("MIGRATE.ERROR", it.name, e.message, e.stackTrace.map { it.toString() + "\n" })
                        }
                    }
                    if (count > 0)
                        sender.sendLocale("MIGRATE.LOAD-SUCCESS", count)
                    else if (files.size - count > 0)
                        sender.sendLocale("MIGRATE.LOAD-ERROR", files.size - count)
                }
            }
            "deluxemenus" -> {

            }
            else -> sender.sendLocale("MIGRATE.UNSUPPORTED-PLUGIN", args[0])
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