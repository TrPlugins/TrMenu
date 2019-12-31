package me.arasple.mc.trmenu.actions.ext;

import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.actions.option.ActionOption;
import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.HashMap;
/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionJs extends BaseAction {

    public IconActionJs(String command, HashMap<ActionOption, String> options) {
        super(command, options);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e) {
        JavaScript.run(player, getCommand(), e);
    }

}
