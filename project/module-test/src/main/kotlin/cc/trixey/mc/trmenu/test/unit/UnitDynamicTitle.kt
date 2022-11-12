package cc.trixey.mc.trmenu.test.unit

import cc.trixey.mc.trmenu.coroutine.launch
import cc.trixey.mc.trmenu.invero.impl.element.BasicItem
import cc.trixey.mc.trmenu.invero.impl.panel.StandardPanel
import cc.trixey.mc.trmenu.invero.impl.window.CompleteWindow
import cc.trixey.mc.trmenu.invero.module.TypeAddress
import cc.trixey.mc.trmenu.invero.util.addElement
import cc.trixey.mc.trmenu.invero.util.addPanel
import cc.trixey.mc.trmenu.invero.util.buildWindow
import cc.trixey.mc.trmenu.test.TestInvero
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/11/12 20:49
 */
object UnitDynamicTitle {

    val command = subCommand {
        execute { player, _, _ ->
            val window = buildWindow<CompleteWindow>(player, TypeAddress.GENERIC_9X3) {
                addPanel<StandardPanel>(9 to 3, 0) {
                    for (s in slots) addElement<BasicItem>(s) {
                        setItem(TestInvero.generateRandomItem())
                    }
                }
            }.also { it.open() }

            // Skedule 在低分辨率 （<3ticks) 下存在抛 “ Already resumed” 异常的问题
            // TODO 研究解决或用 Submit 代替低分辨率场景
            launch(true) {
                repeating(3)
                for (title in dynamicTitles) {
                    window.title = title
                    yield()
                }
                window.title = "Hello Invero"
            }
        }
    }

    private val dynamicTitles by lazy {
        var current = ""
        val titles = mutableListOf<String>()

        "Invero Animated Title".windowed(1, 1).forEachIndexed { _, s ->
            current += s
            titles.add(current)
        }
        titles.addAll(titles.reversed())
        titles
    }

}