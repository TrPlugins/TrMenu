package me.arasple.mc.trmenu.actions.ext;

import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.actions.option.ActionOption;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menur;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.HashMap;
/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionOpen extends BaseAction {

    public IconActionOpen(String command, HashMap<ActionOption, String> options) {
        super(command, options);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e) {
        Menur menu = TrMenuAPI.getMenu(getCommand());
        if (menu != null) {
            menu.open(player);
        }
    }

}
