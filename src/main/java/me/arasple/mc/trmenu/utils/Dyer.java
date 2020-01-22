package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * @author Arasple, Bkm016
 * @date 2020/1/14 21:52
 */
public class Dyer {

    /**
     * @author Bkm016
     */
    public static void setBanner(BannerMeta itemMeta, List<String> patterns) {
        patterns.forEach(pattern -> {
            String[] type = pattern.split(" ");
            if (type.length == 1) {
                try {
                    itemMeta.setBaseColor(DyeColor.valueOf(type[0].toUpperCase()));
                } catch (Exception ignored) {
                    itemMeta.setBaseColor(DyeColor.BLACK);
                }
            } else if (type.length == 2) {
                try {
                    itemMeta.addPattern(new Pattern(DyeColor.valueOf(type[0].toUpperCase()), PatternType.valueOf(type[1].toUpperCase())));
                } catch (Exception e) {
                    itemMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.BASE));
                }
            }
        });
    }

    public static void setLeather(LeatherArmorMeta meta, String color) {
        String[] rgb = color.split(",");
        if (rgb.length == 3) {
            int r = NumberUtils.toInt(rgb[0], 0);
            int g = NumberUtils.toInt(rgb[1], 0);
            int b = NumberUtils.toInt(rgb[2], 0);
            meta.setColor(Color.fromRGB(r, g, b));
        }
    }

}
