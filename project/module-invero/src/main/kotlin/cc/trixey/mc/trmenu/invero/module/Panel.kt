package cc.trixey.mc.trmenu.invero.module

import cc.trixey.mc.trmenu.invero.event.PanelRenderEvent

/**
 * @author Arasple
 * @since 2022/10/29 10:59
 */
interface Panel : Parentable {

    fun onRender(e: (event: PanelRenderEvent) -> Unit)

}