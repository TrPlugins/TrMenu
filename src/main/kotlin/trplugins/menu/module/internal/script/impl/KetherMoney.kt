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
class KetherMoney(private val money: ParsedAction<*>) : BaseAction<Boolean>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Boolean> {
        return context.newFrame(money).run<Any>().thenApply {
            HookPlugin.getVault().hasMoney(context.viewer(), it.toString().toDoubleOrNull() ?: 0.0)
        }
    }

    companion object {

        @KetherParser(["money", "eco"], namespace = "trmenu", shared = true)
        fun parser() = scriptParser {
            KetherMoney(it.next(ArgTypes.ACTION))
        }

    }

}