package me.arasple.mc.trmenu.modules.catcher

import me.arasple.mc.trmenu.TrMenu
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/21 10:06
 */
object InputCatchers {

    private val catchers = mutableMapOf<UUID, InputCatcher?>()

    fun callCatcher(player: Player, catcher: InputCatcher) = catchers.put(player.uniqueId, catcher.run(player))

    fun getCatcher(player: Player) = catchers.computeIfAbsent(player.uniqueId) { null }

    fun finish(player: Player) {
        catchers[player.uniqueId] = null
    }

    fun isCancelWord(word: String): Boolean {
        return TrMenu.SETTINGS.getStringList("Actions.Catcher-Cancel-Words").any { word.split(" ")[0].matches(it.toRegex()) }
    }


}