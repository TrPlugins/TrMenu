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

    public abstract void onExecute(Player player, InventoryEvent e, String... args);

    public String getCommand() {
        return command;
    }

    public String getRequirement() {
        return requirement;
    }

    public double getChance() {
        return chance;
    }

    public BaseAction setRequirement(String requirement) {
        this.requirement = requirement;
        return this;
    }

    public BaseAction setChance(double chance) {
        this.chance = chance;
        return this;
    }

}
