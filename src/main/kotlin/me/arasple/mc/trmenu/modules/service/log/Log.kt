package me.arasple.mc.trmenu.modules.service.log

/**
 * @author Arasple
 * @date 2020/7/21 14:39
 */
enum class Log(val format: String) {

    EVENT_MENU_OPEN("Player: {0}, Page: {1}, Reason: {2}"),
    EVENT_MENU_CLOSE("Player: {0}, Page: {1}, Reason: {2}, IsSilent: {3}"),
    EVENT_MENU_CLICK("Player: {0}, Page: {1}, Slot: {2}, ClickType: {3}, Icon: {4}"),

}