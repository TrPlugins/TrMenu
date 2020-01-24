package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
        e.setCancelled(open(e.getPlayer(), (e.getPlayer().isSneaking() ? TrMenu.getSettings().getString("SHORTCUT-OPEN.SNEAKING-OFFHAND", null) : TrMenu.getSettings().getString("SHORTCUT-OPEN.OFFHAND", null))));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void rightClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof Player) {
            e.setCancelled(open(p, (p.isSneaking() ? TrMenu.getSettings().getString("SHORTCUT-OPEN.SNEAKING-RIGHT-CLICK-PLAYER", null) : TrMenu.getSettings().getString("SHORTCUT-OPEN.RIGHT-CLICK-PLAYER", null)), e.getRightClicked().getName()));
        }
    }

    private boolean open(Player player, String read, String... args) {
        String[] menu = read != null ? read.split("\\|") : null;
        if (menu != null) {
            Menu trMenu = TrMenuAPI.getMenu(menu[0]);
            String perm = menu.length > 1 ? menu[1] : null;
            if (!((perm != null && !player.hasPermission(perm)) || trMenu == null)) {
                trMenu.open(player, args);
                return true;
            }
        }
        return false;
    }

}
