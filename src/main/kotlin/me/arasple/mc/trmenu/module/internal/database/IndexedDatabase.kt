package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.entity.Player
import taboolib.library.configuration.FileConfiguration

/**
 * @Author Rubenicos
 * @Since 2021-11-08 17:27
 */
abstract class IndexedDatabase : Database() {

    private val index = PlayerIndex.of(TrMenu.SETTINGS.getString("Database.Index.Player", "USERNAME"))

    override fun pull(player: Player): FileConfiguration {
        return pull(if (index == PlayerIndex.UUID) player.uniqueId.toString() else player.name)
    }

    abstract fun pull(player: String): FileConfiguration

    override fun push(player: Player) {
        push(if (index == PlayerIndex.UUID) player.uniqueId.toString() else player.name)
    }

    abstract fun push(player: String)

    override fun release(player: Player) {
        release(if (index == PlayerIndex.UUID) player.uniqueId.toString() else player.name)
    }

    abstract fun release(player: String)
}