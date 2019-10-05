package me.arasple.mc.trmenu.settings;

/**
 * @author Arasple
 * @date 2019/10/4 17:21
 */
public enum MenurSettings {

    /**
     * 菜单的设置
     */
    MENU_TITLE("title"),
    MENU_SHAPE("shape"),
    MENU_OPEN_COMAMNDS("open-commands"),
    MENU_OPEN_ACTIONS("open-actions"),
    MENU_CLOSE_ACTIONS("close-actions"),
    MENU_OPTIONS("option(s)?"),
    MENU_OPTIONS_LOCKHAND("lock-player-inv"),
    MENU_OPTIONS_ARGS("transfer-args"),
    MENU_OPTIONS_FORCEARGS("force-transfer-args"),
    MENU_OPTIONS_BINDLORES("bind-item-lore"),
    MENU_BUTTONS("button(s)?"),

    /**
     * 按钮各种设置
     */
    BUTTON_UPDATE_PERIOD("update"),
    BUTTON_REFRESH_CONDITIONS("refresh-conditions"),
    BUTTON_ICONS("icons"),
    BUTTON_ICONS_CONDITION("condition"),

    ICON_DISPLAY_NAME("name|displayname"),
    ICON_DISPLAY_MATERIALS("material(s)?|id|mat(s)?"),
    ICON_DISPLAY_LORES("lore(s)?"),
    ICON_DISPLAY_ATTRIBUTES("attribute(s)?");

    private String name;

    MenurSettings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
