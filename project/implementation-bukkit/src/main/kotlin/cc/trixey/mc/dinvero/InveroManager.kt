package cc.trixey.mc.dinvero

import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Arasple
 * @since 2022/10/21
 */
@Deprecated("Invero 1.x 需要重写")
object InveroManager {

    private val registeredPools = CopyOnWriteArrayList<InveroPool>()
    internal var poolIndex = 119
        get() {
            return field++
        }

    @Awake(LifeCycle.ACTIVE)
    fun schedule() {
        submitAsync(delay = 20L, period = 1L) {
            registeredPools.forEach {
                it.forInveros { invero ->
                    invero.tick()
                }
            }
        }
    }

    fun destory(invero: Invero) {
        invero.pool.removeInvero(invero)
        invero.view.closeAll()
    }

    fun unregisterPlayer(player: Player) {
        findViewingInvero(player)?.view?.close(player, true)
        player.updateInventory()
    }

    fun findViewingInvero(player: Player, poolIndex: Int = 0): Invero? {
        if (poolIndex < 0) {
            return getRegisterdPoolByIndex(poolIndex)?.findInvero {
                it.view.isViewing(player)
            }
        } else {
            registeredPools.forEach { pool ->
                val find = pool.findInvero {
                    it.view.isViewing(player)
                }
                if (find != null) {
                    return find
                }
            }
            return null
        }
    }

    fun getRegisterdPoolByIndex(containerId: Int): InveroPool? {
        return registeredPools.find { it.index == containerId }
    }

    fun getRegisterdPoolByName(name: String): InveroPool? {
        return registeredPools.find { it.name == name }
    }

    fun registerPool(name: String): InveroPool {
        return InveroPool(name).also {
            registeredPools.add(it)
        }
    }

}