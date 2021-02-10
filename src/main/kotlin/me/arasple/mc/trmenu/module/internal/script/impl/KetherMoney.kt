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
class KetherMoney(val money: ParsedAction<*>) : BaseAction<Boolean>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Boolean> {
        return context.newFrame(money).run<Any>().thenApply {
            HookPlugin.getVault().hasMoney(context.viewer(), it.toString().toDoubleOrNull() ?: 0.0)
        }
    }

    companion object {

        @KetherParser(["money", "eco"], namespace = "trmenu")
        fun parser() = ScriptParser.parser {
            KetherMoney(it.next(ArgTypes.ACTION))
        }

    }

}