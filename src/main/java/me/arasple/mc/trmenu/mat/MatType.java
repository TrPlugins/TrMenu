package me.arasple.mc.trmenu.mat;

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
    MODEL_DATA,

    /**
     * 恒定不变指定正版ID的玩家头颅
     */
    PLAYER_HEAD,

    /**
     * 动态头颅 (PlaceholderAPI 变量)
     */
    VARIABLE_HEAD,

    /**
     * 自定义头颅 (自定义材质)
     */
    CUSTOM_HEAD,

    /**
     * 未知
     */
    UNKNOW

}
