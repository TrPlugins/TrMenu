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

    private ItemStack staticTemp;
    private String mat;
    private MatType type;
    private Material material;
    private String head;
    private int model;
    private byte data;

    public Mat(String mat) {
        this.mat = mat;
        this.data = 0;
        this.model = 0;
        this.type = initProperties(this.mat);
    }

    /**
     * 转换 Mat 为一个显示的 ItemStack
     *
     * @param player 玩家
     * @return ItemStack
     */
    @SuppressWarnings("deprecation")
    public ItemStack createItem(Player player) {
        if (staticTemp != null) {
            return staticTemp;
        } else {
            ItemStack item = material != null ? new ItemStack(material) : null;
            ItemMeta meta = item != null ? item.getItemMeta() : null;
            // 原版材质
            if (type == MatType.ORIGINAL && data != 0) {
                item.setDurability(data);
            }
            // 1.14+ Model Data 材质
            else if (type == MatType.MODEL_DATA && model != 0) {
                meta.setCustomModelData(model);
                item.setItemMeta(meta);
            }
            // 自定义纹理头颅
            else if (type == MatType.CUSTOM_HEAD) {
                item = Skulls.getCustomSkull(head);
            }
            // 玩家动态头颅材质
            else if (type == MatType.PLAYER_HEAD) {
                return Skulls.getPlayerSkull(Vars.replace(player, head));
            }
            // HeadDatabase
            else if (type == MatType.HEAD_DATABASE) {
                if (!HookHeadDatabase.isHoooked()) {
                    TrMenu.getTLogger().error("&c未安装 &6HeadDatabase&c, 你无法使用该材质: &6" + mat);
                    return null;
                }
                item = HookHeadDatabase.getItem(head);
            }
            staticTemp = item;
            return item;
        }
    }

    /**
     * 解析材质内容返回 MatType
     *
     * @param material 材质写法
     * @return MatType
     */
    public MatType initProperties(String material) {
        List<Variables.Variable> variable = new Variables(material).find().getVariableList().stream().filter(Variables.Variable::isVariable).collect(Collectors.toList());
        String[] args;
        if (variable.size() >= 1) {
            args = variable.get(0).getText().split(":");
            if (args.length >= 2) {
                MatType matType = MatType.matchByName(args[0]);
                if (matType == MatType.MODEL_DATA) {
                    this.material = ItemMaterials.readMaterial(args[1]);
                    this.model = NumberUtils.toInt(args[2], 0);
                } else {
                    this.head = args[1];
                }
                return matType;
            }
        } else {
            args = material.split(":");
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

    /*
    GETTERS & SETTERS
     */

    public String getMat() {
        return mat;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }

    public MatType getType() {
        return type;
    }

    public void setType(MatType type) {
        this.type = type;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }

}
