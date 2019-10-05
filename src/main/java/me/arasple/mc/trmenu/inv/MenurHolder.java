package me.arasple.mc.trmenu.inv;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Arasple
 * @date 2019/10/4 14:01
 */
public class MenurHolder implements InventoryHolder {

    private Menur menu;

    public MenurHolder(Menur menu) {
        this.menu = menu;
    }

    public Menur getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

}
