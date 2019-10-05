package me.arasple.mc.trmenu.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/2 9:23
 */
public abstract class BaseAction {

    private String requirement;
    private String command;
    private double chance;
    public BaseAction(String command) {
        this.command = command;
    }

    public BaseAction(String requirement, String command, double chance) {
        this.requirement = requirement;
        this.command = command;
        this.chance = chance;
    }


    /**
     * 执行动作时的方法
     *
     * @param player 玩家
     * @param e      容器事件
     * @param args   参数
     */
    public abstract void onExecute(Player player, InventoryEvent e, String... args);

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRequirement() {
        return requirement;
    }

    public BaseAction setRequirement(String requirement) {
        this.requirement = requirement;
        return this;
    }

    public double getChance() {
        return chance;
    }

    public BaseAction setChance(double chance) {
        this.chance = chance;
        return this;
    }

}
