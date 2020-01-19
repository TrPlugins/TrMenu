package me.arasple.mc.trmenu.data;

import me.arasple.mc.trmenu.display.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 20:04
 */
public class ArgsCache {

    private static HashMap<UUID, String[]> args = new HashMap<>();
    private static HashMap<UUID, InventoryClickEvent> event = new HashMap<>();
    private static HashMap<UUID, Integer> heldSlot = new HashMap<>();
    private static HashMap<UUID, Item> clickedItem = new HashMap<>();

    public static HashMap<UUID, String[]> getArgs() {
        return args;
    }

    public static String[] getPlayerArgs(Player player) {
        return args.getOrDefault(player.getUniqueId(), new String[0]);
    }

    public static HashMap<UUID, InventoryClickEvent> getEvent() {
        return event;
    }

    public static HashMap<UUID, Integer> getHeldSlot() {
        return heldSlot;
    }

    public static HashMap<UUID, Item> getClickedItem() {
        return clickedItem;
    }

}
