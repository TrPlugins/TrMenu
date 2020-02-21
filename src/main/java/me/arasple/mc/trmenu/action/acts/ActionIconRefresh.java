package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.menu.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Arasple
 * @date 2020/1/18 21:01
 */
public class ActionIconRefresh extends AbstractAction {

    @Override
    public String getName() {
        return "(icon)?(-)?(refresh|update)";
    }

    @Override
    public void onExecute(Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        Menu menu = TrMenuAPI.getMenu(player);

        if (inv instanceof MenuHolder && menu != null) {
            Button clicked = ArgsCache.getClickedButtons().get(player.getUniqueId());
            if (clicked != null) {
                clicked.refreshConditionalIcon(player, null);
                menu.setButton(player, clicked, inv, menu.getButtons().get(clicked).getSlots(menu.getShape(player)), ArgsCache.getPlayerArgs(player));
            }
        }
    }

}
