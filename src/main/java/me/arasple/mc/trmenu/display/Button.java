package me.arasple.mc.trmenu.display;

import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Button {

    private int update;
    private int refresh;
    private Icon defIcon;
    private HashMap<String, Icon> icons;
    private HashMap<UUID, String> visibles;

    public Button(int update, int refresh, Icon defIcon, HashMap<String, Icon> icons) {
        this.update = update;
        this.refresh = refresh;
        this.defIcon = defIcon;
        this.icons = icons;
    }

    /**
     * 重新计算条件并刷新显示的图标
     *
     * @param player 玩家
     * @param event  容器事件
     */
    public void refreshConditionalIcon(Player player, InventoryClickEvent event) {
        if (icons.values().size() > 0) {
            for (Map.Entry<String, Icon> iconEntry : icons.entrySet()) {
                if ((boolean) JavaScript.run(player, iconEntry.getKey(), event)) {
                    visibles.put(player.getUniqueId(), iconEntry.getKey());
                    return;
                }
            }
            visibles.remove(player.getUniqueId());
        }
    }

    /*
    GETTERS
     */

    public int getUpdate() {
        return update;
    }

    public int getRefresh() {
        return refresh;
    }

    public Icon getDefIcon() {
        return defIcon;
    }

    public HashMap<String, Icon> getIcons() {
        return icons;
    }

    public Icon getIcon(Player player) {
        return visibles.containsKey(player.getUniqueId()) ? icons.get(visibles.get(player.getUniqueId())) : getDefIcon();
    }

}
