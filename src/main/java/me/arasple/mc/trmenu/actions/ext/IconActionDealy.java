package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionDealy extends BaseAction {

    public IconActionDealy(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e) {
    }

    public long getDelay() {
        return NumberUtils.toLong(getCommand(), -1);
    }

}
