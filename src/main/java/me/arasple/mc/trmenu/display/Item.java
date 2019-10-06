package me.arasple.mc.trmenu.display;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.mats.Mat;
import me.arasple.mc.trmenu.utils.JavaScript;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/4 15:39
 */
public class Item {

    private List<String> names;
    private List<Mat> materials;
    private List<List<String>> lores;
    private List<List<Integer>> slots;
    private List<ItemFlag> itemFlags;

    private String amount;
    private int finalAmount;
    private String shiny;
    private boolean finalShiny;

    private List<Integer> currentSlots;

    private int[] indexs;

    public Item(List<String> names, List<Mat> materials, List<List<String>> lores, List<List<Integer>> slots, List<ItemFlag> itemFlags, String shiny, String amount) {
        this.names = names;
        this.materials = materials;
        this.lores = lores;
        this.slots = slots;
        this.itemFlags = itemFlags;
        this.shiny = shiny;
        this.amount = amount;
        this.finalShiny = Boolean.parseBoolean(shiny);
        this.finalAmount = NumberUtils.toInt(amount, -1);
        resetIndex();
    }

    /**
     * 为一名玩家刷新该物品
     *
     * @param player 玩家
     * @param args   参数
     * @return 物品
     */
    public ItemStack createItemStack(Player player, String... args) {
        // 取动态材质创建物品
        ItemStack itemStack = materials.get(nextIndex(1)).createItem(player);
        if (itemStack == null) {
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        // 载入动态 Lore
        if (lores.size() > 0) {
            itemMeta.setLore(Vars.replace(player, lores.get(nextIndex(2))));
        }
        // 载入动态名称
        if (names.size() > 0) {
            itemMeta.setDisplayName(Vars.replace(player, names.get(nextIndex(0))));
        }
        // 计算并应用发光效果
        if (finalShiny || (!Strings.isBlank(shiny) && (boolean) JavaScript.run(player, shiny))) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemFlags.add(ItemFlag.HIDE_ENCHANTS);
        }
        // 计算并应用动态数量
        if (finalAmount != -1) {
            itemStack.setAmount(finalAmount);
        } else {
            itemStack.setAmount(NumberUtils.toInt(String.valueOf(JavaScript.run(player, amount)), 1));
        }
        // 应用物品标签
        if (itemFlags.size() > 0) {
            itemFlags.forEach(itemFlag -> {
                if (!itemMeta.hasItemFlag(itemFlag)) {
                    itemMeta.addItemFlags(itemFlag);
                }
            });
        }
        itemStack.setItemMeta(itemMeta);
        Metrics.increase(2);
        return itemStack;
    }

    public List<List<Integer>> getSlots() {
        return slots;
    }

    public List<Integer> getNextSlots() {
        currentSlots = slots.get(nextIndex(3));
        return currentSlots;
    }

    private int nextIndex(int type) {
        int size = 0;
        switch (type) {
            case 0:
                size = names.size();
                break;
            case 1:
                size = materials.size();
                break;
            case 2:
                size = lores.size();
                break;
            case 3:
                size = slots.size();
                break;
            default:
                break;
        }
        if (size == 1) {
            return 0;
        }
        int i = indexs[type];
        if (i + 1 >= size) {
            indexs[type] = 0;
        } else {
            indexs[type]++;
        }
        return indexs[type];
    }

    public void resetIndex() {
        this.indexs = new int[]{0, 0, 0, 0};
    }

    public List<Integer> getCurrentSlots() {
        return currentSlots;
    }

}
