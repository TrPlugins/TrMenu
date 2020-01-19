package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/19 10:24
 */
public class ActionClearEmptySlots extends AbstractAction {

    @Override
    public String getName() {
        return "clear(-)?slot(s)?";
    }

    @Override
    public void onExecute(Player player) {
        Menu menu = TrMenuAPI.getMenu(player);
        if (menu != null) {
            menu.clearEmptySlots(player, player.getOpenInventory().getTopInventory());
        }
    }
}
