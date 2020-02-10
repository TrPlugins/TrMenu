package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
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
        Bungees.connect(player, getContent(player));
    }
}
