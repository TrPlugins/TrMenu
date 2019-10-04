package me.arasple.mc.trmenu.settings;

/**
 * @author Arasple
 * @date 2019/10/4 17:21
 */
public enum IconSettings {

    /**
     * 单个图标的各种设置
     */

    DISPLAY_NAME("name|displayname"),
    DISPLAY_MATERIALS("material(s)?|id|mat(s)?"),
    DISPLAY_LORES("lore(s)?");

    private String name;

    IconSettings(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
