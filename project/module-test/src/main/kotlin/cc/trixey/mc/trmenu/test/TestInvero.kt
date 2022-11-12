package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.test.unit.UnitDynamicItem
import cc.trixey.mc.trmenu.test.unit.UnitDynamicTitle
import cc.trixey.mc.trmenu.test.unit.UnitPrinter
import cc.trixey.mc.trmenu.test.unit.UnitStandard
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.isAir

/**
 * @author Arasple
 * @since 2022/10/29 10:51
 */
@CommandHeader(name = "testInvero")
object TestInvero {

    /**
     * 测试动态标题性能
     */
    @CommandBody
    val testDynamicTitle = UnitDynamicTitle.command

    /**
     * 测试常规面板和窗口
     *
     * StandardPanel / StandardPagedPanel
     */
    @CommandBody
    val testStandardPanels = UnitStandard.command

    /**
     * 测试修改 PosMark 实现 Panel 位移
     */
    @CommandBody
    val testStandardPanels_PosMark = UnitStandard.posMark

    /**
     * 测试动态物品滚动的效果
     */
    @CommandBody
    val testDynamicItem = UnitDynamicItem.normal

    /**
     * 测试基于多页实现的独立的动态物品滚动效果
     */
    @CommandBody
    val testDynamicItem_Paged = UnitDynamicItem.paged

    /**
     * 打印一些信息
     */
    @CommandBody
    val print = UnitPrinter.print

    fun generateRandomItem(): ItemStack {
        var itemStack: ItemStack? = null
        while (itemStack == null || itemStack.isAir) {
            itemStack = XMaterial.values().random().parseItem()
        }
        return itemStack
    }

}