package cc.trixey.trmenu.common

/**
 * TrMenu
 * cc.trixey.trmenu.common.Icon
 *
 * 通过 Layout 指定槽位的 Icon 应该是一个结构体，可以在不同 Menu 中任意调度
 * 而在 Icon 下配置槽位的，应该是属于 Icon 本身属性，可以分离出不含槽位的结构体再调度
 *
 * @author Arasple
 * @since 2023/1/13 13:53
 */
interface Icon {

    fun getName()

}