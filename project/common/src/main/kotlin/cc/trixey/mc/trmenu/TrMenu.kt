package cc.trixey.mc.trmenu

import cc.trixey.mc.trmenu.api.TrMenuAPI
import cc.trixey.mc.trmenu.util.Conclusion
import cc.trixey.mc.trmenu.util.parseBoolean
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.kether.KetherShell
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * ExampleProject
 * com.github.username.Common
 *
 * @author 坏黑
 * @since 2022/5/6 22:20
 */
object TrMenu {

    lateinit var api: TrMenuAPI
        private set

    fun register(api: TrMenuAPI) {
        TrMenu.api = api
    }

    fun <T> Collection<Conclusion<T>>.printStatistics(type: String = "bootstrapping"): Collection<Conclusion<T>> {
        if (!any { it.stats == Conclusion.Stats.FAIL }) {
            return this
        }
        println("§c[TrMenu] §8Unexpected exception while $type:")
        forEach {
            println("         §8${it.dumpProcedureFirst() ?: return@forEach}")
        }

        return this
    }

    fun ProxyPlayer.evalKether(script: String): CompletableFuture<Any?> {
        val player = this
        return try {
            KetherShell.eval(script, namespace = listOf("trmenu")) {
                sender = player
            }
        } catch (e: Exception) {
            if (e.javaClass.name.endsWith("kether.LocalizedException")) {
                println("§c[TrMenu] §8Unexpected exception while parsing kether shell:")
                println("  §7Script: §8$script")
                println("  §7Except:")
                e.localizedMessage.split("\n").forEach {
                    println("         §8$it")
                }
            } else {
                e.printStackTrace()
            }
            CompletableFuture.completedFuture(false)
        }
    }

    fun ProxyPlayer.instantKether(script: String, timeout: Long = 100): Any? {
        return try {
            evalKether(script).get(timeout, TimeUnit.MILLISECONDS)
        } catch (e: TimeoutException) {
            println("§c[TrMenu] §8Timeout while parsing kether shell:")
            e.localizedMessage?.split("\n")?.forEach { println("         §8$it") }
            false
        }
    }

    fun ProxyPlayer.conditionalKether(script: String, timeout: Long = 100): Boolean {
        return instantKether(script, timeout).parseBoolean()
    }

    fun Player.evalKether(script: String) =
        adaptPlayer(this).evalKether(script)

    fun Player.instantKether(script: String, timeout: Long = 100) =
        adaptPlayer(this).instantKether(script, timeout)

    fun Player.conditionalKether(script: String, timeout: Long = 100) =
        adaptPlayer(this).conditionalKether(script, timeout)

}