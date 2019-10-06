package me.arasple.mc.trmenu.loader;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.trmenu.actions.ActionType;
import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.mat.Mat;
import me.arasple.mc.trmenu.settings.MenurSettings;
import me.arasple.mc.trmenu.utils.Maps;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Arasple
 * @date 2019/10/4 20:58
 */
@SuppressWarnings("unchecked")
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
        List<List<Integer>> slots = Lists.newArrayList();
        List<ItemFlag> flags = Lists.newArrayList();

        Map<String, Object> displayMap = Maps.sectionToMap(map.get("display"));
        Map<String, Object> actionsMap = Maps.containsSimilar(map, "actions") ? Maps.sectionToMap(map.get("actions")) : new HashMap<>();
        HashMap<ClickType, List<BaseAction>> actions = new HashMap<>();

        Object name = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_NAME.getName(), null);
        Object mats = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_MATERIALS.getName(), null);
        Object lore = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_LORES.getName(), null);
        Object slot = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_SLOTS.getName(), null);
        Object flag = Maps.getSimilarOrDefault(displayMap, MenurSettings.ICON_DISPLAY_FLAGS.getName(), null);
        String shiny = String.valueOf(Maps.getSimilar(displayMap, MenurSettings.ICON_DISPLAY_SHINY.getName()));
        String amount = String.valueOf(Maps.getSimilar(displayMap, MenurSettings.ICON_DISPLAY_AMOUNT.getName()));
        shiny = "null".equals(shiny) ? "false" : shiny;
        amount = "null".equals(amount) ? "1" : amount;

        // 载入点击动作
        for (ClickType value : ClickType.values()) {
            List<String> actionStrings = Maps.getSimilarOrDefault(actionsMap, value.name(), null);
            if (actionStrings != null && !actionStrings.isEmpty()) {
                actions.put(value, ActionType.readAction(actionStrings));
            }
        }
        Object allActionObject = Maps.getSimilarOrDefault(actionsMap, "ALL", null);
        if (allActionObject != null) {
            List<String> allAction = Lists.newArrayList();
            if (allActionObject instanceof List) {
                allAction = (List<String>) allActionObject;
            } else {
                allAction.add(String.valueOf(allActionObject));
            }
            if (allAction != null && !allAction.isEmpty()) {
                actions.put(null, ActionType.readAction(allAction));
            }
        }

        // 载入动态材质
        if (mats == null) {
            throw new NullPointerException("Materials can not be null");
        } else {
            if (mats instanceof List) {
                ((List) mats).forEach(m -> materials.add(new Mat(String.valueOf(m))));
            } else {
                materials.add(new Mat(String.valueOf(mats)));
            }
        }
        // 载入图标动态名称
        if (name != null) {
            if (name instanceof List) {
                names.addAll((List<String>) name);
            } else {
                names.add(String.valueOf(name));
            }
        }
        // 载入Lore组
        if (lore != null) {
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
        // 载入动态位置组
        if (slot != null) {
            if (slot instanceof List) {
                List<Object> s = (List<Object>) slot;
                if (s.size() > 0) {
                    if (s.get(0) instanceof List) {
                        slots.addAll((List<List<Integer>>) slot);
                    } else {
                        slots.add((List<Integer>) slot);
                    }
                }
            }
        }
        // 载入flags
        if (flag != null) {
            if (flag instanceof List) {
                try {
                    ((List<Object>) flag).forEach(f -> flags.add(Items.asItemFlag(String.valueOf(f))));
                } catch (Throwable ignored) {
                }
                flags.removeIf(Objects::isNull);
            }
        }
        return new Icon(new Item(names, materials, lores, slots, flags, shiny, amount), actions);
    }

}
