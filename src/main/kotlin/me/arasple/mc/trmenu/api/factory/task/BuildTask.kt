package me.arasple.mc.trmenu.api.factory.task

import me.arasple.mc.trmenu.api.factory.MenuFactorySession
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/20 20:21
 */
fun interface BuildTask {

    fun run(player: Player, session: MenuFactorySession)

}