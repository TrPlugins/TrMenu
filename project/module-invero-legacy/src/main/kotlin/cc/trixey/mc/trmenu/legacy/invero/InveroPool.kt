package cc.trixey.mc.trmenu.legacy.invero

import cc.trixey.mc.trmenu.legacy.invero.InveroManager.poolIndex
import cc.trixey.mc.trmenu.legacy.invero.nms.WindowProperty
import cc.trixey.mc.trmenu.legacy.invero.type.InvChest
import cc.trixey.mc.trmenu.legacy.invero.type.InvHopper
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/10/21
 */
class InveroPool(
    val name: String,
    private val inveros: CopyOnWriteArrayList<Invero> = CopyOnWriteArrayList(),
    val index: Int = poolIndex
) {

    fun createInvero(
        type: WindowProperty, title: String = "Untitled", viewer: UUID? = null, constructor: Invero.() -> Unit = {}
    ): Invero {
        return createInvero(this, type, title, viewer).also {
            inveros.add(it)
            constructor(it)
        }
    }

    fun findInvero(filter: (Invero) -> Boolean): Invero? {
        return inveros.find(filter)
    }

    fun forInveros(run: (Invero) -> Unit) {
        inveros.forEach(run)
    }

    fun removeInvero(invero: Invero) {
        inveros.remove(invero)
    }

    companion object {

        private fun createInvero(
            pool: InveroPool, type: WindowProperty, title: String, viewer: UUID?
        ): Invero {
            if (type.isOrdinaryChest) {
                return InvChest(pool, type.rows, title, viewer)
            } else return when (type) {
                WindowProperty.GENERIC_3X3 -> TODO()
                WindowProperty.ANVIL -> TODO()
                WindowProperty.BEACON -> TODO()
                WindowProperty.BLAST_FURNACE -> TODO()
                WindowProperty.BREWING_STAND -> TODO()
                WindowProperty.CRAFTING -> TODO()
                WindowProperty.ENCHANTMENT -> TODO()
                WindowProperty.FURNACE -> TODO()
                WindowProperty.GRINDSTONE -> TODO()
                WindowProperty.HOPPER -> InvHopper(pool, title, viewer)
                WindowProperty.LOOM -> TODO()
                WindowProperty.MERCHANT -> TODO()
                WindowProperty.SHULKER_BOX -> TODO()
                WindowProperty.SMOKER -> TODO()
                WindowProperty.CARTOGRAPHY_TABLE -> TODO()
                WindowProperty.STONECUTTER -> TODO()
                else -> throw UnsupportedOperationException("")
            }
        }

    }

}