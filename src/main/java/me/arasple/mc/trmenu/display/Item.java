package me.arasple.mc.trmenu.display;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.mat.Mat;
import me.arasple.mc.trmenu.utils.JavaScript;
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
    private List<ItemFlag> itemFlags;

    private String amount;
    private int finalAmount;
    private String shiny;
    private boolean finalShiny;

    private int[] indexs;

    public Item(List<String> names, List<Mat> materials, List<List<String>> lores, List<ItemFlag> itemFlags, String shiny, String amount) {
        this.names = names;
        this.materials = materials;
        this.lores = lores;
        this.itemFlags = itemFlags;
        this.shiny = shiny;
        this.amount = amount;
        this.finalShiny = Boolean.parseBoolean(shiny);
        this.finalAmount = NumberUtils.toInt(amount, -1);
        resetIndex();
    }

    public ItemStack createItemStack(Player player, String... args) {
        ItemStack itemStack = materials.get(nextIndex(1)).createItem(player);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (lores.size() > 0) {
            itemMeta.setLore(replaceWith(player, lores.get(nextIndex(2)), args));
        }
        if (names.size() > 0) {
            itemMeta.setDisplayName(TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(names.get(nextIndex(0)), args)));
        }
        if (finalShiny || (!Strings.isBlank(shiny) && (boolean) JavaScript.run(player, shiny, null, args))) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemFlags.add(ItemFlag.HIDE_ENCHANTS);
        }
        if (finalAmount != -1) {
            itemStack.setAmount(finalAmount);
        } else {
            itemStack.setAmount(NumberUtils.toInt(String.valueOf(JavaScript.run(player, amount, null, args)), 1));
        }
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

    private List<String> replaceWith(Player player, List<String> strings, String... args) {
        List<String> result = Lists.newArrayList();
        strings.forEach(string -> result.add(TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(string, args))));
        return result;
    }

    private int nextIndex(int type) {
        int size = type == 0 ? names.size() : type == 1 ? materials.size() : lores.size();

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
        this.indexs = new int[]{0, 0, 0};
    }

}
