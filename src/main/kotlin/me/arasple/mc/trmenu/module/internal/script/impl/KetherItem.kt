package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.ParsedAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import me.arasple.mc.trmenu.util.bukkit.ItemMatcher
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/4 15:24
 */
class KetherItem(val itemMatcher: ParsedAction<*>) : BaseAction<Boolean>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Boolean> {
        return context.newFrame(itemMatcher).run<Any>().thenApply {
            ItemMatcher.eval(it.toString()).hasItem(context.viewer())
        }
    }

    companion object {

        @KetherParser(["item"], namespace = "trmenu")
        fun parser() = ScriptParser.parser {
            KetherItem(it.next(ArgTypes.ACTION))
        }

    }

}