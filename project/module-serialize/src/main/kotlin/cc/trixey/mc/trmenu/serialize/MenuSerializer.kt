package cc.trixey.mc.trmenu.serialize

import cc.trixey.mc.trmenu.api.menu.Menu
import cc.trixey.mc.trmenu.util.Conclusion
import cc.trixey.mc.trmenu.util.fromClassesCollect
import taboolib.common.io.runningClasses
import taboolib.common.util.resettableLazy
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.module.configuration.Configuration

/**
 * TrMenu
 * cc.trixey.mc.trmenu.serialize.MenuSerializer
 *
 * @author Score2
 * @since 2022/12/10 0:36
 */
interface MenuSerializer {

    fun serializeMenu(conf: Configuration): Conclusion<Menu>

    companion object {

        /**
         * ResettableLazy.reset("plugin-bootstrap")
         */
        private val serializers by resettableLazy("plugin-bootstrap") {
            runningClasses.fromClassesCollect(MenuSerializer::class.java).map {
                Conclusion<MenuSerializer>().prove { it.invokeConstructor() }
            }.mapNotNull {
                if (it.isAbsoluteSuccessful) {
                    return@mapNotNull it.get()
                } else {
                    // TODO 发送失败原因的消息
                }

                return@mapNotNull null
            }
        }

        fun <T> serialize() {





        }

    }
}