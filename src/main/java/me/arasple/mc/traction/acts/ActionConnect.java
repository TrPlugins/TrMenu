package me.arasple.mc.traction.acts;

import me.arasple.mc.traction.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Bungees;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:07
 */
public class ActionConnect extends AbstractAction {
    @Override
    public String getName() {
        return "connect|bungee|server";
    }

    @Override
    public void onExecute(Player player) {
        Bungees.connect(player, Vars.replace(player, getContent()));
    }
}
