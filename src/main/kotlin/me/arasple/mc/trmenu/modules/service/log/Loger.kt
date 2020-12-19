package me.arasple.mc.trmenu.modules.service.log

import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.util.Files
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.service.Mirror
import java.text.SimpleDateFormat

/**
 * @author Arasple
 * @date 2020/7/21 14:32
 */
object Loger {

    val time = SimpleDateFormat("HH:mm:ss")
    val date = SimpleDateFormat("yyyy-MM-dd")
    val waves = mutableMapOf<String, MutableList<String>>()

    fun log(menu: Menu, logType: Log, vararg arguments: Any) {
        waves
            .computeIfAbsent(menu.id) { mutableListOf() }
            .add("[${currentTime()}] ${Strings.replaceWithOrder(logType.format, *arguments)}\n")
    }

    @TSchedule(delay = 20, period = 20 * 60, async = true)
    @TFunction.Cancel
    fun save() {
        Mirror.eval("LogService:onSave(async)") {
            waves.forEach { (id, logs) ->
                val file = logFile(id)
                logs.removeIf {
                    file.appendText(it)
                    true
                }
            }
        }
    }

    private fun currentTime() = time.format(System.currentTimeMillis())

    private fun logFile(id: String) = Files.file(TrMenu.plugin.dataFolder, "logs/$id/${date.format(System.currentTimeMillis())}.log")

}