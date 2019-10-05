package me.arasple.mc.trmenu.display;

import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.data.ArgsCache;
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

    private int updatePeriod;
    private int refreshConditions;
    private Icon defaultIcon;
    private HashMap<String, Icon> conditionalIcons;
    private Icon currentIcon;

    public Button(int updatePeriod, int refreshConditions, Icon defaultIcon, HashMap<String, Icon> conditionalIcons) {
        this.updatePeriod = updatePeriod;
        this.refreshConditions = refreshConditions;
        this.defaultIcon = defaultIcon;
        this.conditionalIcons = conditionalIcons;
    }

    /**
     * 重新计算条件并刷新显示的图标
     *
     * @param player 玩家
     * @param event  容器事件
     */
    public void refreshConditionalIcon(Player player, InventoryClickEvent event) {
        if (conditionalIcons.values().size() > 0) {
            for (Map.Entry<String, Icon> iconEntry : conditionalIcons.entrySet()) {
                if ((boolean) JavaScript.run(player, iconEntry.getKey(), event, ArgsCache.getPlayerArgs(player))) {
                    currentIcon = iconEntry.getValue();
                    return;
                }
            }
        }
        currentIcon = defaultIcon;
        Metrics.increase(2);
    }

    /*
    GETTERS & SETTERS
     */

    public int getUpdatePeriod() {
        return updatePeriod;
    }

    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public int getRefreshConditions() {
        return refreshConditions;
    }

    public void setRefreshConditions(int refreshConditions) {
        this.refreshConditions = refreshConditions;
    }

    public Icon getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(Icon defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public HashMap<String, Icon> getConditionalIcons() {
        return conditionalIcons;
    }

    public void setConditionalIcons(HashMap<String, Icon> conditionalIcons) {
        this.conditionalIcons = conditionalIcons;
    }

    public Icon getCurrentIcon() {
        return currentIcon;
    }

    public void setCurrentIcon(Icon currentIcon) {
        this.currentIcon = currentIcon;
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
        return updatePeriod == button.updatePeriod &&
                refreshConditions == button.refreshConditions &&
                Objects.equals(defaultIcon, button.defaultIcon) &&
                Objects.equals(conditionalIcons, button.conditionalIcons) &&
                Objects.equals(currentIcon, button.currentIcon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updatePeriod, refreshConditions, defaultIcon, conditionalIcons, currentIcon);
    }

}
