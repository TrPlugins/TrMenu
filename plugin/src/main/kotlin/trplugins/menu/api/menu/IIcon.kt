package trplugins.menu.api.menu

import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.display.icon.Icon
import trplugins.menu.module.display.icon.IconProperty

/**
 * @author Arasple
 * @date 2021/1/24 20:54
 * @see Icon
 */
interface IIcon {

    /**
     * 为一个会话加载图标功能
     *
     * @param session 菜单会话
     *
     */
    fun startup(session: MenuSession)

    /**
     * 显示图标属性
     */
    fun settingItem(session: MenuSession, icon: IconProperty, lastMenuId: String?)

    /**
     * 更新显示属性 & 动画
     */
    fun onUpdate(session: MenuSession, frames: Set<Int>)

    /**
     * 重新计算优先级, 筛选子图标
     */
    fun onRefresh(session: MenuSession)

    /**
     * 重置会话缓存
     */
    fun onReset(session: MenuSession)

    /**
     * 取得针对当前会话有效的图标属性
     */
    fun getProperty(session: MenuSession): IconProperty

    /**
     * 判断该图标是否对该会话可用
     */
    fun isAvailable(session: MenuSession): Boolean

}