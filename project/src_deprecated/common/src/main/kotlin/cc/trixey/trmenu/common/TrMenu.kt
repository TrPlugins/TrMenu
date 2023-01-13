package cc.trixey.trmenu.common

import cc.trixey.trmenu.common.api.TrMenuAPI
import cc.trixey.trmenu.common.util.Conclusion
import cc.trixey.trmenu.common.util.parseBoolean
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.warning
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
        println("§c[TrMenu] §4Unexpected exception while $type:")
        forEach {
            println("         §4${it.dumpProcedureFirst() ?: return@forEach}")
        }

        return this
    }

    fun Throwable.printError(vararg adds: String) {
        System.err.println("§c[TrMenu] §4Unexpected exception while ${adds.getOrNull(0) ?: "running"}:")
        if (adds.size > 1) {
            for (i in 1 until adds.size) {
                System.err.println("         §3${adds[i]}")
            }
            System.err.println()
        }
        stackTraceToString().split("\n").forEach {
            System.err.println("         §4$it")
        }
    }

    fun Throwable.printWarning(vararg adds: String) {
        System.err.println("§e[TrMenu] §6Unexpected exception while ${adds.getOrNull(0) ?: "running"}:")
        if (adds.size > 1) {
            for (i in 1 until adds.size) {
                System.err.println("         §3${adds[i]}")
            }
            System.err.println()
        }
        stackTraceToString().split("\n").forEach {
            System.err.println("         §6$it")
        }
    }

    fun ProxyPlayer.evalKether(script: String): CompletableFuture<Any?> {
        val player = this
        return try {
            KetherShell.eval(script, namespace = listOf("trmenu")) {
                sender = player
            }
        } catch (e: Exception) {
            if (e.javaClass.name.endsWith("kether.LocalizedException")) {
                System.err.println("§e[TrMenu] §6Unexpected exception while parsing kether shell:")
                System.err.println("  §7Script: §8$script")
                if (e.localizedMessage != null || e.localizedMessage.isNotBlank()) {
                    System.err.println("  §7Messages:")
                    e.localizedMessage.split("\n").forEach {
                        System.err.println("         §6$it")
                    }
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
            System.err.println("§e[TrMenu] §6Timeout while parsing kether shell:")
            System.err.println("  §7Script: §8$script")
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