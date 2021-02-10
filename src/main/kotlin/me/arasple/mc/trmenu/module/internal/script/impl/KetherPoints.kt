package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.ParsedAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/4 15:28
 */
class KetherPoints(val points: ParsedAction<*>) : BaseAction<Boolean>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Boolean> {
        return context.newFrame(points).run<Any>().thenApply {
            HookPlugin.getPlayerPoints().hasPoints(context.viewer(), it.toString().toIntOrNull() ?: 0)
        }
    }

    companion object {

        @KetherParser(["points"], namespace = "trmenu")
        fun parser() = ScriptParser.parser {
            KetherPoints(it.next(ArgTypes.ACTION))
        }

    }

}