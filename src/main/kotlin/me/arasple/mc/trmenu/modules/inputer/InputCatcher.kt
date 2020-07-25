package me.arasple.mc.trmenu.modules.inputer

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.MetaPlayer.removeMeta
import me.arasple.mc.trmenu.data.MetaPlayer.removeMetaStartsWith
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/24 21:37
 */
object InputCatcher {

    private val catchers = mutableMapOf<UUID, Catchers?>()

    fun Player.setCatcher(catcher: Catchers) = catchers.put(this.uniqueId, catcher)

    fun Player.getCatcher() = catchers.computeIfAbsent(this.uniqueId) { null }

    fun Player.removeCatcher() {
        catchers.remove(this.uniqueId)
        this.clearCatcherMeta()
    }

    fun Player.clearCatcherMeta() {
        this.removeMeta("\$input")
        this.removeMetaStartsWith("\${input")
    }

    fun Player.isCatcherReInputing() = this.hasMetadata("RE_ENTER")

    fun Player.cancelCatcherReInputing() = this.removeMetadata("RE_ENTER", TrMenu.plugin)

    fun isCancelWord(word: String) = TrMenu.SETTINGS.getStringList("Actions.Catcher-Cancel-Words").any { word.split(" ")[0].matches(it.toRegex()) }

}