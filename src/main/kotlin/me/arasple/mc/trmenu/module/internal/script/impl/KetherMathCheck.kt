package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.ParsedAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/4/2 17:10
 * mtc
 */
class KetherMathCheck(val type: Type, val menu: ParsedAction<*>?) : BaseAction<Any>() {

    enum class Type {

        INT,

        DOUBLE

    }

    override fun process(context: QuestContext.Frame): CompletableFuture<Any> {
        return context.newFrame(menu).run<String>().thenApply {
            when (type) {
                Type.INT -> it.toIntOrNull() != null
                Type.DOUBLE -> it.toDoubleOrNull() != null
            }
        }
    }

    companion object {

        @KetherParser(["mathcheck", "mtc"], namespace = "trmenu")
        fun parser() = ScriptParser.parser {
            val type = Type.valueOf(it.nextToken().uppercase())
            KetherMathCheck(
                type,
                it.next(ArgTypes.ACTION)
            )
        }

    }

}