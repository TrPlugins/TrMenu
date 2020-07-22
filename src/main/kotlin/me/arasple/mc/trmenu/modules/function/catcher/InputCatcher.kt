package me.arasple.mc.trmenu.modules.function.catcher

import io.izzel.taboolib.util.lite.Signs
import me.arasple.mc.trmenu.data.MetaPlayer
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.function.Reactions
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/7/21 10:08
 */
class InputCatcher(val stages: Animated<Stage>) {

    fun currentStage(player: Player) = stages.currentElement(player)

    fun nextStage(player: Player) = stages.nextElement(player)

    fun isFinal(player: Player): Boolean = stages.currentIndex(player) + 1 == stages.elements.size

    fun run(player: Player): InputCatcher {
        stages.currentElement(player)?.before(player)
        return this
    }

    class Stage(val id: String, val type: Type, val before: Reactions, val cancel: Reactions, val reactions: Reactions) {

        fun before(player: Player) {
            before.eval(player)
            if (type == Type.SIGN) {
                Signs.fakeSign(player) {
                    val catcher = InputCatchers.getCatcher(player) ?: return@fakeSign
                    val result = reaction(player, MetaPlayer.filterInput(it.joinToString()))
                    if (result == Result.PROCCED) {
                        catcher.nextStage(player)
                    } else if (result == Result.RETRY) {
                        catcher.currentStage(player)?.before(player)
                    }
                }
            }
        }

        fun reaction(player: Player, input: String): Result {
            if (id.isNotBlank()) {
                MetaPlayer.setMeta(player, "\${input_$id}", input)
            } else {
                MetaPlayer.setMeta(player, "\${input}", input)
                MetaPlayer.setMeta(player, "\$input", input)
            }

            return if (InputCatchers.isCancelWord(input)) {
                cancel(player)
                Result.CANCEL
            } else if (!reactions.eval(player)) {
                cancel(player)
                InputCatchers.finish(player)
                Result.RETRY
            } else Result.PROCCED
        }

        fun cancel(player: Player) {
            cancel.eval(player)
        }

        enum class Result {

            CANCEL,
            PROCCED,
            RETRY

        }

    }

    enum class Type {

        SIGN,
        CHAT,
        ANVIL;

        companion object {

            fun matchType(name: String?): Type {
                return values().firstOrNull { it.name.equals(name, true) } ?: CHAT
            }

        }

    }

}