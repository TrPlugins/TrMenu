package me.arasple.mc.trmenu.action.acts;

import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.Vars;
import me.arasple.mc.trmenu.utils.TrUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rubenicos
 * @date 2020/6/19 10:50
 */
public class ActionEnchant extends AbstractAction {

    @Override
    public String getName() {
        return "enchant|enchant(-)?(item|slot|armor)";
    }

    @Override
    public void onExecute(Player player) {
        String[] part = getContent(player).split(",", 3);
        Enchantment enchant = Enchantment.getByName(part[1].toUpperCase());
        String[] l = part[2].split("-");
        int level = (l.length > 1 ? TrUtils.getInst().randomInteger(Integer.parseInt(l[0]), Integer.parseInt(l[1])) : Integer.parseInt(l[0]));
        ItemStack item = player.getInventory().getItem(0);

        if (level != 0) {
            if (TrUtils.getInst().isNumber(part[0])) {
                item = player.getInventory().getItem(Integer.parseInt(part[0]));
            }
            if (part[0].toLowerCase().equals("hand")) {
                item = player.getInventory().getItemInMainHand();
            }
            if (part[0].toLowerCase().equals("offhand")) {
                item = player.getInventory().getItemInOffHand();
            }
            if (part[0].toLowerCase().equals("helmet")) {
                item = player.getInventory().getArmorContents()[3];
            }
            if (part[0].toLowerCase().equals("chestplate")) {
                item = player.getInventory().getArmorContents()[2];
            }
            if (part[0].toLowerCase().equals("leggings")) {
                item = player.getInventory().getArmorContents()[1];
            }
            if (part[0].toLowerCase().equals("boots")) {
                item = player.getInventory().getArmorContents()[0];
            }

            if (part[1].toLowerCase().startsWith("custom:") || part[1].toLowerCase().startsWith("c:")) {
                String[] loreConfig = part[1].split(":");
                String lore = (loreConfig[1] + " " + convertLevelString(level)).replace("&", "§");
                ItemMeta meta = item.getItemMeta();
                if (meta.hasLore()) {
                    int lineNumber = Integer.parseInt(loreConfig[2]);
                    List<String> Lore = meta.getLore();
                    if (loreConfig.length == 2 || Lore.size() < lineNumber) {lineNumber = 0;}
                    Lore.add(lineNumber, lore);
                    meta.setLore(Lore);
                } else {
                    List<String> newLore = new ArrayList<>();
                    newLore.add(lore);
                    meta.setLore(newLore);
                }
                item.setItemMeta(meta);
            }

            if (enchant != null) {
                item.addUnsafeEnchantment(enchant, level);
            }
        }
    }

    private String convertLevelString(int i) {
        switch (i) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return i + "";
        }
    }
}
