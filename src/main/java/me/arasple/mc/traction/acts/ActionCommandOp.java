package me.arasple.mc.traction.acts;

import me.arasple.mc.traction.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 18:51
 */
public class ActionCommandOp extends AbstractAction {

    @Override
    public String getName() {
        return "op";
    }

    @Override
    public void onExecute(Player player) {
        boolean isOp = player.isOp();
        player.setOp(true);
        Bukkit.dispatchCommand(player, Vars.replace(player, getContent()));
        player.setOp(isOp);
    }

}
