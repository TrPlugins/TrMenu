package me.arasple.mc.trmenu.actions.option;

/**
 * @author Arasple
 * @date 2019/10/13 11:32
 */
public enum ActionOption {

    /**
     * 动作的参数
     */

    REQUIREMENT("requirement"),
    CHANCE("chance"),
    DELAY("delay|wait");

    private String name;

    ActionOption(String name) {
        this.name = name;
    }

    public static ActionOption matchType(String name) {
        for (ActionOption value : values()) {
            if (value.getName().matches("^(?i)" + name.toLowerCase())) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

}
