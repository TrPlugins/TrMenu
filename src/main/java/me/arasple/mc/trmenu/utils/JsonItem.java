package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.gson.*;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.module.nms.nbt.NBTCompound;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author Bkm016
 * @date 2020/1/20 21:41
 */
public class JsonItem {

    public static ItemStack fromJson(String item) {
        JsonElement json = new JsonParser().parse(TLocale.Translate.setColored(item));
        if (json instanceof JsonObject) {
            ItemBuilder itemBuilder = new ItemBuilder(Material.STONE);
            JsonElement type = ((JsonObject) json).get("type");
            if (type != null) {
                itemBuilder.material(Items.asMaterial(type.getAsString()));
            }
            JsonElement data = ((JsonObject) json).get("data");
            if (data != null) {
                itemBuilder.damage(data.getAsInt());
            }
            JsonElement amount = ((JsonObject) json).get("amount");
            if (amount != null) {
                itemBuilder.amount(amount.getAsInt());
            }
            ItemStack itemBuild = itemBuilder.build();
            JsonElement meta = ((JsonObject) json).get("meta");
            if (meta != null) {
                return NMS.handle().saveNBT(itemBuild, NBTCompound.fromJson(meta.toString()));
            }
            return itemBuild;
        }
        return null;
    }

    public static String toJson(ItemStack item) {
        JsonObject json = new JsonObject();
        String type = item.getType().name();
        byte data = item.getData().getData();
        int amount = item.getAmount();

        json.addProperty("type", item.getType().name());
        if (data > 0) {
            json.addProperty("data", data);
        }
        if (amount > 1) {
            json.addProperty("amount", amount);
        }
        if (item.hasItemMeta()) {
            // Uncolor
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName().replace('§', '&'));
            }
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                lore.replaceAll(s -> s.replace('§', '&'));
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            json.add("meta", new JsonParser().parse(NMS.handle().loadNBT(item).toJson()));
        }
        return json.toString();
    }

    public static String toJsonFormatted(ItemStack item) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new Gson().toJsonTree(toJson(item)));
    }

    public static boolean isJson(String string) {
        try {
            return new JsonParser().parse(string).isJsonObject();
        } catch (Throwable e) {
            return false;
        }
    }

}
