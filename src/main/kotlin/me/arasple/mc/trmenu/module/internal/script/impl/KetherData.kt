package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.ParsedAction
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import io.izzel.taboolib.kotlin.kether.common.loader.types.ArgTypes
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.script.impl.KetherData.Type.*
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/1/29 10:17
 */
class KetherData(val type: Type, val source: ParsedAction<*>, private val apply: String?) : BaseAction<String>() {

    enum class Type {

        DEL,
        SET,
        GET

    }

    override fun process(context: QuestContext.Frame): CompletableFuture<String> {
        val viewer = context.viewer()

        return context.newFrame(source).run<String>().thenApply {
            when (type) {
                DEL -> Metadata.getData(viewer).remove(it)
                SET -> Metadata.getData(viewer)[it] = apply.toString()
                GET -> Metadata.getData(viewer)[it]
            }.toString()
        }

    }

    companion object {

        @Suppress("UnstableApiUsage")
        fun parser() = ScriptParser.parser {

            val type = when (it.nextToken().toLowerCase()) {
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
                    it.nextToken()
                } catch (ignored: Exception) {
                    it.reset()
                    null
                }
            )
        }

    }

}