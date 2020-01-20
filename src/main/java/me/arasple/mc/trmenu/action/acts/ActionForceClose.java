package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author Arasple
 * @date 2020/1/20 12:38
 */
public class ActionForceClose extends AbstractAction {

    @Override
    public String getName() {
        return "force(-)?(close|shut)";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(TrMenu.getPlugin(), () -> {
            player.setMetadata("TrMenu.Force-Close", new FixedMetadataValue(TrMenu.getPlugin(), "Close"));
            player.closeInventory();
            player.removeMetadata("TrMenu.Force-Close", TrMenu.getPlugin());
        });
    }

}
