package trplugins.menu.api.receptacle

/**
 * TrMenu
 * trplugins.menu.api.receptacle.ReceptacleLayout
 *
 * @author Score2
 * @since 2022/03/05 14:31
 */
open class ReceptacleLayout(val slotRange: IntRange) {

    val mainInvSlots = (slotRange.last + 1..slotRange.last + 27).toList()

    val hotBarSlots = (mainInvSlots.last() + 1..mainInvSlots.last() + 9).toList()

    val containerSlots = slotRange.toList()

    val containerSize = containerSlots.size + 1

    val totalSlots = (0..hotBarSlots.last()).toList()

    val totalSize = totalSlots.size
}