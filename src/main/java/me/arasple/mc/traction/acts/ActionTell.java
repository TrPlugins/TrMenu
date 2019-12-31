package me.arasple.mc.traction.acts;

import me.arasple.mc.traction.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/21 20:34
 */
public class ActionTell extends AbstractAction {

    @Override
    public String getName() {
        return "talk|message|send|tell";
    }

    @Override
    public void onExecute(Player player) {
        player.sendMessage(Vars.replace(player, getContent()));
    }

}
