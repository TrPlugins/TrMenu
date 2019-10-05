package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Arasple
 * @date 2019/10/4 16:36
 */
public class MaterialUtils {

    public static String[] readNewMaterialForOld(String material) {
        if (NumberUtils.toInt(material, -1) != -1) {
            int id = NumberUtils.toInt(material, -1);
            for (Material value : Material.values()) {
                if (value.getId() == id) {
                    return new String[]{value.name(), "0"};
                }
            }
            return new String[]{Material.STONE.name(), "0"};
        } else {
            try {
                return new String[]{Material.valueOf(material).name(), "0"};
            } catch (Throwable e) {
                Materials materials;
                try {
                    materials = Materials.valueOf(material);
                } catch (Throwable e1) {
                    materials = Arrays.stream(Materials.values())
                            .filter(m -> Strings.similarDegree(m.name(), material) > TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))
                            .max(Comparator.comparingDouble(x -> Strings.similarDegree(x.name(), material)))
                            .orElse(Materials.STONE);
                }
                return new String[]{materials.parseMaterial().name(), String.valueOf(materials.getData())};
            }
        }
    }

    public static Material readMaterial(String material) {
        if (NumberUtils.toInt(material, -1) != -1) {
            // 是数字ID
            int id = NumberUtils.toInt(material, -1);
            for (Material value : Material.values()) {
                if (value.getId() == id) {
                    return value;
                }
            }
            return Material.STONE;
        } else {
            // 尝试直接读取
            try {
                return Material.valueOf(material);
            } catch (Throwable e) {
                // 返回类似
                return Arrays.stream(Materials.values())
                        .filter(m -> Strings.similarDegree(m.name(), material) > TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))
                        .max(Comparator.comparingDouble(x -> Strings.similarDegree(x.name(), material)))
                        .orElse(Materials.STONE)
                        .parseMaterial();
            }
        }
    }

}
