package me.arasple.mc.trmenu.module.internal.inputer

import com.google.common.collect.Lists
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.action.impl.ActionSilentClose
import me.arasple.mc.trmenu.api.action.pack.Reactions
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.util.bukkit.ItemHelper
import me.arasple.mc.trmenu.util.collections.CycleList
import net.md_5.bungee.api.chat.TextComponent
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEditBookEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.SkipTo
import taboolib.common.platform.SubscribeEvent
import taboolib.common.platform.submit
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.inputSign
import taboolib.platform.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer


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
        val items: Array<String>
    ) {

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
                Type.BOOK -> inputBook(
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

    companion object {

        // TODO - RELOADABLE
        private val cancelWords by lazy { TrMenu.SETTINGS.getStringList("Action.Inputer.Cancel-Words").map { it.toRegex() } }

        fun isCancelWord(word: String): Boolean {
            return cancelWords.any { it.matches(word) }
        }

        fun Player.retypable(): Boolean {
            return Metadata.byBukkit(this, "RE_ENTER")
        }

        val inputBookMap: ConcurrentHashMap<String, Consumer<List<String>>> =
            ConcurrentHashMap<String, Consumer<List<String>>>()


        /**
         * 向玩家发送一本书
         * 并捕获该书本的编辑动作
         *
         * @param player     玩家
         * @param display    展示名称
         * @param disposable 编辑后销毁
         * @param origin     原始内容
         * @param catcher    编辑动作
         */
        fun inputBook(
            player: Player,
            display: String,
            disposable: Boolean,
            origin: List<String>,
            catcher: Consumer<List<String>>
        ) {
            // 移除正在编辑的书本
            player.inventory.takeItem(99) { item ->
                item.hasLore("Features Input")
            }
            // 发送书本
            player.inventory.addItem(
                ItemBuilder(BookBuilder().also {
                    it.material = XMaterial.WRITABLE_BOOK
                    it.bookPages.add(BookBuilder.Text(java.lang.String.join("\n", origin), true))
                }.build()).also {
                    it.name = "§f$display"
                    it.lore.addAll(arrayOf("§0Features Input", if (disposable) "§0Disposable" else ""))
                }.build()
            )
            inputBookMap[player.name] = catcher
        }


        @SubscribeEvent
        fun e(e: PlayerEditBookEvent) {
            val bookLore = e.newBookMeta.lore
            if (bookLore != null && bookLore.size > 0 && bookLore[0] == "§0Features Input") {
                val consumer = inputBookMap[e.player.name]
                if (consumer != null) {
                    val pages: MutableList<String> = Lists.newArrayList()
                    for (page in e.newBookMeta.pages) {

                        pages.addAll(TextComponent(page).toPlainText().replace("§0", "").split("\n"))
                    }
                    consumer.accept(pages)
                    // 一次性捕获
                    if (bookLore.size > 1 && bookLore[1] == "§0Disposable") {
                        inputBookMap.remove(e.player.name)
                        e.player.inventory.takeItem(99) { item ->
                            item.hasLore("Features Input")
                        }
                    }
                }
            }
        }
    }

}