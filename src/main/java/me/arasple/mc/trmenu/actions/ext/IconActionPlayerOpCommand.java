package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionPlayerOpCommand extends BaseAction {

    public IconActionPlayerOpCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e, String... args) {
        boolean isOp = player.isOp();
        player.setOp(true);
        Bukkit.dispatchCommand(player, TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(getCommand(), args)));
        player.setOp(isOp);
    }

}
