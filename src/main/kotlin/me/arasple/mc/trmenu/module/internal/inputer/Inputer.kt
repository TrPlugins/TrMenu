package me.arasple.mc.trmenu.module.internal.inputer

import io.izzel.taboolib.util.Features
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.impl.ActionSilentClose
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.util.Tasks
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import me.arasple.mc.trmenu.util.collections.CycleList
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/1/31 20:23
 *
 * return = retype current stage
 */
class Inputer(private val stages: CycleList<Catcher>) {

    companion object {

        // TODO - RELOADABLE
        private val cancelWords = TrMenu.SETTINGS.getStringList("Action.Inputer.Cancel-Words").map { it.toRegex() }

        fun isCancelWord(word: String): Boolean {
            return cancelWords.any { it.matches(word) }
        }

        fun Player.retypable(): Boolean {
            return Metadata.byBukkit(this, "RE_ENTER")
        }

    }


    fun startInput(session: MenuSession) {
        @Suppress("DEPRECATION")
        Metadata.getMeta(session).data.let { map ->
            map.keys.filter { it.startsWith("input") }.forEach { key -> map.remove(key) }
        }

        run(session, stages.next(session.id)!!) { stages.reset(session.id) }
    }

    private fun run(session: MenuSession, stage: Catcher, finish: () -> Unit) {
        val viewer = session.viewer
        stage.start.eval(session)
        stage.input(viewer) {
            Metadata.getMeta(session)["input"] = it
            if (stage.id.isNotBlank()) Metadata.getMeta(session)["input-${stage.id}"] = it

            if (isCancelWord(it)) {
                stage.cancel.eval(session)
                finish.invoke()
                false
            } else if (!stage.end.eval(session)) {
                // BREAK
                finish.invoke()
                false
            } else if (viewer.retypable()) {
                // RETYPE
                true
            } else {
                // PROCEED
                if (stages.isFinal(session.id)) finish.invoke()
                else run(session, stages.next(session.id)!!, finish)
                false
            }
        }
    }

    class Catcher(
        val id: String,
        val type: Type,
        val start: Reactions,
        val cancel: Reactions,
        val end: Reactions,
        val display: Array<String>,
        val items: Array<String>
    ) {

        fun input(viewer: Player, respond: (String) -> Boolean) {
            val session = MenuSession.getSession(viewer)
            if (type != Type.CHAT && session.isViewing()) {
                ActionSilentClose().assign(viewer)
            }
            when (type) {
                Type.CHAT -> {
                    Tasks.delay(2) {
                        Features.inputChat(viewer, object : Features.ChatInput {
                            override fun quit(): String {
                                return ""
                            }

                            override fun onChat(content: String): Boolean {
                                return respond(content)
                            }
                        })
                    }
                }
                Type.SIGN -> Features.inputSign(viewer, display[1].split("\n").toTypedArray()) {
                    respond(it.joinToString(""))
                }
                Type.ANVIL -> AnvilGUI.Builder()
                    .onClose { respond("") }
                    .onComplete { _, text ->
                        if (respond(text)) AnvilGUI.Response.text("")
                        else AnvilGUI.Response.close()
                    }
                    .itemLeft(ItemHelper.fromJson(items[0]))
                    .itemRight(ItemHelper.fromJson(items[1]))
                    .title(display[0])
                    .open(viewer)
                Type.BOOK -> Features.inputBook(
                    viewer,
                    display[0],
                    true,
                    display[1].split("\n")
                ) { respond(it.joinToString("")) }
            }
        }

    }

    enum class Type {

        CHAT,

        SIGN,

        ANVIL,

        BOOK;

        companion object {

            fun of(name: String): Type {
                return values().find { it.name.equals(name, true) } ?: CHAT
            }

        }

    }

}