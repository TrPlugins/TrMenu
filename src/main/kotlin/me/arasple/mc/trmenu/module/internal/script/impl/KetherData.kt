package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.ParsedAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import me.arasple.mc.trmenu.module.internal.script.kether.EditType
import me.arasple.mc.trmenu.module.internal.script.kether.EditType.*
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/1/29 10:17
 */
class KetherData(val type: EditType, private val source: ParsedAction<*>, private val apply: ParsedAction<*>?) :
    BaseAction<Any>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Any> {
        val viewer = context.viewer()

        return context.newFrame(source).run<String>().thenApply {
            when (type) {
                DEL -> Metadata.getData(viewer).remove(it)
                SET -> {
                    context.newFrame(apply).run<String>().thenApply { apply ->
                        Metadata.getData(viewer)[it] = apply
                    }
                }
                GET -> Metadata.getData(viewer)[it]
                HAS -> Metadata.getData(viewer).data.containsKey(it)
            }
        }

    }

    companion object {

        @KetherParser(["data"], namespace = "trmenu")
        fun parser() = ScriptParser.parser {
            val type = when (it.nextToken().lowercase()) {
                "del" -> DEL
                "set" -> SET
                "get" -> GET
                else -> throw LocalizedException.of("unknown type")
            }
            val key = it.next(ArgTypes.ACTION)
            it.mark()
            KetherData(
                type, key,
                try {
                    it.expect("to")
                    it.next(ArgTypes.ACTION)
                } catch (ignored: Exception) {
                    it.reset()
                    null
                }
            )
        }

    }

}