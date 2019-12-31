package me.arasple.mc.trmenu.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Arasple
 * @date 2019/10/4 14:01
 */
public class MenuHolder implements InventoryHolder {

    private Menu menu;

    public MenuHolder(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

}
