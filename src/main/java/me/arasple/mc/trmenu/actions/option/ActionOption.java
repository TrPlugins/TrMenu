package me.arasple.mc.trmenu.actions.option;

import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/10/13 11:32
 */
public enum ActionOption {

    /**
     * 动作的参数
     */

    REQUIREMENT("requirement|condition"),
    CHANCE("chance"),
    DELAY("delay|wait"),
    PERMISSION("perm(ission)?");

    private String name;

    ActionOption(String name) {
        this.name = name;
    }

    public static ActionOption matchType(String name) {
        return Arrays.stream(values()).filter(v -> name.matches("^(?i)" + v.getName())).findFirst().orElse(null);
    }

    public String getName() {
        return name;
    }

}
