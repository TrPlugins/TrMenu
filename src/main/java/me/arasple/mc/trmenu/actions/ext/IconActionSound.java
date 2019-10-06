package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionSound extends BaseAction {

    public IconActionSound(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e) {
        new SoundPack(getCommand()).play(player);
    }

}
