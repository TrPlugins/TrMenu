package me.arasple.mc.trmenu.actions.ext;

import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;

/**
 * @author Arasple
 * @date 2019/10/4 18:24
 */
public class IconActionSoundAll extends BaseAction {

    public IconActionSoundAll(String command) {
        super(command);
    }

    @Override
    public void onExecute(Player player, InventoryEvent e, String... args) {
        SoundPack soundPack = new SoundPack(getCommand());
        Bukkit.getOnlinePlayers().forEach(soundPack::play);
    }

}
