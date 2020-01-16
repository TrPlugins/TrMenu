package me.arasple.mc.trmenu.menu;

import me.arasple.mc.trmenu.utils.Maps;

import java.util.Map;

/**
 * @author Arasple
 * @date 2019/10/4 17:21
 */
public enum MenuNodes {

    /**
     * 菜单的设置
     */
    MENU_TITLE("title"),
    MENU_SHAPE("shape"),
    MENU_TYPE("type"),

    MENU_OPEN_REQUIREMENT("open-requirement(s)?"),
    MENU_OPEN_DENY_ACTIONS("open-deny-action(s)?"),
    MENU_CLOSE_REQUIREMENT("close-requirement(s)?"),
    MENU_CLOSE_DENY_ACTIONS("close-deny-action(s)?"),

    MENU_OPEN_COMAMNDS("open-commands"),
    MENU_OPEN_ACTIONS("open-actions"),
    MENU_CLOSE_ACTIONS("close-actions"),

    MENU_OPTIONS("option(s)?"),
    MENU_OPTIONS_DEPEND_EXPANSIONS("depend-expansion(s)?"),
    MENU_OPTIONS_LOCKHAND("lock-player-inv"),
    MENU_OPTIONS_UPDATEINV("update-inventory"),
    MENU_OPTIONS_ARGS("transfer-args"),
    MENU_OPTIONS_FORCEARGS("force-transfer-args"),
    MENU_OPTIONS_BINDLORES("bind-item-lore"),

    MENU_BUTTONS("button(s)?"),

    /**
     * 按钮各种设置
     */
    BUTTON_UPDATE_PERIOD("update(s)?"),
    BUTTON_REFRESH_CONDITIONS("refresh-condition(s)?"),
    BUTTON_ICONS("icons"),
    BUTTON_ICONS_CONDITION("condition"),
    BUTTON_ICONS_PRIORITY("pri(ority)?"),

    ICON_DISPLAY_NAME("name(s)?|displayname(s)?"),
    ICON_DISPLAY_MATERIALS("material(s)?|id(s)?|mat(s)?"),
    ICON_DISPLAY_LORES("lore(s)?"),
    ICON_DISPLAY_SLOTS("slot(s)?"),
    ICON_DISPLAY_FLAGS("flag(s)?"),
    ICON_DISPLAY_SHINY("shiny|glow"),
    ICON_DISPLAY_AMOUNT("amount(s)?");

    private String name;

    MenuNodes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SafeVarargs
    public final <T> T getFromMap(Map<?, ?> map, T... def) {
        return Maps.getSimilarOrDefault(map, getName(), def.length > 0 ? def[0] : null);
    }

}
