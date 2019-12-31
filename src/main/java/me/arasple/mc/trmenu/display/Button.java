package me.arasple.mc.trmenu.display;

import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Button {

    private int update;
    private int refresh;
    private Icon defIcon;
    private HashMap<String, Icon> icons;
    private Icon icon;

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
                    icon = iconEntry.getValue();
                    return;
                }
            }
        }
        icon = defIcon;
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

    public Icon getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Button button = (Button) o;
        return update == button.update &&
                refresh == button.refresh &&
                Objects.equals(defIcon, button.defIcon) &&
                Objects.equals(icons, button.icons) &&
                Objects.equals(icon, button.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(update, refresh, defIcon, icons, icon);
    }

}
