package me.arasple.mc.trmenu.modules.migrate.impl

import me.arasple.mc.trmenu.modules.migrate.Migrater
import java.io.File

/**
 * @author Arasple
 * @date 2020/7/21 8:18
 */
class MigraterTrMenu(val file: File) : Migrater {

    override fun run() {
        loadConfiguration(file)

    }

}