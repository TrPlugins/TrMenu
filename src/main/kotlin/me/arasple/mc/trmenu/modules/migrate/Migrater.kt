package me.arasple.mc.trmenu.modules.migrate

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * @author Arasple
 * @date 2020/7/21 8:02
 */
interface Migrater {

    fun run()

    fun loadConfiguration(file: File) = YamlConfiguration.loadConfiguration(file)

}