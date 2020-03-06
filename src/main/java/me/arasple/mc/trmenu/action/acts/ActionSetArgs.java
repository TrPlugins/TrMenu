package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.data.ArgsCache;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/18 20:48
 */
public class ActionSetArgs extends AbstractAction {

    @Override
    public String getName() {
        return "set(-)?args";
    }

    @Override
    public void onExecute(Player player) {
        ArgsCache.updateArgs(player.getUniqueId(), getContent(player).split(" "));
    }

}
