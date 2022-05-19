package trplugins.menu.module.internal.inputer.inputs

import trplugins.menu.util.parseSimplePlaceholder
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.reflect.Reflex.Companion.getProperty
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.PacketReceiveEvent
import taboolib.module.nms.getI18nName
import trplugins.menu.api.receptacle.vanilla.window.WindowReceptacle
import taboolib.platform.util.buildItem
import trplugins.menu.api.receptacle.vanilla.window.WindowLayout
import java.util.concurrent.ConcurrentHashMap

/**
 * @project TrMenu
 *
 * @author Score2
 * @since 2021/09/29 19:21
 */
class InputAnvil(
    val player: Player,
    val title: String,
    val items: Map<Int, ItemStack?>,
    val respond: (String) -> Boolean,
) {

    val receptacle = WindowReceptacle(WindowLayout.ANVIL, title)
    var inputText: String
    private var succeed = false

    init {
        items.forEach {
            receptacle.setElement(it.value, it.key)
        }
        if (!receptacle.hasElement(0)) {
            receptacle.setElement(ANVIL_EMPTY_ITEM, 0)
        }
        inputText = receptacle.getElement(0)?.let {
            if (it.itemMeta?.hasDisplayName() == true) {
                it.itemMeta?.displayName
            } else {
                it.getI18nName(player)
            }
        } ?: ""
        receptacle.onClick = onClick@{ _, e ->
            e.isCancelled = true
            if (e.slot == 2) {
                succeed = true
                receptacle.close()
                return@onClick
            }
            receptacle.refresh(0)
            receptacle.refresh(1)
            if (receptacle.hasElement(2)) {
                receptacle.refresh(2)
            }
            (3..38).forEach { receptacle.refresh(it) }
        }
        receptacle.onClose = { player, _ ->
            inputs.remove(player)
            if (succeed)
                respond(inputText)
            else
                respond("ANVIL_CANCEL")
        }
    }

    fun open() {
        inputs[player] = this
        receptacle.open(player)
    }

    companion object {
        val ANVIL_EMPTY_ITEM by lazy {
            buildItem(XMaterial.STONE) {
                name = "UNKNOWN_ITEM"
            }
        }

        val inputs = ConcurrentHashMap<Player, InputAnvil>()

        @SubscribeEvent
        fun e(e: PacketReceiveEvent) {
            if (e.packet.name != "PacketPlayInItemName") {
                return
            }
            val input = inputs[e.player] ?: return
            val inputText = e.packet.source.getProperty<String>("a")!!

            input.receptacle.getElement(2)?.let {
                it.itemMeta?.let {
                    if (!it.hasDisplayName()) return@let
                    it.setDisplayName(
                        it.displayName.parseSimplePlaceholder(mapOf("(?i)@cached@".toRegex() to inputText))
                    )
                }
                input.receptacle.refresh(2)
            }
            input.inputText = inputText
        }
    }
}
