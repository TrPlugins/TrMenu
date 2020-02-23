package me.arasple.mc.trmenu.nms;

import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.lite.SimpleVersionControl;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.menu.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

/**
 * @author Arasple
 * @date 2020/1/20 15:34
 */
public abstract class TrMenuNms {

    public static TrMenuNms instance;

    static {
        try {
            instance = (TrMenuNms) SimpleVersionControl.createNMS(
                    "me.arasple.mc.trmenu.nms.imp.TrMenuNms" + (Version.isAfter(Version.v1_14) ? "Modern" : "Old")
            ).translate(TrMenu.getPlugin()).newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static TrMenuNms getInst() {
        return instance;
    }

    public static void setTitle(Player player, Inventory inventory, String title) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
            getInst().setInventoryTitle(player, inventory, title);
        }
    }

    /**
     * 通过数据包更新一个容器的标题
     *
     * @param player    玩家
     * @param inventory 容器
     * @param title     标题
     */
    public abstract void setInventoryTitle(Player player, Inventory inventory, String title);

    /**
     * 发包关闭一个玩家的残留窗口
     *
     * @param player 玩家
     */
    public abstract void closeInventory(Player player);

    public Object setPacket(Class<?> nms, Object packet, Map<String, Object> sets) {
        sets.forEach((key, value) -> SimpleReflection.setFieldValue(nms, packet, key, value));
        return packet;
    }

}
