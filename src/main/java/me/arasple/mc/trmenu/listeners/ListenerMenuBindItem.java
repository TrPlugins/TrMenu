package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 22:36
 */
@TListener
public class ListenerMenuBindItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        List<String> lores = getLores(e.getItem());
        if (lores == null) {
            return;
        }

        // 防止 1.9+ 副手双次触发事件
        if (Materials.getVersion() != Materials.MinecraftVersion.VERSION_1_8) {
            if (e.getHand() == EquipmentSlot.OFF_HAND) {
                return;
            }
        }

        TrMenuAPI.getMenus().stream().filter(x -> loreContains(x.getBindItemLore(), lores)).findFirst().ifPresent(menu -> menu.open(e.getPlayer()));
    }

    private boolean loreContains(List<String> m, List<String> n) {
        for (String s : n) {
            for (String s1 : m) {
                if (s.contains(s1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getLores(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return null;
        } else {
            return item.getItemMeta().getLore();
        }
    }

}
