package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionMessage extends BaseAction {

    public IconActionMessage(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryClickEvent e, String... args) {
        player.sendMessage(TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(getCommand(), args)));
    }

}
