package me.arasple.mc.trmenu.module.internal.script.kether

import io.izzel.taboolib.kotlin.kether.ScriptContext
import io.izzel.taboolib.kotlin.kether.common.api.QuestAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/1/29 10:18
 */
abstract class BaseAction<T> : QuestAction<T>() {

    internal val completableFuture: CompletableFuture<Void> = CompletableFuture.completedFuture(null)

    fun CommandSender.session(): MenuSession? {
        return if (this is Player) MenuSession.getSession(this) else null
    }

    fun QuestContext.Frame.viewer(): Player {
        return ((this.context() as ScriptContext).sender ?: throw RuntimeException("No viewer found")) as Player
    }

}