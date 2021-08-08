package me.arasple.mc.trmenu.module.internal.script.impl

import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher
import openapi.kether.ArgTypes
import openapi.kether.ParsedAction
import openapi.kether.QuestContext
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

        @KetherParser(["item"], namespace = "trmenu")
        fun parser() = scriptParser {
            KetherItem(it.next(ArgTypes.ACTION))
        }

    }

}