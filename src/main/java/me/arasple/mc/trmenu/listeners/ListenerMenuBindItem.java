package me.arasple.mc.trmenu.listeners;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arasple
 * @date 2019/10/4 22:36
 */
@TListener
public class ListenerMenuBindItem implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();

        try {
            if (item == null || Materials.getVersion() != Materials.MinecraftVersion.VERSION_1_8) {
                if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    return;
                }
            }
        } catch (Throwable ignored) {
        }

        TrMenuAPI.getMenus().stream().filter(menu -> menu.getBindItemLore() != null && menu.getBindItemLore().stream().anyMatch(lore -> Items.hasLore(item, lore))).findFirst().ifPresent(menu -> {
            menu.open(e.getPlayer());
            e.setCancelled(true);
        });
    }
}
