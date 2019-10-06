package me.arasple.mc.trmenu.mats;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.hook.HookHeadDatabase;
import me.arasple.mc.trmenu.utils.ItemMaterials;
import me.arasple.mc.trmenu.utils.Skulls;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/4 16:21
 */
public class Mat {

    private String mat;
    private MatType type;

    private Material material;
    private String head;
    private int model;
    private byte data;

    public Mat(String mat) {
        this.mat = mat.replace(' ', '_').toUpperCase();
        this.type = initType(this.mat);
    }

    /**
     * 为玩家创建该物品
     *
     * @param player 玩家
     * @return 物品
     */
    public ItemStack createItem(Player player) {
        ItemStack itemStack = material != null ? new ItemStack(material) : null;
        ItemMeta itemMeta = itemStack != null ? itemStack.getItemMeta() : null;

        switch (type) {
            case ORIGINAL:
                if (data != 0) {
                    itemStack.setDurability(data);
                }
                return itemStack;
            case MODEL_DATA:
                itemMeta.setCustomModelData(model);
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            case PLAYER_HEAD:
                return Skulls.getPlayerSkull(Vars.replace(player, head));
            case CUSTOM_HEAD:
                return Skulls.getCustomSkull(head);
            case HEAD_DATABASE:
                if (!HookHeadDatabase.isHoooked()) {
                    TrMenu.getTLogger().error("&c未安装 &6HeadDatabase&c, 你无法使用该材质: &6" + mat);
                    return null;
                }
                return HookHeadDatabase.getItem(head);
            default:
                return itemStack;
        }
    }

    /**
     * 判断材质类型并注入
     *
     * @param material text
     * @return 类型
     */
    public MatType initType(String material) {
        List<Variables.Variable> variable = new Variables(material).find().getVariableList().stream().filter(Variables.Variable::isVariable).collect(Collectors.toList());
        if (variable.size() >= 1) {
            String[] args = variable.get(0).getText().split(":");
            if (args.length >= 2) {
                MatType matType = MatType.matchByName(args[0]);
                if (matType == MatType.MODEL_DATA) {
                    this.material = ItemMaterials.readMaterial(args[1]);
                    this.model = NumberUtils.toInt(args[2], 0);
                } else {
                    this.head = args[1].toLowerCase();
                }
                return matType;
            }
        } else {
            String[] args = material.split(":");
            if (!Materials.isNewVersion()) {
                String[] mat = ItemMaterials.readNewMaterialForOld(args[0]);
                this.material = Material.valueOf(mat[0]);
                this.data = (byte) (args.length > 1 ? NumberUtils.toInt(args[1], 0) : NumberUtils.toInt(mat[1], 0));
            } else {
                if (Materials.matchMaterials(args[0]) != null) {
                    this.material = Materials.matchMaterials(args[0]).parseMaterial();
                } else {
                    this.material = ItemMaterials.readMaterial(args[0]);
                }
            }
            return MatType.ORIGINAL;
        }
        this.material = Material.STONE;
        return MatType.ORIGINAL;
    }

}
