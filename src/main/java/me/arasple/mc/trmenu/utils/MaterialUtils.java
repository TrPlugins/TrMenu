package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Material;

import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/10/4 16:36
 */
public class MaterialUtils {

    public static Material matchMaterial(String material) {
        if (NumberUtils.toInt(material, -1) != -1) {
            return Arrays.stream(Materials.values())
                    .filter(m -> m.getId() == NumberUtils.toInt(material, -1))
                    .findFirst()
                    .orElse(Materials.BEDROCK)
                    .parseMaterial();
        }
        return Arrays.stream(Materials.values())
                .filter(m -> Strings.similarDegree(m.name(), material) > TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))
                .findFirst()
                .orElse(Materials.BEDROCK)
                .parseMaterial();
    }

    public static boolean existMaterial(String material) {
        String[] args = material.replace(' ', '_').toUpperCase().split(":");
        if (NumberUtils.toInt(args[0], -1) != -1) {
            if (args.length >= 2 && NumberUtils.toInt(args[1], -1) == -1) {
                return false;
            }
            return Materials.matchMaterials(NumberUtils.toInt(args[0], -1), args.length >= 2 ? NumberUtils.toByte(args[1], (byte) -1) : 0) != null;
        } else {
            return Arrays.stream(Materials.values()).anyMatch(m -> m.name().equalsIgnoreCase(args[0]));
        }
    }

}
