package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:07
 */
public class ActionClose extends AbstractAction {

    @Override
    public String getName() {
        return "close|shut";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(TrMenu.getPlugin(), player::closeInventory);
    }

}
