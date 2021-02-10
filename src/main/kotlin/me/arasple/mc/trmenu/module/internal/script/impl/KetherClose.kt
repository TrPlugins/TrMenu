package me.arasple.mc.trmenu.module.internal.script.impl

import io.izzel.taboolib.kotlin.kether.KetherParser
import io.izzel.taboolib.kotlin.kether.ScriptParser
import io.izzel.taboolib.kotlin.kether.common.api.QuestContext
import me.arasple.mc.trmenu.module.internal.script.kether.BaseAction
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/1/29 10:17
 */
class KetherClose : BaseAction<Void>() {

    override fun process(context: QuestContext.Frame): CompletableFuture<Void> {
        context.viewer().session()?.close(closePacket = true, updateInventory = true)

        return completableFuture
    }

    companion object {

        @KetherParser(["close"], namespace = "trmenu")
        fun parser() = ScriptParser.parser { KetherClose() }

    }

}