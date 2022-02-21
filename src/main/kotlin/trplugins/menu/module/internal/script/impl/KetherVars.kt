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
 * @date 2021/2/8 21:52
 */
class KetherVars(private val source: ParsedAction<*>) : BaseAction<String>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<String> {
        return context.newFrame(source).run<Any>().thenApply {
            context.viewer().session()?.parse(it.toString().trimIndent())
        }
    }

    companion object {

        @KetherParser(["vars", "var"], namespace = "trmenu", shared = true)
        fun parser() = scriptParser {
            KetherVars(it.next(ArgTypes.ACTION))
        }

    }

}