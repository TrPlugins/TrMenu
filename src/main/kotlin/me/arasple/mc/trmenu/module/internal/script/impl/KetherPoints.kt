package me.arasple.mc.trmenu.module.internal.script.impl

import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import openapi.kether.ArgTypes
import openapi.kether.ParsedAction
import openapi.kether.QuestContext
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

        @KetherParser(["points"], namespace = "trmenu")
        fun parser() = scriptParser {
            KetherPoints(it.next(ArgTypes.ACTION))
        }

    }

}