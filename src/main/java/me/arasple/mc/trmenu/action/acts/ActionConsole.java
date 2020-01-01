package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/22 10:22
 */
public class ActionConsole extends AbstractAction {

    @Override
    public String getName() {
        return "console";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Vars.replace(player, getContent()));
    }

}
