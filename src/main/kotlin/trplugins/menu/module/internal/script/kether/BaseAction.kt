package trplugins.menu.module.internal.script.kether

import trplugins.menu.module.display.MenuSession
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.library.kether.QuestAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.ScriptContext
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
        return ((this.context() as ScriptContext).sender ?: throw RuntimeException("No viewer found")).cast()
    }

}