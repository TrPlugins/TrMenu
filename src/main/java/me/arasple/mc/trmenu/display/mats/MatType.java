package me.arasple.mc.trmenu.display.mats;

import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/10/4 16:29
 */
public enum MatType {

    /**
     * 纯粹的原版物品ID
     */
    ORIGINAL("original"),

    /**
     * 带 Model Data 的原版物品 (1.14+)
     */
    MODEL_DATA("model-data"),

    /**
     * 玩家头颅 (支持动态变量)
     */
    PLAYER_HEAD("head|(player-)?head|(variable-)?head"),

    /**
     * 自定义头颅 (自定义材质)
     */
    CUSTOM_HEAD("skull|custom(-head)?|custom(-skull)?"),

    /**
     * Head Database
     */
    HEAD_DATABASE("head-database|hdb"),

    /**
     * 动态读取
     */
    VARIABLE("var(iable)?(s)?");

    private String key;

    MatType(String key) {
        this.key = key;
    }

    public static MatType matchByName(String name) {
        return Arrays.stream(values()).filter(v -> name.matches("^(?i)" + v.getKey())).findFirst().orElse(null);
    }

    public String getKey() {
        return key;
    }

}
