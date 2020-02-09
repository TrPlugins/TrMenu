package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/2/9 15:44
 */
public class ActionUnknow extends AbstractAction {

    @Override
    public String getName() {
        return "unknown";
    }

    @Override
    public void onExecute(Player player) {
        player.sendMessage("§8[§3Tr§bAction§8] §cUNKNOW | §7Input Action: §f" + getContent());
    }

}
