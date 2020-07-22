package me.arasple.mc.trmenu.modules.log

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

    fun log(logType: Log, vararg arguments: Any) {
        logFile().appendText("[${currentTime()}] ${Strings.replaceWithOrder(logType.format, *arguments)}\n")
    }

    private fun currentTime() = time.format(System.currentTimeMillis())

    private fun logFile() = Files.file(TrMenu.plugin.dataFolder, "logs/${date.format(System.currentTimeMillis())}.log")

}