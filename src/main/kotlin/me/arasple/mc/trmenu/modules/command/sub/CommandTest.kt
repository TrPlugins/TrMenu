package me.arasple.mc.trmenu.modules.command.sub

import io.izzel.taboolib.module.command.lite.CommandBuilder
import io.izzel.taboolib.module.inject.TInject
import me.arasple.mc.trmenu.TrMenu
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.system.measureTimeMillis

/**
 * @author Arasple
 * @date 2020/8/29 19:07
 */
object CommandTest {

    @TInject
    val testV: CommandBuilder = CommandBuilder.create("testTrMenu", TrMenu.plugin).execute { sender, _ ->
        val player = sender as Player
        val names = arrayOf("Arasple", "NotOnline")

        player.sendMessage("Running whitelist check test (10,000 times)")

        names.forEach { name ->
            player.sendMessage("Name: $name -- [1]: ${measureTimeMillis { Bukkit.getOfflinePlayer(name).isWhitelisted }}ms")
            player.sendMessage("Name: $name -- [2]: ${
                measureTimeMillis {
                    Bukkit.getWhitelistedPlayers().any { it.name.equals(name, true) }
                }
            }ms")
        }
    }

}