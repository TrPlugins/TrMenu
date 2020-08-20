package me.arasple.mc.trmenu.modules.service.log

import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.util.Files
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.TrMenu
import java.text.SimpleDateFormat

/**
 * @author Arasple
 * @date 2020/7/21 14:32
 */
object Loger {

    val time = SimpleDateFormat("HH:mm:ss")
    val date = SimpleDateFormat("yyyy-MM-dd")
    val logs = mutableListOf<String>()

    fun log(logType: Log, vararg arguments: Any) {
        logs.add("[${currentTime()}] ${Strings.replaceWithOrder(logType.format, *arguments)}\n")
    }

    @TSchedule(delay = 20, period = 20 * 60, async = true)
    @TFunction.Cancel
    fun save() {
        val file = logFile()
        logs.removeIf {
            file.appendText(it)
            true
        }
    }

    private fun currentTime() = time.format(System.currentTimeMillis())

    private fun logFile() = Files.file(TrMenu.plugin.dataFolder, "logs/${date.format(System.currentTimeMillis())}.log")

}