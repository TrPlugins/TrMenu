package cc.trixey.mc.trmenu.legacy.invero.type

import cc.trixey.mc.trmenu.legacy.invero.InveroPool
import cc.trixey.mc.trmenu.legacy.invero.nms.WindowProperty
import java.util.*

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InvChest(pool: InveroPool, rows: Int, title: String, viewer: UUID?) : InvInstance(
    pool = pool, property = when (rows) {
        1 -> WindowProperty.GENERIC_9X1
        2 -> WindowProperty.GENERIC_9X2
        3 -> WindowProperty.GENERIC_9X3
        4 -> WindowProperty.GENERIC_9X4
        5 -> WindowProperty.GENERIC_9X5
        else -> WindowProperty.GENERIC_9X6
    }, title = title, viewer
) {

    override fun tick() {
//        view.forViewers {
////            it.sendActionBar("$${System.currentTimeMillis()} ~ Just a tick")
//        }
    }

}