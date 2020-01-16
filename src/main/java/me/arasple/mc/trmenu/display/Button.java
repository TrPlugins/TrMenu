package me.arasple.mc.trmenu.display;

import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Button {

    private int update;
    private int refresh;
    private Icon defIcon;
    private List<Icon> icons;
    private HashMap<UUID, Integer> visibles;

    public Button(int update, int refresh, List<Icon> icons) {
        this.update = update;
        this.refresh = refresh;
        this.icons = icons;
        this.visibles = new HashMap<>();

        this.icons.sort(Comparator.comparingInt(Icon::getPriority));
        Collections.reverse(icons);

        this.defIcon = icons.stream().filter(i -> Strings.isEmpty(i.getRequirement())).findFirst().orElse(null);
        this.icons.remove(defIcon);
    }

    /**
     * 重新计算条件并刷新显示的图标
     *
     * @param player 玩家
     * @param event  容器事件
     */
    public void refreshConditionalIcon(Player player, InventoryClickEvent event) {
        if (icons.size() > 0) {
            for (int i = 0; i < icons.size(); i++) {
                Icon icon = icons.get(i);
                if ((boolean) JavaScript.run(player, icon.getRequirement(), event)) {
                    visibles.put(player.getUniqueId(), i);
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

    public List<Icon> getIcons() {
        return icons;
    }

    public Icon getDefIcon() {
        return defIcon;
    }

    public Icon getIcon(Player player) {
        return visibles.containsKey(player.getUniqueId()) ? icons.get(visibles.get(player.getUniqueId())) : getDefIcon();
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
                Objects.equals(visibles, button.visibles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(update, refresh, defIcon, icons, visibles);
    }

}
