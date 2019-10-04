package me.arasple.mc.trmenu.actions.ext;

import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionClose extends BaseAction {

    public IconActionClose(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e, String... args) {
        player.closeInventory();
    }

}
