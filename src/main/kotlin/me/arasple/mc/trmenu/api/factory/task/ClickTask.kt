package me.arasple.mc.trmenu.api.factory.task

import me.arasple.mc.trmenu.api.factory.MenuFactory
import me.arasple.mc.trmenu.api.inventory.InvClickType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/7/20 20:21
 */
fun interface ClickTask {

    fun run(event: Event): Action

    class Event(val player: Player, val session: MenuFactory.Session, val factory: MenuFactory, val slot: Int, val key: String, val clicked: ItemStack?, val cursor: ItemStack?, val type: InvClickType)

    enum class Action {

        /**
         * 取消指针选取的物品
         */
        CANCEL_CUROSE,

        /**
         * 取消指针选取的物品，并恢复该位置上的物品
         */
        CANCEL_MODIFY,

        /**
         * 完全允许访问数据包物品
         */
        ACCESS

    }

}