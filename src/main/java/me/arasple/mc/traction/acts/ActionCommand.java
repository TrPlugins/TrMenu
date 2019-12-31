package me.arasple.mc.traction.acts;

import me.arasple.mc.traction.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 18:51
 */
public class ActionCommand extends AbstractAction {

    @Override
    public String getName() {
        return "player|command|execute";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.dispatchCommand(player, Vars.replace(player, getContent()));
    }

}
