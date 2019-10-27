package me.arasple.mc.trmenu.loader;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.ActionType;
import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.menu.Menur;
import me.arasple.mc.trmenu.menu.MenurHolder;
import me.arasple.mc.trmenu.menu.MenurSettings;
import me.arasple.mc.trmenu.utils.Maps;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

/**
 * @author Arasple
 * @date 2019/10/4 14:33
 */
public class MenuLoader {

    /**
     * 加载插件默认目录下的菜单到一个集合中
     *
     * @param menus   集合
     * @param senders 接受反馈
     */
    public static void loadMenus(List<Menur> menus, CommandSender... senders) {
        // 清空
        menus.clear();
        // 关闭正在查看被查看的菜单
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenurHolder) {
                player.closeInventory();
                TLocale.sendTo(player, "MENU.FORCED-CLOSE");
            }
        });
        // 读取目录
        File folder = new File(TrMenu.getPlugin().getDataFolder(), "menus");
        if (!folder.exists()) {
            TrMenu.getPlugin().saveResource("menus/example.yml", true);
        }
        // 异步加载
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            long start = System.currentTimeMillis();
            int all = MenuLoader.countMenus(folder);
            List<String> errors = MenuLoader.loadMenu(folder);
            // 载入自定义路径的菜单
            if (TrMenu.getSettings().isSet("MENU-FILES")) {
                for (String path : TrMenu.getSettings().getStringList("MENU-FILES")) {
                    File menuFile = new File(path);
                    if (menuFile.exists() && menuFile.getName().toLowerCase().endsWith(".yml")) {
                        errors.addAll(MenuLoader.loadMenu(menuFile));
                    }
                }
            }
            // 报平安
            for (CommandSender sender : senders) {
                if (menus.size() > 0) {
                    TLocale.sendTo(sender, "MENU.LOADED-SUCCESS", menus.size(), System.currentTimeMillis() - start);
                }

                if (all - menus.size() > 0 && !errors.isEmpty() && errors.size() > 0) {
                    TLocale.sendTo(sender, "MENU.LOADED-FAILURE", all - menus.size());
                    sender.sendMessage("§8[§3Tr§bMenu§8]§8[§6WARN§8] §6--------------------------------------------------");
                    sender.sendMessage("");
                    errors.forEach(error -> sender.sendMessage("§8[§3Tr§bMenu§8]§8[§7INFO§8] §6" + error));
                    sender.sendMessage("");
                    sender.sendMessage("§8[§3Tr§bMenu§8]§8[§6WARN§8] §6--------------------------------------------------");
                }
            }
        });
    }

    /**
     * 读取文件到菜单
     *
     * @param file 文件/目录
     * @return 加载错误
     */
    @SuppressWarnings("unchecked")
    public static List<String> loadMenu(File file) {
        List<String> errors = Lists.newArrayList();
        // 判断是否为目录
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                errors.addAll(loadMenu(f));
            }
        } else if (!file.getName().toLowerCase().endsWith(".yml")) {
            return new ArrayList<>();
        } else {
            // 如果是文件, 则读取信息
            String fileName = file.getName();
            Map<String, Object> sets = YamlConfiguration.loadConfiguration(file).getValues(false);
            // 菜单的属性、内容值
            String title = Maps.getSimilarOrDefault(sets, MenurSettings.MENU_TITLE.getName(), null);
            List<String> shape = fixShape(Maps.getSimilarOrDefault(sets, MenurSettings.MENU_SHAPE.getName(), null));
            List<String> openCmds = Maps.getSimilarOrDefault(sets, MenurSettings.MENU_OPEN_COMAMNDS.getName(), null);
            HashMap<Button, List<Integer>> buttons = new HashMap<>();
            List<BaseAction> openActions = ActionType.readAction(Maps.getSimilarOrDefault(sets, MenurSettings.MENU_OPEN_ACTIONS.getName(), new ArrayList<>()));
            List<BaseAction> closeActions = ActionType.readAction(Maps.getSimilarOrDefault(sets, MenurSettings.MENU_CLOSE_ACTIONS.getName(), new ArrayList<>()));
            String openRequirement = Maps.getSimilarOrDefault(sets, MenurSettings.MENU_OPEN_REQUIREMENT.getName(), null);
            List<BaseAction> openDenyActions = ActionType.readAction(Maps.getSimilarOrDefault(sets, MenurSettings.MENU_OPEN_DENY_ACTIONS.getName(), new ArrayList<>()));
            String closeRequirement = Maps.getSimilarOrDefault(sets, MenurSettings.MENU_CLOSE_REQUIREMENT.getName(), null);
            List<BaseAction> closeDenyActions = ActionType.readAction(Maps.getSimilarOrDefault(sets, MenurSettings.MENU_CLOSE_DENY_ACTIONS.getName(), new ArrayList<>()));
            Map<String, Object> options = Maps.getSimilarOrDefault(sets, MenurSettings.MENU_OPTIONS.getName(), new HashMap<>());
            List<String> depends = Maps.getSimilarOrDefault(options, MenurSettings.MENU_OPTIONS_DEPEND_EXPANSIONS.getName(), null);
            boolean lockPlayerInv = Maps.getSimilarOrDefault(options, MenurSettings.MENU_OPTIONS_LOCKHAND.getName(), true);
            boolean transferArgs = Maps.getSimilarOrDefault(options, MenurSettings.MENU_OPTIONS_ARGS.getName(), false);
            int forceTransferArgsAmount = Maps.getSimilarOrDefault(options, MenurSettings.MENU_OPTIONS_FORCEARGS.getName(), -1);
            List<String> bindItemLore = Maps.getSimilarOrDefault(options, MenurSettings.MENU_OPTIONS_BINDLORES.getName(), null);

            // 检测一些错误、冲突
            if (title == null) {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.NO-TITLE", fileName));
            }
            if (shape == null || shape.size() < 1) {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.NO-SHAPE", fileName, shape));
            }
            TrMenu.getMenus().forEach(menu -> {
                if (menu.getOpenCommands() != null && openCmds != null && menu.getOpenCommands().stream().anyMatch(openCmds::contains)) {
                    errors.add(TLocale.asString("MENU.LOADING-ERRORS.CONFLICT-OPEN-COMMANDS", fileName, menu.getName()));
                }
                if (menu.getBindItemLore() != null && bindItemLore != null && menu.getBindItemLore().stream().anyMatch(bindItemLore::contains)) {
                    errors.add(TLocale.asString("MENU.LOADING-ERRORS.CONFLICT-OPEN-ITEMS", fileName, menu.getName()));
                }
            });

            // 加载图标
            if (Maps.containsSimilar(sets, MenurSettings.MENU_BUTTONS.getName())) {
                ((Map<String, Object>) Maps.sectionToMap(Maps.getSimilarOrDefault(sets, MenurSettings.MENU_BUTTONS.getName(), null))).
                        forEach((key, b) -> {
                            try {
                                Map<String, Object> buttonSection = (Map<String, Object>) Maps.sectionToMap(b);
                                Icon defaultIcon = IconLoader.loadIcon(buttonSection);
                                HashMap<String, Icon> conditionalIcons = new HashMap<>();
                                int update = Maps.getSimilarOrDefault(buttonSection, MenurSettings.BUTTON_UPDATE_PERIOD.getName(), -1);
                                int refreshConditions = Maps.getSimilarOrDefault(buttonSection, MenurSettings.BUTTON_REFRESH_CONDITIONS.getName(), -1);

                                // 优先级图标
                                if (Maps.getSimilarOrDefault(buttonSection, MenurSettings.BUTTON_ICONS.getName(), null) != null) {
                                    ((List<Object>) Maps.getSimilar(buttonSection, MenurSettings.BUTTON_ICONS.getName())).forEach(icon -> {
                                        Map iconMap = Maps.sectionToMap(icon);
                                        String condition = Maps.getSimilarOrDefault(iconMap, MenurSettings.BUTTON_ICONS_CONDITION.getName(), null);
                                        if (condition != null) {
                                            conditionalIcons.put(condition, IconLoader.loadIcon(iconMap, defaultIcon));
                                        } else {
                                            errors.add(TLocale.asString("MENU.LOADING-ERRORS.ICON-NO-CONDITION", fileName, key));
                                        }
                                    });
                                }
                                // 定位图标的位置, 创建按钮
                                List<Integer> slots = locateButton(shape, key.charAt(0));
                                Button button = new Button(update, refreshConditions, defaultIcon, conditionalIcons);
                                if (slots.size() > 0) {
                                    buttons.put(button, slots);
                                }
                            } catch (Throwable e) {
                                errors.add(TLocale.asString("MENU.LOADING-ERRORS.ICON-LOAD-FAILED", fileName, key, e.toString(), Arrays.toString(e.getStackTrace())));
                            }
                        });
            } else {
                errors.add(TLocale.asString("MENU.LOADING-ERRORS.NO-BUTTONS", fileName));
            }
            // 保存菜单到内存
            if (errors.size() <= 0) {
                TrMenu.getMenus().add(new Menur(fileName.substring(0, fileName.length() - 4), title, shape.size(), buttons, openRequirement, openDenyActions, closeRequirement, closeDenyActions, openCmds, openActions, closeActions, lockPlayerInv, transferArgs, forceTransferArgsAmount, bindItemLore, depends));
            }
        }
        return errors;
    }

    /**
     * 修复一个布局形状
     *
     * @param shape 布局
     * @return 修复后的
     */
    private static List<String> fixShape(List<String> shape) {
        if (shape == null) {
            return shape;
        }
        // 裁剪多余行数
        while (shape.size() > 6) {
            shape.remove(shape.size() - 1);
        }
        // 裁剪每行多余的物品
        for (int i = 0; i < shape.size(); i++) {
            if (shape.get(i).length() > 9) {
                shape.set(i, shape.get(i).substring(0, 9));
            }
        }
        return shape;
    }

    /**
     * 取得一个图标在形状中的位置
     *
     * @param shape 布局
     * @param key   按钮字符
     * @return 槽位
     */
    private static List<Integer> locateButton(List<String> shape, char key) {
        List<Integer> slots = Lists.newArrayList();
        // 第 N 行 第 M 个 物品的 SLOT = (N-1)*9+M-1
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

    /**
     * 计算目录中的.YML文件数量
     *
     * @param folder 目录
     * @return 总数量(包括子目录)
     */
    public static int countMenus(File folder) {
        int count = 0;
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                count += countMenus(file);
            }
        } else {
            count += folder.getName().toLowerCase().endsWith(".yml") ? 1 : 0;
        }
        return count;
    }

}
