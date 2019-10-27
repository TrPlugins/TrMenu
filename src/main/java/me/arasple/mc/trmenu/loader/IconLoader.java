package me.arasple.mc.trmenu.loader;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.trmenu.actions.ActionType;
import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.mats.Mat;
import me.arasple.mc.trmenu.menu.MenurSettings;
import me.arasple.mc.trmenu.utils.Maps;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.*;

/**
 * @author Arasple
 * @date 2019/10/4 20:58
 */
@SuppressWarnings("unchecked")
public class IconLoader {

    public static Icon loadIcon(Map<String, Object> map) {
        return loadIcon(map, null);
    }

    /**
     * 加载图标
     *
     * @param map         图标设置
     * @param defaultIcon 默认图标
     * @return 图标
     */
    public static Icon loadIcon(Map<String, Object> map, Icon defaultIcon) {
        List<Mat> materials = Lists.newArrayList();
        List<String> names;
        List<List<String>> lores;
        List<List<Integer>> slots;
        List<ItemFlag> flags = Lists.newArrayList();
        Map displayMap = Maps.sectionToMap(map.get("display"));
        Map actionsMap = Maps.containsSimilar(map, "actions") ? Maps.sectionToMap(map.get("actions")) : new HashMap<>();
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

        // Actions
        Object allActionObject = Maps.getSimilarOrDefault(actionsMap, "ALL", null);
        List<String> allAction = allActionObject instanceof List ? (List<String>) allActionObject : allActionObject != null ? Collections.singletonList(String.valueOf(allActionObject)) : null;
        if (allAction != null && !allAction.isEmpty()) {
            actions.put(null, ActionType.readAction(allAction));
        }

        // 载入动态材质
        if (mats == null && defaultIcon == null) {
            throw new NullPointerException("Materials can not be null");
        } else {
            if (mats instanceof List) {
                ((List) mats).forEach(m -> materials.add(new Mat(String.valueOf(m))));
            } else {
                materials.add(new Mat(String.valueOf(mats)));
            }
        }
        names = name instanceof List ? (List<String>) name : Collections.singletonList(String.valueOf(name));
        lores = readStringList(lore);
        slots = readIntegerList(slot);
        // flags
        if (flag != null) {
            if (flag instanceof List) {
                try {
                    ((List<Object>) flag).forEach(f -> flags.add(Items.asItemFlag(String.valueOf(f))));
                } catch (Throwable ignored) {
                }
                flags.removeIf(Objects::isNull);
            }
        }

        Item item = (displayMap == null && defaultIcon != null) ? defaultIcon.getItem() : new Item(names, materials, lores, slots, flags, shiny, amount);
        return new Icon(item, actions);
    }

    public static List<List<String>> readStringList(Object object) {
        List<List<String>> list = Lists.newArrayList();
        if (!(object instanceof List) || ((List) object).size() <= 0) {
            return list;
        } else if (((List) object).get(0) instanceof List) {
            ((List) object).forEach(x -> list.add((ArrayList) x));
        } else {
            list.add((List<String>) object);
        }
        return list;
    }

    public static List<List<Integer>> readIntegerList(Object object) {
        List<List<Integer>> list = Lists.newArrayList();
        if (!(object instanceof List) || ((List) object).size() <= 0) {
            return list;
        } else if (((List) object).get(0) instanceof List) {
            ((List) object).forEach(x -> list.add((ArrayList) x));
        } else {
            list.add((List<Integer>) object);
        }
        return list;
    }

}
