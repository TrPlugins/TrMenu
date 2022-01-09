package trplugins.menu.module.internal.database

import trplugins.menu.TrMenu.SETTINGS
import org.bukkit.entity.Player
import taboolib.module.configuration.Configuration
import taboolib.module.database.bridge.Index
import taboolib.module.database.bridge.createBridgeCollection

/**
 * @Author sky
 * @Since 2020-08-14 14:46
 */
class DatabaseMongodb : Database() {

    val collection = createBridgeCollection(
        SETTINGS.getString("Database.Type.MongoDB.client") ?: "mongodb://localhost:3307",
        SETTINGS.getString("Database.Type.MongoDB.database") ?: "trixey",
        SETTINGS.getString("Database.Type.MongoDB.collection") ?: "menu",
        Index.valueOf(index.name)
    )

    override fun pull(player: Player, indexPlayer: String): Configuration {
        return collection[indexPlayer].also {
            if (it.contains("username")) {
                it["username"] = player.name
            }
        }
    }

    override fun push(player: Player, indexPlayer: String) {
        collection.update(indexPlayer)
    }

    override fun release(player: Player, indexPlayer: String) {
        collection.release(indexPlayer)
    }
}