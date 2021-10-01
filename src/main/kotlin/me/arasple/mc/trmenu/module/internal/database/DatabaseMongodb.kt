package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu.SETTINGS
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.library.configuration.FileConfiguration
import taboolib.module.database.bridge.Index
import taboolib.module.database.bridge.createBridgeCollection

/**
 * @Author sky
 * @Since 2020-08-14 14:46
 */
class DatabaseMongodb : Database() {

    val collection = createBridgeCollection(
        SETTINGS.getString("Database.Url.Client"),
        SETTINGS.getString("Database.Url.Database"),
        SETTINGS.getString("Database.Url.Collection"),
        Index.UUID
    )

    override fun pull(player: Player): FileConfiguration {
        return collection[adaptPlayer(player)].also {
            if (it.contains("username")) {
                it.set("username", player.name)
            }
        }
    }

    override fun push(player: Player) {
        collection.update(player.uniqueId.toString())
    }

    override fun release(player: Player) {
        collection.release(player.uniqueId.toString())
    }
}