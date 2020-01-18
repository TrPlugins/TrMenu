package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.menu.MenuHolder;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/18 21:01
 */
public class ActionIconRefresh extends AbstractAction {

    @Override
    public String getName() {
        return "(icon)?(-)?refresh";
    }

    @Override
    public void onExecute(Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
            Menu menu = ((MenuHolder) player.getOpenInventory().getTopInventory().getHolder()).getMenu();
            menu.getButtons().keySet().forEach(button -> button.refreshConditionalIcon(player, null));
        }
    }

}
