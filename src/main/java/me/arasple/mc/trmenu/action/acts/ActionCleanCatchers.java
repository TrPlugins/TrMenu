package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.util.lite.Catchers;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/15 16:29
 */
public class ActionCleanCatchers extends AbstractAction {

    @Override
    public String getName() {
        return "catcher(s)?-(clean|clear)|(clear|clean)-catcher(s)?";
    }

    @Override
    public void onExecute(Player player) {
        Catchers.getPlayerdata().remove(player.getName());
    }

}
