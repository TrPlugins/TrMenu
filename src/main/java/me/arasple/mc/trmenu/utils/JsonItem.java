package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.gson.*;
import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.module.nms.nbt.NBTCompound;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bkm016
 * @date 2020/1/20 15:18
 */
public class JsonItem {

    public static ItemStack fromJson(String item) {
        JsonElement json = new JsonParser().parse(item);
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
        json.addProperty("type", item.getType().name());
        json.addProperty("data", item.getData().getData());
        json.addProperty("amount", item.getAmount());
        json.add("meta", new JsonParser().parse(NMS.handle().loadNBT(item).toJson()));
        return json.toString();
    }

    public static String toJsonFormatted(ItemStack item) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new Gson().toJsonTree(toJson(item)));
    }

    public static boolean isJson(String string) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(string);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }

}
