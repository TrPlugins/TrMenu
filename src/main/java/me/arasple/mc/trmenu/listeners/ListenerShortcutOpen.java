package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * @author Arasple
 * @date 2020/1/23 18:57
 */
@TListener(condition = "isRegistrable")
public class ListenerShortcutOpen implements Listener {

    public boolean isRegistrable() {
        return Materials.isVersionOrHigher(Materials.MinecraftVersion.V1_9);
    }

    @EventHandler
    public void offHand(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        String read = (p.isSneaking() ? TrMenu.getSettings().getString("SHORTCUT-OPEN.SNEAKING-OFFHAND", null) : TrMenu.getSettings().getString("SHORTCUT-OPEN.OFFHAND", null));
        String[] menu = read != null ? read.split("\\|") : null;
        if (menu != null) {
            Menu trMenu = TrMenuAPI.getMenu(menu[0]);
            String perm = menu.length > 1 ? menu[1] : null;
            if (!((perm != null && !p.hasPermission(perm)) || trMenu == null)) {
                trMenu.open(p);
            }
        }
    }

}
