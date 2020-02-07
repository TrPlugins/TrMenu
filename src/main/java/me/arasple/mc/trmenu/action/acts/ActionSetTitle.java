package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import me.arasple.mc.trmenu.nms.TrMenuNms;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/2/7 21:05
 */
public class ActionSetTitle extends AbstractAction {

    @Override
    public String getName() {
        return "set(-)?title";
    }

    @Override
    public void onExecute(Player player) {
        Menu menu = TrMenuAPI.getMenu(player);
        if (menu != null) {
            TrMenuNms.setTitle(player, player.getOpenInventory().getTopInventory(), Vars.replace(player, getContent()));
        }
    }

}
