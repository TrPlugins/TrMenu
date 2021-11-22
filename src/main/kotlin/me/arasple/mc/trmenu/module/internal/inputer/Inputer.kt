package me.arasple.mc.trmenu.module.internal.inputer

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.impl.ActionSilentClose
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.display.texture.Texture
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.inputer.inputs.InputAnvil
import me.arasple.mc.trmenu.module.internal.inputer.inputs.InputBook
import me.arasple.mc.trmenu.util.collections.CycleList
import me.arasple.mc.trmenu.util.reloadable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.SkipTo
import taboolib.common.platform.function.submit
import taboolib.library.configuration.MemorySection
import taboolib.module.configuration.Configuration
import taboolib.module.nms.inputSign
import taboolib.platform.util.inputBook
import taboolib.platform.util.nextChat
import java.util.function.Consumer
import java.util.function.Function


/**
 * @author Arasple
 * @date 2021/1/31 20:23
 *
 * return = retype current stage
 */
@SkipTo(LifeCycle.ENABLE)
class Inputer(private val stages: CycleList<Catcher>) {

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

    open class Catcher(
        val id: String,
        val type: Type,
        val start: Reactions,
        val cancel: Reactions,
        val end: Reactions,
        val display: Array<String>,
        val items: Array<String>,
        val section: Configuration
    ) {

        fun input(viewer: Player, respond: Function<String, Boolean>) =
            input(viewer) { respond.apply(it) }

        fun input(viewer: Player, respond: (String) -> Boolean) {
            val session = MenuSession.getSession(viewer)
            if (type != Type.CHAT && session.isViewing()) {
                ActionSilentClose().assign(viewer)
            }
            when (type) {
                Type.CHAT -> {
                    submit(delay = 2) {
                        viewer.nextChat {
                            respond(it)
                        }
                    }
                }
                Type.SIGN -> {
                    viewer.inputSign(display[1].split("\n").toTypedArray()) {
                        respond(it.joinToString(""))
                    }
                }
                Type.ANVIL -> {
                    viewer.inputAnvil(kotlin.runCatching { display[0] }.getOrElse { "TrMenu Anvil Catcher" },
                        mapOf(
                            0 to kotlin.runCatching { Texture.createTexture(items[0]).static }.getOrElse { InputAnvil.ANVIL_EMPTY_ITEM },
                            1 to kotlin.runCatching { Texture.createTexture(items[1]).static }.getOrNull()
                    ), respond)
                }
                Type.BOOK -> viewer.inputBook(
                    display[0],
                    true,
                    display[1].split("\n")
                ) { respond(it.joinToString("")) }
            }
            items
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

    companion object {

        internal val cancelWords = reloadable {
            TrMenu.SETTINGS.getStringList("Action.Inputer.Cancel-Words").map { it.toRegex() }
        }

        fun isCancelWord(word: String): Boolean {
            if (word == "ANVIL_CANCEL") {
                return true
            }
            return cancelWords.value.any { it.matches(word) }
        }

        fun Player.retypable(): Boolean {
            return Metadata.byBukkit(this, "RE_ENTER")
        }

        fun Player.inputAnvil(
            title: String,
            items: Map<Int, ItemStack?>,
            respond: (String) -> Boolean,
        ) = InputAnvil(this, title, items, respond).open()

        fun Player.inputBook(
            display: String,
            disposable: Boolean,
            origin: List<String>,
            catcher: Consumer<List<String>>
        ) = InputBook(this, display, disposable, origin, catcher).open()

    }
}