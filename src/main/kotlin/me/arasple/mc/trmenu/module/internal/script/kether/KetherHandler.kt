package me.arasple.mc.trmenu.module.internal.script.kether

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.ScriptContext
import io.izzel.taboolib.kotlin.kether.ScriptLoader
import me.arasple.mc.trmenu.module.internal.script.EvalResult
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * @author Arasple
 * @date 2021/1/27 10:38
 **/
object KetherHandler {

    fun eval(player: Player, script: String): EvalResult {
        return EvalResult(KetherShell.eval(script, namespace = listOf("trmenu")) {
            this.sender = player
        }.get(1, TimeUnit.SECONDS))
    }

    private fun runScript(sender: CommandSender, raw: String): CompletableFuture<Any> {
        val quest = ScriptLoader.load(raw)
        return ScriptContext.create(quest) { this.sender = sender }.runActions()
    }

}