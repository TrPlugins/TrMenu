package me.arasple.mc.trmenu.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Arasple
 * @date 2019/10/2 9:23
 */
public abstract class BaseAction {

    private String command;

    public abstract void onExecute(Player player, InventoryClickEvent e, String... args);

    public BaseAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
