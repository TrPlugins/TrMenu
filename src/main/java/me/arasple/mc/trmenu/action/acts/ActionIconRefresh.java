package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.menu.Menu;
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
        Menu menu = TrMenuAPI.getMenu(player);
        if (menu != null) {
            menu.open(player, menu.getShape(player), true, ArgsCache.getPlayerArgs(player));
        }
    }

}
