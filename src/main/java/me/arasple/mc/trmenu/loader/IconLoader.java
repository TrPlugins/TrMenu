package me.arasple.mc.trmenu.loader;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.actions.ActionType;
import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.mat.Mat;
import me.arasple.mc.trmenu.settings.MenurSettings;
import me.arasple.mc.trmenu.utils.Maps;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/10/4 20:58
 */
public class IconLoader {

    /**
     * 加载图标
     *
     * @param map 图标设置
     * @return 图标
     */
    public static Icon loadIcon(Map<String, Object> map) {
        List<String> names = Lists.newArrayList();
        List<Mat> materials = Lists.newArrayList();
        List<List<String>> lores = Lists.newArrayList();

        Map<String, Object> displayMap = Maps.sectionToMap(map.get("display"));
        Map<String, Object> actionsMap = Maps.containsSimilar(map, "actions") ? Maps.sectionToMap(map.get("actions")) : new HashMap<>();
        HashMap<ClickType, List<BaseAction>> actions = new HashMap<>();

        Object name = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_NAME.getName(), null);
        Object mats = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_MATERIALS.getName(), null);
        Object lore = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_LORES.getName(), null);

        for (ClickType value : ClickType.values()) {
            List<String> actionStrings = Maps.getSimilarOrDefault(actionsMap, value.name(), null);
            if (actionStrings != null && !actionStrings.isEmpty()) {
                actions.put(value, ActionType.readAction(actionStrings));
            }
        }

        if (mats == null) {
            throw new NullPointerException("Materials can not be null");
        } else {
            // 载入图标的动态材质
            if (mats instanceof List) {
                ((List) mats).forEach(m -> materials.add(new Mat(String.valueOf(m))));
            } else {
                materials.add(new Mat(String.valueOf(mats)));
            }
        }

        if (name != null) {
            // 载入图标动态名称
            if (name instanceof List) {
                names.addAll((List<String>) name);
            } else {
                names.add(String.valueOf(name));
            }
        }
        if (lore != null) {
            // 载入单个/多个Lore组
            if (lore instanceof List) {
                List<Object> l = (List<Object>) lore;
                if (l.size() > 0) {
                    if (l.get(0) instanceof List) {
                        lores.addAll((List<List<String>>) lore);
                    } else {
                        lores.add((List<String>) lore);
                    }
                }
            }
        }

        return new Icon(new Item(names, materials, lores), actions);
    }

}
