package me.arasple.mc.trmenu.settings;

/**
 * @author Arasple
 * @date 2019/10/4 17:21
 */
public enum ButtonSettings {

    /**
     * 单个按钮的各种设置
     */

    UPDATE_PERIOD("update"),
    REFRESH_CONDITIONS("refresh-conditions"),
    ICONS("icons"),
    ICONS_CONDITION("condition");

    private String name;

    ButtonSettings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
