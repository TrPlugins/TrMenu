package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:07
 */
public class ActionOpen extends AbstractAction {

    @Override
    public String getName() {
        return "gui|trmenu|open";
    }

    @Override
    public void onExecute(Player player) {
        Menu menu = TrMenuAPI.getMenu(getContent());
        if (menu != null) {
            menu.open(player);
        }
    }

}
