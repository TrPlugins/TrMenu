package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.util.Commands;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/22 10:22
 */
public class ActionCommandConsole extends AbstractAction {

    @Override
    public String getName() {
        return "console";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(TrMenu.getPlugin(), () -> Commands.dispatchCommand(Bukkit.getConsoleSender(), getContent(player)));
    }

}
