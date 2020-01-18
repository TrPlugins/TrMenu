package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.util.ArrayUtil;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:07
 */
public class ActionOpen extends AbstractAction {

    @Override
    public String getName() {
        return "gui|trmenu|menu|open";
    }

    @Override
    public void onExecute(Player player) {
        Menu menu = TrMenuAPI.getMenu(getContent().split(" ")[0]);
        if (menu != null) {
            menu.open(player, true, ArrayUtil.arrayJoin(getContent().split(" "), 1));
        } else {
            player.sendMessage(ChatColor.RED + "Menu " + ChatColor.YELLOW + getContent() + " does not exist");
        }
    }

}
