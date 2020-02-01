package me.arasple.mc.trmenu.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.izzel.taboolib.util.Files;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 20:08
 */
public class Skulls {

    private static HashMap<String, String> playerTextures = new HashMap<>();
    private static HashMap<String, ItemStack> skulls = new HashMap<>();

    /**
     * 取得一个自定义头颅
     *
     * @param texture 材质
     * @return 头颅物品
     */
    public static ItemStack getCustomSkull(String texture) {
        return skulls.computeIfAbsent(texture, x -> setTexture(Materials.PLAYER_HEAD.parseItem(), texture));
    }

    /**
     * 设置一个头颅物品的材质
     *
     * @param skull   头颅
     * @param texture 目标材质
     * @return 自定义头颅
     */
    private static ItemStack setTexture(ItemStack skull, String texture) {
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            Field field;
            try {
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                profile.getProperties().put("textures", new Property("textures", texture, null));
                assert meta != null;
                field = meta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(meta, profile);
                skull.setItemMeta(meta);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        return skull;
    }

    /**
     * 取得一个头颅的材质
     *
     * @param skull 头颅物品
     * @return 材质
     */
    public static String getTexture(ItemStack skull) {
        GameProfile profile;
        ItemMeta meta = skull.getItemMeta();
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            profile = (GameProfile) field.get(meta);
            if (profile != null) {
                for (Property prop : profile.getProperties().values()) {
                    if ("textures".equals(prop.getName())) {
                        return prop.getValue();
                    }
                }
            }
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack getPlayerSkull(String id) {
        String texture = getPlayerTexture(id);
        if (texture != null) {
            return skulls.computeIfAbsent(id, x -> setTexture(Materials.PLAYER_HEAD.parseItem(), texture));
        } else {
            return Materials.PLAYER_HEAD.parseItem();
        }
    }

    private static String getPlayerTexture(String id) {
        if (playerTextures.containsKey(id)) {
            return playerTextures.get(id);
        } else {
            playerTextures.put(id, null);
            Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                try {
                    JsonObject userProfile = (JsonObject) new JsonParser().parse(Files.readFromURL("https://api.mojang.com/users/profiles/minecraft/" + id));
                    JsonArray textures = ((JsonObject) new JsonParser().parse(Files.readFromURL("https://sessionserver.mojang.com/session/minecraft/profile/" + userProfile.get("id").getAsString()))).getAsJsonArray("properties");
                    for (JsonElement element : textures) {
                        if ("textures".equals(element.getAsJsonObject().get("name").getAsString())) {
                            playerTextures.put(id, element.getAsJsonObject().get("value").getAsString());
                        }
                    }
                } catch (Throwable ignored) {
                }
            });
        }
        return playerTextures.get(id);
    }

    public static HashMap<String, ItemStack> getSkulls() {
        return skulls;
    }

    private static Skulls instance = new Skulls();

    public static Skulls getInst() {
        return instance;
    }

    public ItemStack getSkull(String id) {
        return id.length() > 150 ? getCustomSkull(id) : getPlayerSkull(id);
    }

}
