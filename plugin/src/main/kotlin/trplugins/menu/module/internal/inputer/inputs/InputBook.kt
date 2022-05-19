package trplugins.menu.module.internal.inputer.inputs

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEditBookEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.BookBuilder
import taboolib.platform.util.buildBook
import taboolib.platform.util.hasLore
import taboolib.platform.util.takeItem
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

/**
 * 向玩家发送一本书
 * 并捕获该书本的编辑动作
 *
 * @param player     玩家
 * @param display    展示名称
 * @param disposable 编辑后销毁
 * @param origin     原始内容
 * @param catcher    编辑动作
 *
 * @project TrMenu
 *
 * @author Score2
 * @since 2021/09/30 14:52
 */
class InputBook(
    val player: Player,
    val display: String,
    val disposable: Boolean,
    val origin: List<String>,
    val catcher: Consumer<List<String>>
) {

    private val book = buildBook {
        bookPages.add(BookBuilder.Text(java.lang.String.join("\n", origin), true))
        name = "§f$display"
        lore.addAll(arrayOf("§0Features Input", if (disposable) "§0Disposable" else ""))
    }

    fun open() {
        // 移除正在编辑的书本
        player.inventory.takeItem(99) { item ->
            item.hasLore("Features Input")
        }
        // 发送书本
        player.inventory.addItem(book)
        inputBookMap[player.name] = catcher
    }

    companion object {

        val inputBookMap: ConcurrentHashMap<String, Consumer<List<String>>> =
            ConcurrentHashMap<String, Consumer<List<String>>>()

        @SubscribeEvent
        fun e(e: PlayerEditBookEvent) {
            val bookLore = e.newBookMeta.lore ?: return
            if (bookLore.size > 0 && bookLore[0] == "§0Features Input") {
                val consumer = inputBookMap[e.player.name] ?: return
                val pages = mutableListOf<String>()
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