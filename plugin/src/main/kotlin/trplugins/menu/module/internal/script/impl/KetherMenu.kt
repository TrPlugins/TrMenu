package trplugins.menu.module.internal.script.impl

import trplugins.menu.api.TrMenuAPI
import trplugins.menu.api.event.MenuOpenEvent
import trplugins.menu.module.internal.script.kether.BaseAction
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestContext
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser
import java.util.concurrent.CompletableFuture
import kotlin.math.min

/**
 * @author Arasple
 * @date 2021/1/29 10:17
 */
class KetherMenu(val type: Type, val menu: ParsedAction<*>?) : BaseAction<Void>() {

    enum class Type {

        OPEN,

        PAGE,

        CLOSE

    }

    override fun process(context: QuestContext.Frame): CompletableFuture<Void> {
        val viewer = context.viewer()
        val session = viewer.session()

        when (type) {
            Type.CLOSE -> session?.close(closePacket = true, updateInventory = true)
            Type.PAGE -> menu?.let {
                context.newFrame(it).run<Int>().thenApply {
                    val menu = session?.menu ?: return@thenApply false
                    val page = min(it.coerceAtLeast(0), menu.layout.getSize() - 1)

                    menu.page(viewer, page)
                }
            }
            Type.OPEN -> menu?.let {
                context.newFrame(it).run<String>().thenApply {
                    TrMenuAPI.getMenuById(it)?.open(context.viewer(), reason = MenuOpenEvent.Reason.CONSOLE)
                }
            }
        }

        return completableFuture
    }

    companion object {

        @KetherParser(["menu"], namespace = "trmenu", shared = true)
        fun parser() = scriptParser {
            val type = Type.valueOf(it.nextToken().uppercase())
            KetherMenu(
                type,
                if (type != Type.CLOSE) it.next(ArgTypes.ACTION) else null
            )
        }

    }

}