package me.arasple.mc.trmenu.modules.function.inputer

import io.izzel.taboolib.util.lite.Signs
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.Extends.getMenuSession
import me.arasple.mc.trmenu.api.Extends.setMeta
import me.arasple.mc.trmenu.modules.data.Metas
import me.arasple.mc.trmenu.modules.display.animation.Animated
import me.arasple.mc.trmenu.modules.display.function.Reactions
import me.arasple.mc.trmenu.modules.function.inputer.Catchers.Type.ANVIL
import me.arasple.mc.trmenu.modules.function.inputer.Catchers.Type.SIGN
import me.arasple.mc.trmenu.modules.function.inputer.InputCatcher.cancelCatcherReInputing
import me.arasple.mc.trmenu.modules.function.inputer.InputCatcher.getCatcher
import me.arasple.mc.trmenu.modules.function.inputer.InputCatcher.isCatcherReInputing
import me.arasple.mc.trmenu.modules.function.inputer.InputCatcher.removeCatcher
import me.arasple.mc.trmenu.util.Tasks
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/24 20:23
 */
class Catchers(val catchers: Animated<Stage>) {

    fun run(player: Player): Catchers {
        catchers.currentElement(player)?.run(player)
        return this
    }

    class Stage(val id: String, val type: Type, val beforeReactions: Reactions, val cancelReactions: Reactions, val afterReactions: Reactions) {

        val anvilBuilder =
            if (type == ANVIL)
                AnvilGUI.Builder()
                    .plugin(TrMenu.plugin)
                    .preventClose()
                    .onComplete { player, text ->
                        input(player, text)
                        return@onComplete AnvilGUI.Response.close()
                    }
            else null

        fun run(player: Player) {
            beforeReactions.eval(player)
            if (type == SIGN || type == ANVIL) {
                val s = player.getMenuSession()
                s.close(player)
            }
            Tasks.delay(3) {
                if (type == SIGN) {
                    Signs.fakeSign(player) {
                        input(player, it.joinToString(""))
                    }
                } else if (type == ANVIL) anvilBuilder!!.open(player)
            }
        }

        fun input(player: Player, message: String) {
            val input = setInput(player, message)

            // Cancel Catcher
            if (InputCatcher.isCancelWord(input)) {
                player.removeCatcher()
                cancelReactions.eval(player)
                return
            }

            // After Input
            val after = afterReactions.eval(player)

            Tasks.delay(2) {
                if (player.isCatcherReInputing()) {
                    player.cancelCatcherReInputing()
                    run(player)
                } else if (after) {
                    val catcher = player.getCatcher() ?: return@delay
                    if (catcher.catchers.currentIndex(player) >= catcher.catchers.elements.size - 1) {
                        catcher.catchers.reset(player)
                        player.removeCatcher()
                    } else {
                        catcher.catchers.nextElement(player)?.run(player)
                    }
                } else player.removeCatcher()
            }
        }

        private fun setInput(player: Player, message: String): String {
            val input = Metas.filterInput(message)
            if (id.isNotBlank()) {
                player.setMeta("\${input_$id}", input)
            } else {
                player.setMeta(arrayOf("\${input}", "\$input"), input)
            }
            return input
        }

    }

    enum class Type {

        CHAT,
        SIGN,
        ANVIL;

        companion object {

            fun matchType(string: String?) = values().firstOrNull { it.name.equals(string, true) } ?: CHAT

        }

    }

}