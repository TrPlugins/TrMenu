package me.arasple.mc.trmenu.module.internal.database

import me.arasple.mc.trmenu.TrMenu.SETTINGS
import org.bukkit.entity.Player
import taboolib.library.configuration.FileConfiguration
import taboolib.module.database.bridge.Index
import taboolib.module.database.bridge.createBridgeCollection

/**
 * @Author sky
 * @Since 2020-08-14 14:46
 */
class DatabaseMongodb : Database() {

    val collection = createBridgeCollection(
        SETTINGS.getString("Database.Type.MongoDB.client"),
        SETTINGS.getString("Database.Type.MongoDB.database"),
        SETTINGS.getString("Database.Type.MongoDB.collection"),
        try {
            Index.valueOf(SETTINGS.getString("Database.Index.Player", "UUID").uppercase())
        } catch (ignored: Throwable) {
            Index.UUID
        }
    )

    override fun pull(player: Player): FileConfiguration {
        return collection[player.asIndexType()].also {
            if (it.contains("username")) {
                it.set("username", player.name)
            }
        }
    }

    override fun push(player: Player) {
        collection.update(player.asIndexType())
    }

    override fun release(player: Player) {
        collection.release(player.asIndexType())
    }

    private fun Player.asIndexType(): String {
        return if (collection.index == Index.UUID) uniqueId.toString() else name
    }
}