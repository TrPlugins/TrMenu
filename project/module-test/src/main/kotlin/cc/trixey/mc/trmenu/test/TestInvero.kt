package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.test.unit.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
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
     * 测试生成器命令
     */
    @CommandBody
    val testGenerator = UnitGenerator.paged

    /**
     * 测试常规面板和窗口
     *
     * StandardPanel / PagedStandardPanel
     */
    @CommandBody
    val testStandard = UnitStandard.command

    /**
     * 测试修改 PosMark 实现 Panel 位移
     */
    @CommandBody
    val testStandard_Mark = UnitStandard.posMark

    /**
     * 测试嵌套面板
     */
    @CommandBody
    val testNested = UnitNested.command

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
     * 测试物品输入与输出 Panel
     */
    @CommandBody
    val testIO = UnitIO.command


    /**
     * 测试滚动页面
     */
    @CommandBody
    val testScrollStandard_Vertical = UnitScroll.vertical

    /**
     * 测试滚动页面
     */
    @CommandBody
    val testScrollStandard_Horizontal = UnitScroll.horizontal

    /**
     * 测试滚动页面
     */
    @CommandBody
    val testScrollGenerator = UnitGenerator.scroll

    /**
     * 打印一些信息
     */
    @CommandBody
    val print = UnitPrinter.print

}

fun generateRandomItem(builder: ItemBuilder.() -> Unit = {}): ItemStack {
    var itemStack: ItemStack? = null
    while (itemStack.isAir()) {
        itemStack = ItemStack(Material.values().filter {
            it.isItem || it.isBlock
        }.random())
    }
    return buildItem(itemStack!!, builder).also {
        it.amount = it.amount.coerceAtMost(it.maxStackSize)
    }
}