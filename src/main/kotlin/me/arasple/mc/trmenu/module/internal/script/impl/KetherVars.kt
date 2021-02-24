package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.ParsedAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/8 21:52
 */
class KetherVars(private val source: ParsedAction<*>) : BaseAction<String>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<String> {
        return context.newFrame(source).run<Any>().thenApply {
            context.viewer().session()?.parse(it.toString().trimIndent())
        }
    }

    companion object {

        @KetherParser(["vars", "var"], namespace = "trmenu")
        fun parser() = ScriptParser.parser {
            KetherVars(it.next(ArgTypes.ACTION))
        }

    }

}