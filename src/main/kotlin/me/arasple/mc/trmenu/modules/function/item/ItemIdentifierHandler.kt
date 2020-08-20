package me.arasple.mc.trmenu.modules.function.item

import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.modules.function.item.base.MatchItemIdentifier
import me.arasple.mc.trmenu.modules.function.item.impl.*

/**
 * @author Arasple
 * @date 2020/3/16 21:42
 */
object ItemIdentifierHandler {

    private val MATCHERS = mutableListOf(
        MatchItemType(),
        MatchItemAmount(),
        MatchItemDamage(),
        MatchItemModelData(),
        MatchItemName(),
        MatchItemLore(),
        MatchItemTexture(),
    )
    private val CACHES = mutableMapOf<String, ItemIdentifier>()

    fun registerIdentifer(identifier: MatchItemIdentifier) {
        MATCHERS.add(identifier.newInstance())
    }

    fun read(string: String): ItemIdentifier =
        CACHES.computeIfAbsent(string) {
            val identifier = ItemIdentifier(string)
            string.split(";").forEach { part ->
                val characteristic = ItemIdentifier.Identifier()
                part.split(",").forEach { string ->
                    val args = string.split(":", limit = 2)
                    val matcher = MATCHERS.firstOrNull { it.name.matches(args[0]) }?.newInstance()
                    if (matcher == null && args.size == 1 && !Strings.isBlank(args[0])) {
                        characteristic.characteristic.add(MatchItemLore().let {
                            it.setContent(args[0])
                            return@let it
                        })
                    } else if (args.size > 1 && matcher != null) {
                        matcher.setContent(args[1])
                        characteristic.characteristic.add(matcher)
                    }
                }
                identifier.identifiers.add(characteristic)
            }
            identifier
        }

    fun write(items: List<ItemIdentifier>): List<String> {
        val list = mutableListOf<String>()
        items.forEach {
            list.add(it.raw)
        }
        return list
    }

}