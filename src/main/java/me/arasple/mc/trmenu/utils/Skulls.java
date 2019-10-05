package me.arasple.mc.trmenu.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.izzel.taboolib.util.lite.Materials;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 20:08
 */
public class Skulls {

    private static HashMap<String, ItemStack> skulls = new HashMap<>();

    /**
     * 取得一个自定义头颅
     *
     * @param texture 材质
     * @return 头颅物品
     */
    public static ItemStack getCustomSkull(String texture) {
        return skulls.computeIfAbsent(texture, x -> setTexture(Materials.matchMaterials("PLAYER_HEAD").parseItem(), texture));
    }

    /**
     * 设置一个头颅物品的材质
     *
     * @param skull   头颅
     * @param texture 目标材质
     * @return 自定义头颅
     */
    public static ItemStack setTexture(ItemStack skull, String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Field field;
        try {
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            profile.getProperties().put("textures", new Property("textures", texture, null));
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
            skull.setItemMeta(meta);
            return skull;
        } catch (ClassCastException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack getPlayerSkull(String id) {
        return skulls.computeIfAbsent(id, x -> {
            ItemStack itemStack = Materials.matchMaterials("PLAYER_HEAD").parseItem();
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwner(id);
            return itemStack;
        });
    }

    public static HashMap<String, ItemStack> getSkulls() {
        return skulls;
    }

}
