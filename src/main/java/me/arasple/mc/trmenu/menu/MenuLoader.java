package me.arasple.mc.trmenu.menu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfigWatcher;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.TrAction;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.display.Mat;
import me.arasple.mc.trmenu.menu.objs.LoadedMenu;
import me.arasple.mc.trmenu.utils.Maps;
import me.arasple.mc.trmenu.utils.Notifys;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.util.*;

import static me.arasple.mc.trmenu.menu.MenuNodes.*;

/**
 * @author Arasple
 * @date 2019/12/8 9:58
 */
public class MenuLoader {

    private static File folder;

    public static void init() {
        folder = new File("plugins/TrMenu/menus");
        if (!folder.exists()) {
            TrMenu.getPlugin().saveResource("menus/example.yml", true);
            TrMenu.getPlugin().saveResource("menus/buy-and-sell.yml", true);
        }
    }

    public static LoadedMenu loadMenu(Map<String, Object> sets, String name) {
        return loadMenu(sets, name, null, false);
    }

    public static void loadMenus(CommandSender... receivers) {
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            long start = System.currentTimeMillis();
            int all = MenuLoader.getMenuFilesCount(folder);
            List<String> errors = new ArrayList<>();

            if (TrMenu.getSettings().isSet("MENU-FILES")) {
                for (String path : TrMenu.getSettings().getStringList("MENU-FILES")) {
                    File menuFile = new File(path);
                    if (menuFile.exists() && menuFile.getName().toLowerCase().endsWith(".yml")) {
                        errors.addAll(loadMenu(menuFile));
                    }
                }
            }
            TrMenu.getMenus().removeIf(menu -> menu.getLoadedFrom() == null);
            if (TrMenu.getSettings().isSet("MENUS")) {
                TrMenu.getSettings().getList("MENUS", new ArrayList<>()).forEach(s -> {
                    LinkedHashMap map = (LinkedHashMap) s;
                    map.forEach((name, section) -> errors.addAll(loadMenu((LinkedHashMap) section, String.valueOf(name), null, true).getErrors()));
                });
            }
            errors.addAll(MenuLoader.loadMenu(folder));
            TrMenu.getMenus().removeIf(menu -> menu.getLoadedFrom() != null && !menu.getLoadedFrom().exists());

            int loaded = TrMenuAPI.getMenus().size();
            if (loaded >= 0) {
                Notifys.notify(receivers, "MENU.LOADED-SUCCESS", loaded, System.currentTimeMillis() - start);
            }
            if (!errors.isEmpty()) {
                Notifys.notify(receivers, "MENU.LOADED-FAILURE", all - loaded);
                Notifys.sendMsg(receivers, "§8[§3Tr§bMenu§8] §6WARN §8| §6--------------------------------------------------");
                errors.forEach(error -> Notifys.sendMsg(receivers, "§8[§3Tr§bMenu§8] §bINFO §8| " + error));
                Notifys.sendMsg(receivers, "§8[§3Tr§bMenu§8] §6WARN §8| §6--------------------------------------------------");
            }
        });
    }

    private static List<String> loadMenu(File file) {
        String name = file.getName();
        List<String> errors = Lists.newArrayList();

        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                errors.addAll(Objects.requireNonNull(loadMenu(f)));
            }
        } else if (!name.toLowerCase().endsWith(".yml")) {
            return new ArrayList<>();
        } else {
            Menu menu = TrMenu.getMenus().stream().filter(m -> m.getLoadedFrom() != null && m.getLoadedFrom().getName().equals(file.getName())).findFirst().orElse(null);
            Map<String, Object> sets;
            try {
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);
                sets = config.getValues(false);
            } catch (Exception e) {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.YAML", name, e.getMessage(), Arrays.toString(e.getStackTrace())));
                return errors;
            }
            errors.addAll(loadMenu(sets, name, file, true).getErrors());
        }
        return errors;
    }


    public static LoadedMenu loadMenu(Map<String, Object> sets, String name, File file, boolean add) {
        LoadedMenu loadedMenu = new LoadedMenu();
        Menu menu = file == null || !file.exists() ? null : TrMenu.getMenus().stream().filter(m -> m.getLoadedFrom() != null && m.getLoadedFrom().getName().equals(file.getName())).findFirst().orElse(null);
        Map<String, Object> options = Maps.sectionToMap(MENU_OPTIONS.getFromMap(sets));
        InventoryType inventoryType = Arrays.stream(InventoryType.values()).filter(t -> t.name().equalsIgnoreCase(MenuNodes.MENU_TYPE.getFromMap(sets))).findFirst().orElse(null);
        String title = MENU_TITLE.getFromMap(sets, "TrMenu");
        List<String> shape = fixShape(MENU_SHAPE.getFromMap(sets));
        int rows = shape != null ? shape.size() : 0;
        HashMap<Button, List<Integer>> buttons = new HashMap<>();
        List<String> openCommands = MENU_OPEN_COMAMNDS.getFromMap(sets) instanceof List ? MENU_OPEN_COMAMNDS.getFromMap(sets) : Collections.singletonList(MENU_OPEN_COMAMNDS.getFromMap(sets));
        List<AbstractAction> openActions = TrAction.readActions(MENU_OPEN_ACTIONS.getFromMap(sets, new ArrayList<>()));
        List<AbstractAction> closeActions = TrAction.readActions(MENU_CLOSE_ACTIONS.getFromMap(sets, new ArrayList<>()));
        List<AbstractAction> openDenyActions = TrAction.readActions(MENU_OPEN_DENY_ACTIONS.getFromMap(sets, new ArrayList<>()));
        List<AbstractAction> closeDenyActions = TrAction.readActions(MENU_CLOSE_DENY_ACTIONS.getFromMap(sets, new ArrayList<>()));
        String openRequirement = MENU_OPEN_REQUIREMENT.getFromMap(sets);
        String closeRequirement = MENU_CLOSE_REQUIREMENT.getFromMap(sets);
        boolean lockPlayerInv = MENU_OPTIONS_LOCKHAND.getFromMap(options, true);
        boolean updateInventory = MENU_OPTIONS_UPDATEINV.getFromMap(options, false);
        boolean transferArgs = MENU_OPTIONS_ARGS.getFromMap(options, false);
        int forceTransferArgsAmount = MENU_OPTIONS_FORCEARGS.getFromMap(options, -1);
        List<String> bindItemLore = MENU_OPTIONS_BINDLORES.getFromMap(options);
        List<String> dependExpansions = MENU_OPTIONS_DEPEND_EXPANSIONS.getFromMap(options);

        if (shape == null || shape.isEmpty()) {
            loadedMenu.getErrors().add(TLocale.asString("MENU.LOADING-ERRORS.NO-SHAPE", name, shape));
        }
        if (menu != null) {
            TrMenu.getMenus().forEach(m -> {
                if (m != menu) {
                    if (m.getOpenCommands() != null && openCommands != null && m.getOpenCommands().stream().anyMatch(openCommands::contains)) {
                        loadedMenu.getErrors().add(TLocale.asString("MENU.LOADING-ERRORS.CONFLICT-OPEN-COMMANDS", name, m.getName()));
                    }
                    if (m.getBindItemLore() != null && bindItemLore != null && m.getBindItemLore().stream().anyMatch(bindItemLore::contains)) {
                        loadedMenu.getErrors().add(TLocale.asString("MENU.LOADING-ERRORS.CONFLICT-OPEN-ITEMS", name, m.getName()));
                    }
                }
            });
        }
        if (Maps.containsSimilar(sets, MENU_BUTTONS.getName())) {
            ((Map<String, Object>) Maps.sectionToMap(MENU_BUTTONS.getFromMap(sets))).
                    forEach((key, b) -> {
                        try {
                            Map<String, Object> butMap = (Map<String, Object>) Maps.sectionToMap(b);
                            int update = BUTTON_UPDATE_PERIOD.getFromMap(butMap, -1);
                            int refresh = BUTTON_REFRESH_CONDITIONS.getFromMap(butMap, -1);

                            List<Icon> icons = new ArrayList<>();
                            Icon defIcon = loadIcon(butMap);
                            defIcon.setPriority(Integer.MAX_VALUE);
                            icons.add(defIcon);

                            if (BUTTON_ICONS.getFromMap(butMap) != null) {
                                ((List<Object>) BUTTON_ICONS.getFromMap(butMap)).forEach(icon -> {
                                    Map iconMap = Maps.sectionToMap(icon);
                                    String condition = BUTTON_ICONS_CONDITION.getFromMap(iconMap);
                                    int priority = BUTTON_ICONS_PRIORITY.getFromMap(iconMap, 1);
                                    Icon pIcon = loadIcon(iconMap, defIcon);
                                    pIcon.setRequirement(condition);
                                    pIcon.setPriority(priority);
                                    if (condition != null) {
                                        icons.add(pIcon);
                                    } else {
                                        loadedMenu.getErrors().add(TLocale.asString("MENU.LOADING-ERRORS.ICON-NO-CONDITION", name, key));
                                    }
                                });
                            }
                            List<Integer> slots = locateButton(shape, inventoryType, key.charAt(0));
                            Button button = new Button(update, refresh, icons);
                            if (slots.size() > 0) {
                                buttons.put(button, slots);
                            }
                        } catch (Throwable e) {
                            StringBuilder stackTrace = new StringBuilder();
                            for (StackTraceElement s : e.getStackTrace()) {
                                stackTrace.append("\n").append(s.toString());
                            }
                            loadedMenu.getErrors().add(TLocale.asString("MENU.LOADING-ERRORS.ICON-LOAD-FAILED", name, key, e.toString(), stackTrace.toString()));
                        }
                    });
        } else {
            loadedMenu.getErrors().add(TLocale.asString("MENU.LOADING-ERRORS.NO-BUTTONS", name));
        }

        if (loadedMenu.getErrors().size() <= 0) {
            String mName = name.length() > 4 ? name.substring(0, name.length() - 4) : name;
            Menu nMenu = new Menu(mName, title, inventoryType, shape.size(), buttons, openRequirement, openDenyActions, closeRequirement, closeDenyActions, openCommands, openActions, closeActions, lockPlayerInv, updateInventory, transferArgs, forceTransferArgsAmount, bindItemLore, dependExpansions);
            nMenu.setLoadedFrom(file);
            if (nMenu != null && add) {
                if (menu != null) {
                    List<Player> viewers = menu.getViewers();
                    menu.getViewers().forEach(p -> Bukkit.getScheduler().runTask(TrMenu.getPlugin(), p::closeInventory));
                    TrMenu.getMenus().remove(menu);
                    viewers.forEach(player -> nMenu.open(player));
                }
                TrMenu.getMenus().add(nMenu);

                if (file != null && file.exists()) {
                    if (TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.ENABLE", true)) {
                        TConfigWatcher.getInst().addSimpleListener(file, () -> {
                            if (TrMenuAPI.getMenu(mName) != null) {
                                if (loadMenu(TrMenuAPI.getMenu(mName).getLoadedFrom()).size() <= 0) {
                                    if (TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.NOTIFY", true)) {
                                        TLocale.sendToConsole("MENU.LOADED-AUTOLY", name);
                                    }
                                }
                            }
                        });
                    }
                }
            }
            loadedMenu.setMenu(nMenu);
            loadedMenu.setState(LoadedMenu.LoadedState.SUCCESS);
            return loadedMenu;
        }
        loadedMenu.setState(LoadedMenu.LoadedState.FAILED);
        return loadedMenu;
    }

    public static Icon loadIcon(Map<String, Object> map) {
        return loadIcon(map, null);
    }

    /**
     * 加载图标
     *
     * @param map     图标设置
     * @param defIcon 默认图标
     * @return 图标
     */
    public static Icon loadIcon(Map<String, Object> map, Icon defIcon) {
        List<Mat> materials = Lists.newArrayList();
        List<String> names;
        List<List<String>> lores;
        List<List<Integer>> slots;
        List<ItemFlag> flags = Lists.newArrayList();
        Map displayMap = Maps.sectionToMap(map.get("display"));
        Map actionsMap = Maps.containsSimilar(map, "actions") ? Maps.sectionToMap(map.get("actions")) : new HashMap<>();
        HashMap<ClickType, List<AbstractAction>> actions = new HashMap<>();
        Object name = Maps.getSimilarOrDefault(displayMap, MenuNodes.ICON_DISPLAY_NAME.getName(), null);
        Object mats = Maps.getSimilarOrDefault(displayMap, MenuNodes.ICON_DISPLAY_MATERIALS.getName(), null);
        Object lore = Maps.getSimilarOrDefault(displayMap, MenuNodes.ICON_DISPLAY_LORES.getName(), null);
        Object slot = Maps.getSimilarOrDefault(displayMap, MenuNodes.ICON_DISPLAY_SLOTS.getName(), null);
        Object flag = Maps.getSimilarOrDefault(displayMap, MenuNodes.ICON_DISPLAY_FLAGS.getName(), null);
        String shiny = String.valueOf(Maps.getSimilar(displayMap, MenuNodes.ICON_DISPLAY_SHINY.getName()));
        String amount = String.valueOf(Maps.getSimilar(displayMap, MenuNodes.ICON_DISPLAY_AMOUNT.getName()));
        shiny = "null".equals(shiny) ? "false" : shiny;
        amount = "null".equals(amount) ? "1" : amount;

        // Actions
        for (ClickType value : ClickType.values()) {
            List<String> actStrs = Maps.getSimilarOrDefault(actionsMap, value.name(), null);
            if (actStrs != null && !actStrs.isEmpty()) {
                actions.put(value, TrAction.readActions(actStrs));
            }
        }
        actions.put(null, TrAction.readActions(castList(Maps.getSimilar(actionsMap, "ALL"), String.class)));

        // Materials
        if (mats == null && defIcon == null) {
            throw new NullPointerException("Materials can not be null");
        } else if (mats != null) {
            if (mats instanceof List) {
                List<String> list = castList(mats, String.class);
                list.forEach(m -> materials.add(new Mat(String.valueOf(m))));
            } else {
                materials.add(new Mat(String.valueOf(mats)));
            }
        }
        names = castList(name, String.class);
        lores = readStringList(lore);
        slots = readIntegerList(slot);

        if (defIcon != null) {
            if (names.isEmpty()) {
                names = defIcon.getItem().getNames();
            }
            if (lores.isEmpty()) {
                lores = defIcon.getItem().getLores();
            }
            if (materials.isEmpty()) {
                materials.addAll(defIcon.getItem().getMaterials());
            }
        }
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

        Item item = (displayMap == null && defIcon != null) ? defIcon.getItem() : new Item(names, materials, lores, slots, flags, shiny, amount);
        return new Icon(0, null, item, actions);
    }


    /**
     * 取得一个图标在形状中的位置
     *
     * @param type  容器类型
     * @param shape 布局
     * @param key   按钮字符
     * @return 槽位
     */
    private static List<Integer> locateButton(List<String> shape, InventoryType type, char key) {
        shape = fixShape(shape);
        List<Integer> slots = Lists.newArrayList();
        for (int line = 1; line <= shape.size(); line++) {
            String l = shape.get(line - 1);
            for (int index = 1; index <= l.toCharArray().length; index++) {
                if (key == l.charAt(index - 1)) {
                    int slot = 9 * (line - 1) + index - 1;
                    slots.add(slot);
                }
            }
        }
        return slots;
    }

    private static List<String> fixShape(List<String> shape) {
        if (shape == null) {
            return null;
        }
        while (shape.size() > 6) {
            shape.remove(shape.size() - 1);
        }
        for (int i = 0; i < shape.size(); i++) {
            if (shape.get(i).length() > 9) {
                shape.set(i, shape.get(i).substring(0, 9));
            }
        }
        return shape;
    }

    private static int getMenuFilesCount(File folder) {
        if (folder == null) {
            return 0;
        }
        int count = 0;
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                count += getMenuFilesCount(file);
            }
        } else {
            count += folder.getName().toLowerCase().endsWith(".yml") ? 1 : 0;
        }
        return count;
    }

    public static List<List<String>> readStringList(Object object) {
        List<List<String>> list = Lists.newArrayList();
        if (!(object instanceof List) || ((List) object).size() <= 0) {
            return list;
        } else if (((List) object).get(0) instanceof List) {
            ((List) object).forEach(x -> list.add((ArrayList<String>) x));
        } else {
            list.add(castList(object, String.class));
        }
        return list;
    }

    public static List<List<Integer>> readIntegerList(Object object) {
        List<List<Integer>> list = Lists.newArrayList();
        if (!(object instanceof List) || ((List) object).size() <= 0) {
            return list;
        } else if (((List) object).get(0) instanceof List) {
            ((List) object).forEach(x -> list.add((ArrayList<Integer>) x));
        } else {
            list.add(castList(object, Integer.class));
        }
        return list;
    }

    public static <T> List<T> castList(Object object, Class<T> classz) {
        List<T> result = new ArrayList<>();
        if (object instanceof List<?>) {
            for (Object o : (List<?>) object) {
                result.add(classz.cast(o));
            }
        } else if (object != null) {
            result.add(classz.cast(object));
        }
        return result;
    }

    public static File getFolder() {
        return folder;
    }


}
