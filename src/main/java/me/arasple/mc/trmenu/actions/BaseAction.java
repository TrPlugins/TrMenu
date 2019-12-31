package me.arasple.mc.trmenu.actions;

import me.arasple.mc.trmenu.actions.option.ActionOption;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.HashMap;

/**
 * @author Arasple
 * @date 2019/10/2 9:23
 */
public abstract class BaseAction {

    private String command;
    private HashMap<ActionOption, String> options;

    public BaseAction(String command, HashMap<ActionOption, String> options) {
        this.command = command;
        this.options = options;
    }

    /**
     * 执行动作时的方法
     *
     * @param player 玩家
     * @param e      容器事件
     */
    public abstract void onExecute(Player player, InventoryEvent e);

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public HashMap<ActionOption, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<ActionOption, String> options) {
        this.options = options;
    }

}
