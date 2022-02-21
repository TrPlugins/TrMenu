package trplugins.menu.module.internal.script.impl

import trplugins.menu.module.internal.script.kether.BaseAction
import trplugins.menu.util.bukkit.ItemMatcher
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/4 15:24
 */
class KetherItem(private val itemMatcher: ParsedAction<*>) : BaseAction<Boolean>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Boolean> {
        return context.newFrame(itemMatcher).run<Any>().thenApply {
            ItemMatcher.eval(it.toString()).hasItem(context.viewer())
        }
    }

    companion object {

        @KetherParser(["item"], namespace = "trmenu", shared = true)
        fun parser() = scriptParser {
            KetherItem(it.next(ArgTypes.ACTION))
        }

    }

}