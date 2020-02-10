package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
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
        Bukkit.getScheduler().runTask(TrMenu.getPlugin(), () -> {
            boolean isOp = player.isOp();
            player.setOp(true);
            player.chat("/" + getContent(player));
            player.setOp(isOp);
        });
    }

}
