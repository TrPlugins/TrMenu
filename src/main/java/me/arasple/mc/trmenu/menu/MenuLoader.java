package me.arasple.mc.trmenu.menu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfigWatcher;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.TrAction;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.loader.IconLoader;
import me.arasple.mc.trmenu.utils.Maps;
import me.arasple.mc.trmenu.utils.Notifys;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.*;

import static me.arasple.mc.trmenu.menu.MenurSettings.*;

/**
 * @author Arasple
 * @date 2019/12/8 9:58
 */
@SuppressWarnings("unchecked")
public class MenuLoader {

    private static File folder;

    public static void init() {
        folder = new File(TrMenu.getPlugin().getDataFolder(), "menus");
        if (!folder.exists()) {
            TrMenu.getPlugin().saveResource("menus/example.yml", true);
            TrMenu.getPlugin().saveResource("menus/buy-and-sell.yml", true);
        }
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
                        errors.addAll(MenuLoader.loadMenu(menuFile));
                    }
                }
            }
            errors.addAll(MenuLoader.loadMenu(folder));
            TrMenu.getMenus().removeIf(menu -> !menu.getLoadedFrom().exists());

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
            Menu menu = TrMenu.getMenus().stream().filter(m -> m.getLoadedFrom().getName().equals(file.getName())).findFirst().orElse(null);
            Map<String, Object> sets;
            Map<String, Object> options;
            try {
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);
                sets = config.getValues(false);
            } catch (Exception e) {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.YAML", name, e.getMessage(), Arrays.toString(e.getStackTrace())));
                return errors;
            }
            options = Maps.sectionToMap(MENU_OPTIONS.getFromMap(sets));

            // 读取配置文件中的值
            InventoryType inventoryType = Arrays.stream(InventoryType.values()).filter(t -> t.name().equalsIgnoreCase(MenurSettings.MENU_TYPE.getFromMap(sets))).findFirst().orElse(null);
            String title = MENU_TITLE.getFromMap(sets, "TrMenu");
            List<String> shape = fixShape(MENU_SHAPE.getFromMap(sets));
            int rows = shape != null ? shape.size() : 0;
            HashMap<Button, List<Integer>> buttons = new HashMap<>();
            List<String> openCommands = MENU_OPEN_COMAMNDS.getFromMap(sets);
            List<AbstractAction> openActions = TrAction.readActions(MENU_OPEN_ACTIONS.getFromMap(sets, new ArrayList()));
            List<AbstractAction> closeActions = TrAction.readActions(MENU_CLOSE_ACTIONS.getFromMap(sets, new ArrayList()));
            List<AbstractAction> openDenyActions = TrAction.readActions(MENU_OPEN_DENY_ACTIONS.getFromMap(sets, new ArrayList()));
            List<AbstractAction> closeDenyActions = TrAction.readActions(MENU_CLOSE_DENY_ACTIONS.getFromMap(sets, new ArrayList()));
            String openRequirement = MENU_OPEN_REQUIREMENT.getFromMap(sets);
            String closeRequirement = MENU_CLOSE_REQUIREMENT.getFromMap(sets);
            boolean lockPlayerInv = MENU_OPTIONS_LOCKHAND.getFromMap(options, true);
            boolean transferArgs = MENU_OPTIONS_ARGS.getFromMap(options, false);
            int forceTransferArgsAmount = MENU_OPTIONS_FORCEARGS.getFromMap(options, -1);
            List<String> bindItemLore = MENU_OPTIONS_BINDLORES.getFromMap(options);
            List<String> dependExpansions = MENU_OPTIONS_DEPEND_EXPANSIONS.getFromMap(options);
            // 检查一些常规错误
            if (shape == null || shape.isEmpty()) {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.NO-SHAPE", name, shape));
            }
            TrMenu.getMenus().forEach(m -> {
                if (m != menu) {
                    if (m.getOpenCommands() != null && openCommands != null && m.getOpenCommands().stream().anyMatch(openCommands::contains)) {
                        errors.add(TLocale.asString("MENU.LOADING-ERRORS.CONFLICT-OPEN-COMMANDS", name, m.getName()));
                    }
                    if (m.getBindItemLore() != null && bindItemLore != null && m.getBindItemLore().stream().anyMatch(bindItemLore::contains)) {
                        errors.add(TLocale.asString("MENU.LOADING-ERRORS.CONFLICT-OPEN-ITEMS", name, m.getName()));
                    }
                }
            });
            // 加载按钮
            if (Maps.containsSimilar(sets, MENU_BUTTONS.getName())) {
                ((Map<String, Object>) Maps.sectionToMap(MENU_BUTTONS.getFromMap(sets))).
                        forEach((key, b) -> {
                            try {
                                Map<String, Object> butMap = (Map<String, Object>) Maps.sectionToMap(b);
                                Icon defaultIcon = IconLoader.loadIcon(butMap);
                                HashMap<String, Icon> conditionalIcons = new HashMap<>();
                                int update = BUTTON_UPDATE_PERIOD.getFromMap(butMap, -1);
                                int refresh = BUTTON_REFRESH_CONDITIONS.getFromMap(butMap, -1);

                                if (BUTTON_ICONS.getFromMap(butMap) != null) {
                                    ((List<Object>) BUTTON_ICONS.getFromMap(butMap)).forEach(icon -> {
                                        Map iconMap = Maps.sectionToMap(icon);
                                        String condition = BUTTON_ICONS_CONDITION.getFromMap(iconMap);
                                        if (condition != null) {
                                            conditionalIcons.put(condition, IconLoader.loadIcon(iconMap, defaultIcon));
                                        } else {
                                            errors.add(TLocale.asString("MENU.LOADING-ERRORS.ICON-NO-CONDITION", name, key));
                                        }
                                    });
                                }
                                List<Integer> slots = locateButton(shape, inventoryType, key.charAt(0));
                                Button button = new Button(update, refresh, defaultIcon, conditionalIcons);
                                if (slots.size() > 0) {
                                    buttons.put(button, slots);
                                }
                            } catch (Throwable e) {
                                errors.add(TLocale.asString("MENU.LOADING-ERRORS.ICON-LOAD-FAILED", name, key, e.toString(), Arrays.toString(e.getStackTrace())));
                            }
                        });
            } else {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.NO-BUTTONS", name));
            }
            // 保存菜单
            if (errors.size() <= 0) {
                String mName = name.substring(0, name.length() - 4);
                Menu nMenu = new Menu(mName, title, inventoryType, shape.size(), buttons, openRequirement, openDenyActions, closeRequirement, closeDenyActions, openCommands, openActions, closeActions, lockPlayerInv, transferArgs, forceTransferArgsAmount, bindItemLore, dependExpansions);
                nMenu.setLoadedFrom(file);

                if (menu != null) {
                    List<Player> viewers = menu.getViewers();
                    menu.getViewers().forEach(p -> Bukkit.getScheduler().runTask(TrMenu.getPlugin(), () -> p.closeInventory()));
                    TrMenu.getMenus().remove(menu);
                    TrMenu.getMenus().add(nMenu);
                    viewers.forEach(player -> {
                        nMenu.open(player);
                    });
                } else {
                    TrMenu.getMenus().add(nMenu);
                    if (TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.ENABLE", true)) {
                        TConfigWatcher.getInst().addSimpleListener(file, () -> {
                            if (file.exists() && TrMenuAPI.getMenu(mName) != null) {
                                if (loadMenu(file).size() <= 0) {
                                    if (TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.NOTIFY", true)) {
                                        TLocale.sendToConsole("MENU.LOADED-AUTOLY", name);
                                    }
                                }
                            }
                        });
                    }
                }
            }

        }
        return errors;
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

    public static File getFolder() {
        return folder;
    }

}
