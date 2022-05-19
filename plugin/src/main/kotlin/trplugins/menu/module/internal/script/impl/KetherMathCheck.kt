package trplugins.menu.module.internal.script.impl

import trplugins.menu.module.internal.script.kether.BaseAction
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
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

    override fun process(context: QuestContext.Frame): CompletableFuture<Any>? {
        return menu?.let {
            context.newFrame(it).run<String>().thenApply {
                when (type) {
                    Type.INT -> it.toIntOrNull() != null
                    Type.DOUBLE -> it.toDoubleOrNull() != null
                }
            }
        }
    }

    companion object {

        @KetherParser(["mathcheck", "mtc"], namespace = "trmenu", shared = true)
        fun parser() = scriptParser {
            val type = Type.valueOf(it.nextToken().uppercase())
            KetherMathCheck(
                type,
                it.next(ArgTypes.ACTION)
            )
        }

    }

}