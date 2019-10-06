package me.arasple.mc.trmenu.actions.ext;

import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionMessage extends BaseAction {

    public IconActionMessage(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e) {
        player.sendMessage(Vars.replace(player, getCommand()));
    }

}
