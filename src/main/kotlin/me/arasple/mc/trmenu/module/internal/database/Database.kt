package me.arasple.mc.trmenu.module.internal.database

import org.bukkit.entity.Player
import taboolib.library.configuration.FileConfiguration

/**
 * @Author sky
 * @Since 2020-08-14 14:38
 */
abstract class Database {

    abstract fun pull(player: Player): FileConfiguration

    abstract fun push(player: Player)

    abstract fun release(player: Player)

}