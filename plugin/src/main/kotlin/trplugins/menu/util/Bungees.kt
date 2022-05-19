package trplugins.menu.util

import trplugins.menu.TrMenu
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * @author Arasple
 * @date 2020/3/8 21:19
 */
object Bungees {

    init {
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(TrMenu.plugin, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(TrMenu.plugin, "BungeeCord")
        }
    }

    fun connect(player: Player, server: String) = sendBungeeData(player, "Connect", server)

    fun sendBungeeData(player: Player, vararg args: String) {
        val byteArray = ByteArrayOutputStream()
        val out = DataOutputStream(byteArray)
        for (arg in args) {
            try {
                out.writeUTF(arg)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        player.sendPluginMessage(TrMenu.plugin, "BungeeCord", byteArray.toByteArray())
    }

}