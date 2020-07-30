package me.arasple.mc.trmenu.modules.item

import io.izzel.taboolib.util.Strings
import me.arasple.mc.trmenu.modules.item.base.MatchItemIdentifier
import me.arasple.mc.trmenu.modules.item.impl.*

/**
 * @author Arasple
 * @date 2020/3/16 21:42
 */
object ItemIdentifierHandler {

    private val matchers = mutableListOf(
        MatchItemType(),
        MatchItemAmount(),
        MatchItemDamage(),
        MatchItemModelData(),
        MatchItemName(),
        MatchItemLore(),
        MatchItemTexture(),
    )

    fun registerIdentifer(identifier: MatchItemIdentifier) {
        matchers.add(identifier.newInstance())
    }

    fun read(string: String): ItemIdentifier {
        val identifier = ItemIdentifier(string)
        string.split(";").forEach { it ->
            val characteristic = ItemIdentifier.Identifier()
            it.split(",").forEach { string ->
                val args = string.split(":", limit = 2)
                val matcher = this.matchers.firstOrNull { it.name.matches(args[0]) }?.newInstance()
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
        return identifier
    }

    fun write(items: List<ItemIdentifier>): List<String> {
        val list = mutableListOf<String>()
        items.forEach {
            list.add(it.raw)
        }
        return list
    }

}