package me.arasple.mc.trmenu.display.mats;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.hook.HookHeadDatabase;
import me.arasple.mc.trmenu.utils.Skulls;
import me.arasple.mc.trmenu.utils.Vars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/4 16:21
 */
public class Mat {

    private ItemStack staticItem;
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
    public ItemStack createItem(Player player) {
        if (staticItem == null) {
            ItemStack item = material != null ? new ItemStack(material) : null;
            ItemMeta meta = item != null ? item.getItemMeta() : null;
            // 原版材质
            if (type == MatType.ORIGINAL && data != 0) {
                assert item != null;
                item.setDurability(data);
            }
            // 1.14+ Model Data 材质
            else if (type == MatType.MODEL_DATA && model != 0) {
                assert meta != null;
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
                    TLocale.sendToConsole("ERROR.HDB", mat);
                    return null;
                }
                item = HookHeadDatabase.getItem(head);
            }
            staticItem = item;
        }
        return staticItem;
    }

    /**
     * 解析材质内容返回 MatType
     *
     * @param material 材质写法
     * @return MatType
     */
    private MatType initProperties(String material) {
        List<Variables.Variable> variable = new Variables(material).find().getVariableList().stream().filter(Variables.Variable::isVariable).collect(Collectors.toList());
        String[] args;
        if (variable.size() >= 1) {
            args = variable.get(0).getText().split(":");
            if (args.length >= 2) {
                MatType matType = MatType.matchByName(args[0]);
                if (matType == MatType.MODEL_DATA) {
                    this.material = Material.valueOf(readMaterial(new String[]{args[1], null})[0]);
                    this.model = NumberUtils.toInt(args[2], 0);
                } else {
                    this.head = args[1];
                }
                return matType;
            }
        } else {
            String[] read = readMaterial(material.split(":"));
            this.material = Material.valueOf(read[0]);
            if (!Materials.isNewVersion() && !Strings.isEmpty(read[1])) {
                this.data = (byte) NumberUtils.toInt(read[1], 0);
            }
            return MatType.ORIGINAL;
        }
        this.material = Material.STONE;
        return MatType.ORIGINAL;
    }

    private String[] readMaterial(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replace(' ', '_').toUpperCase();
        }
        if (args.length >= 1) {
            String id = args.length >= 2 && NumberUtils.isCreatable(args[1]) ? args[1] : null;
            if (NumberUtils.isCreatable(args[0])) {
                return new String[]{
                        Arrays.stream(Material.values()).filter(m -> m.getId() == NumberUtils.toInt(args[0], 1)).findFirst().orElse(Material.STONE).name(),
                        id
                };
            } else {
                try {
                    return new String[]{Material.valueOf(args[0]).name(), id};
                } catch (IllegalArgumentException e) {
                    Materials mats = Arrays.stream(Materials.values())
                            .filter(m -> Strings.similarDegree(m.name(), args[0]) > TrMenu.getSettings().getDouble("OPTIONS.MATERIAL-SIMILAR-DEGREE", 0.8))
                            .max(Comparator.comparingDouble(x -> Strings.similarDegree(x.name(), args[0])))
                            .orElse(Materials.STONE);
                    return new String[]{mats
                            .parseMaterial()
                            .name(),
                            id == null ? String.valueOf(mats.getData()) : id
                    };
                }
            }
        }

        return new String[]{Material.STONE.name(), null};
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
