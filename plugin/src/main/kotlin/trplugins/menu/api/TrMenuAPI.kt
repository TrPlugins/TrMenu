package trplugins.menu.api

import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.library.kether.LocalizedException
import taboolib.module.kether.KetherShell
import taboolib.module.kether.KetherShell.eval
import taboolib.module.kether.ScriptContext
import taboolib.module.kether.ScriptOptions
import trplugins.menu.module.display.Menu
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.service.Performance
import trplugins.menu.util.EvalResult
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * @author Arasple
 * @date 2021/1/25 9:54
 */
object TrMenuAPI {

    /**
     * Get the trplugins.menu through its ID
     *
     * @return Menu
     * @see Menu
     */
    fun getMenuById(id: String): Menu? {
        return Menu.menus.find { it.id == id }
    }

    @JvmStatic
    fun eval(player: Player, script: String): CompletableFuture<Any?> {
        Performance.check("Handler:Script:Evaluation") {
            return try {
                eval(script, ScriptOptions.builder().namespace(namespace = listOf("trmenu")).sender(adaptPlayer(player)).build())
            } catch (e: LocalizedException) {
                println("§c[TrMenu] §8Unexpected exception while parsing kether shell:")
                e.localizedMessage.split("\n").forEach {
                    println("         §8$it")
                }
                CompletableFuture.completedFuture(false)
            }
        }
        throw Exception()
    }

    @JvmStatic
    fun instantKether(player: Player, script: String, timeout: Long = 100): EvalResult {
        return try {
            EvalResult(eval(player, script).get(timeout, TimeUnit.MILLISECONDS))
        } catch (e: TimeoutException) {
            println("§c[TrMenu] §8Timeout while parsing kether shell:")
            e.localizedMessage?.split("\n")?.forEach { println("         §8$it") }
            EvalResult.FALSE
        }
    }

}