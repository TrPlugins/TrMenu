package trplugins.menu.module.internal.script.impl

import trplugins.menu.module.internal.hook.HookPlugin
import trplugins.menu.module.internal.script.kether.BaseAction
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/4 15:28
 */
class KetherPoints(private val points: ParsedAction<*>) : BaseAction<Boolean>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Boolean> {
        return context.newFrame(points).run<Any>().thenApply {
            HookPlugin.getPlayerPoints().hasPoints(context.viewer(), it.toString().toIntOrNull() ?: 0)
        }
    }

    companion object {

        @KetherParser(["points"], namespace = "trmenu", shared = true)
        fun parser() = scriptParser {
            KetherPoints(it.next(ArgTypes.ACTION))
        }

    }

}