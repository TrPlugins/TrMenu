package me.arasple.mc.trmenu.mats;

/**
 * @author Arasple
 * @date 2019/10/4 16:29
 */
public enum MatType {

    /**
     * 纯粹的原版物品ID
     */
    ORIGINAL,

    /**
     * 带 Model Data 的原版物品 (1.14+)
     */
    MODEL_DATA("model-data", "model"),

    /**
     * 玩家头颅 (支持动态变量)
     */
    PLAYER_HEAD("player-head", "variable-head", "head"),

    /**
     * 自定义头颅 (自定义材质)
     */
    CUSTOM_HEAD("custom-head", "custom-skull", "skull"),

    /**
     * Head Database
     */
    HEAD_DATABASE("head-database", "hdb");

    private String[] keys;

    MatType(String... keys) {
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }

    public static MatType matchByName(String name) {
        for (MatType value : values()) {
            if (value.getKeys() != null) {
                for (String key : value.getKeys()) {
                    if (name.equalsIgnoreCase(key)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

}
