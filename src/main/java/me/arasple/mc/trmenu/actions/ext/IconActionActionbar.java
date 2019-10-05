package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/5 14:33
 */
public class IconActionActionbar extends BaseAction {

    public IconActionActionbar(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e, String... args) {
        TLocale.Display.sendActionBar(player, TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(getCommand(), args)));
    }

}
