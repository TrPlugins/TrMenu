package me.arasple.mc.trmenu.menu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.lite.Catchers;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.TrAction;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.api.TrMenuAPI;
import me.arasple.mc.trmenu.api.events.MenuOpenEvent;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.display.Loc;
import me.arasple.mc.trmenu.nms.TrMenuNms;
import me.arasple.mc.trmenu.utils.JavaScript;
import me.arasple.mc.trmenu.utils.TrUtils;
import me.arasple.mc.trmenu.utils.Vars;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Menu {

    private String name;
    private InventoryType type;
    private HashMap<Integer, Integer> rows;
    private HashMap<Button, Loc> buttons;
    private List<String> titles;
    private int titleUpdate;
    private HashMap<UUID, Integer> titleIndex;
    private HashMap<UUID, Integer> shapeIndex;
    private List<String> openCommands;
    private List<AbstractAction> openActions;
    private List<AbstractAction> closeActions;
    private List<AbstractAction> openDenyActions;
    private List<AbstractAction> closeDenyActions;
    private String openRequirement;
    private String closeRequirement;
    private String keepOpenRequirement;
    private boolean lockPlayerInv;
    private boolean updateInventory;
    private boolean transferArgs;
    private int forceTransferArgsAmount;
    private List<String> bindItemLore;
    private List<String> dependExpansions;
    private String loadedPath;

    public Menu(String name, List<String> titles, int titleUpdate, InventoryType type, HashMap<Integer, Integer> rows, HashMap<Button, Loc> buttons, String openRequirement, List<AbstractAction> openDenyActions, String closeRequirement, List<AbstractAction> closeDenyActions, String keepOpenRequirement, List<String> openCommands, List<AbstractAction> openActions, List<AbstractAction> closeActions, boolean lockPlayerInv, boolean updateInventory, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore, List<String> dependExpansions) {
        setValues(name, titles, titleUpdate, type, rows, buttons, openRequirement, openDenyActions, closeRequirement, closeDenyActions, keepOpenRequirement, openCommands, openActions, closeActions, lockPlayerInv, updateInventory, transferArgs, forceTransferArgsAmount, bindItemLore, dependExpansions);
    }

    public void setValues(String name, List<String> title, int titleUpdate, InventoryType inventoryType, HashMap<Integer, Integer> rows, HashMap<Button, Loc> buttons, String openRequirement, List<AbstractAction> openDenyActions, String closeRequirement, List<AbstractAction> closeDenyActions, String keepOpenRequirement, List<String> openCommands, List<AbstractAction> openActions, List<AbstractAction> closeActions, boolean lockPlayerInv, boolean updateInventory, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore, List<String> dependExpansions) {
        this.name = name;
        this.titles = title;
        this.titleUpdate = titleUpdate;
        this.titleIndex = new HashMap<>();
        this.shapeIndex = new HashMap<>();
        this.type = inventoryType;
        this.rows = rows;
        this.buttons = buttons;
        this.openRequirement = openRequirement;
        this.openDenyActions = openDenyActions;
        this.closeRequirement = closeRequirement;
        this.closeDenyActions = closeDenyActions;
        this.keepOpenRequirement = keepOpenRequirement;
        this.openCommands = openCommands;
        this.openActions = openActions;
        this.closeActions = closeActions;
        this.lockPlayerInv = lockPlayerInv;
        this.updateInventory = updateInventory;
        this.transferArgs = transferArgs;
        this.forceTransferArgsAmount = forceTransferArgsAmount;
        this.bindItemLore = bindItemLore != null && !bindItemLore.isEmpty() ? TLocale.Translate.setColored(bindItemLore) : bindItemLore;
        this.dependExpansions = dependExpansions;
        checkDepends();
    }

    /**
     * 为一名玩家打开这个菜单
     *
     * @param player 玩家
     * @param args   传递参数
     */
    public void open(Player player, String... args) {
        open(player, 0, false, args);
    }

    public void open(Player player, boolean byConsole, String... args) {
        open(player, 0, byConsole, args);
    }

    /**
     * 为一名玩家打开这个菜单
     *
     * @param player    玩家
     * @param byConsole 是否由控制台命令操作
     * @param args      传递参数
     */
    public void open(Player player, int shape, boolean byConsole, String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = Vars.replace(player, args[i]);
        }
        if (!initEvent(player, shape, byConsole, args)) {
            ArgsCache.getArgs().remove(player.getUniqueId());
            return;
        }
        // 创建容器
        Inventory menu = type == null ? Bukkit.createInventory(new MenuHolder(this), 9 * getRows(shape), Vars.replace(player, titles.get(0))) : Bukkit.createInventory(new MenuHolder(this), type, Vars.replace(player, titles.get(0)));
        player.openInventory(menu);
        // 初始化容器
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            // 布置按钮
            buttons.forEach((button, loc) -> {
                        button.refreshConditionalIcon(player, null);
                        if (loc != null && !loc.getSlots(shape).isEmpty()) {
                            newUpdateTask(player, button, menu, loc.getSlots(shape), args);
                            newRefreshTask(player, button, menu);
                        }
                    }
            );
            // 设置标题
            newTitleUpdateTask(player, menu);
            // 保持打开条件
            newKeepOpenTask(player);
            // 如果设置刷新容器或启用动态标题, 将自动调整玩家手持槽位到一个空位 (如果有)
            // 关闭容器后会自动复原, 防止物品频闪影响体验
            if (isUpdateInventory() || (getTitles().size() > 1 && getTitleUpdate() > 0)) {
                if (!Items.isNull(player.getInventory().getItem(player.getInventory().getHeldItemSlot()))) {
                    for (byte i = 0; i < 9; i++) {
                        if (Items.isNull(player.getInventory().getItem(i))) {
                            ArgsCache.getHeldSlot().put(player.getUniqueId(), player.getInventory().getHeldItemSlot());
                            player.getInventory().setHeldItemSlot(i);
                            break;
                        }
                    }
                }
                player.updateInventory();
            }
            // 打開動作
            Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> {
                if (shape == 0 && openActions != null) {
                    openActions.forEach(action -> action.run(player));
                }
            }, 2);
        });
    }

    /**
     * 周期性检测条件是否满足, 否则关闭菜单
     *
     * @param player 玩家
     */
    private void newKeepOpenTask(Player player) {
        if (Strings.nonEmpty(getOpenRequirement())) {
            String[] require = getOpenRequirement().split(";", 2);
            long period = require.length >= 2 && NumberUtils.isParsable(require[1]) ? NumberUtils.toLong(require[1]) : 15;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!((boolean) JavaScript.run(player, require[0]))) {
                        player.closeInventory();
                        cancel();
                    }
                }
            }.runTaskTimer(TrMenu.getPlugin(), period, period);
        }
    }

    /**
     * 创建标题动态更新任务
     *
     * @param player 玩家
     * @param menu   容器
     */
    private void newTitleUpdateTask(Player player, Inventory menu) {
        if (getTitleUpdate() > 0 && getTitles().size() > 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isViewing(player, menu)) {
                        cancel();
                        return;
                    }
                    TrMenuNms.setTitle(player, menu, Vars.replace(player, getTitle(player)));
                }
            }.runTaskTimerAsynchronously(TrMenu.getPlugin(), getTitleUpdate(), getTitleUpdate());
        }
    }

    /**
     * 创建按钮优先级图标重新计算任务
     *
     * @param player 玩家
     * @param button 按钮
     * @param menu   容器
     */
    private void newRefreshTask(Player player, Button button, Inventory menu) {
        if (button.getRefresh() > 0 && button.getIcons().size() > 0) {
            int update = Math.max(button.getUpdate(), 3);
            int refresh = Math.max(button.getRefresh(), 5);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!isViewing(player, menu)) {
                        cancel();
                        return;
                    }
                    button.refreshConditionalIcon(player, null);
                }
            }.runTaskTimerAsynchronously(TrMenu.getPlugin(), refresh + update, refresh);
        }
    }

    /**
     * 创建图标刷新周期任务
     *
     * @param player 玩家
     * @param button 按钮
     * @param menu   容器
     * @param slots  槽位
     * @param args   参数
     */
    private void newUpdateTask(Player player, Button button, Inventory menu, List<Integer> slots, String... args) {
        if (button.getUpdate() <= 0) {
            return;
        }
        int update = Math.max(button.getUpdate(), 3);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isViewing(player, menu)) {
                    cancel();
                    return;
                }
                Item item = button.getIcon(player).getItem();
                ItemStack itemStack = item.createItemStack(player, args);
                List<ItemStack> emptySlots = Lists.newArrayList();
                (item.getSlots(player).size() > 0 ? item.getNextSlots(player, menu) : slots).forEach(i -> {
                    if (i < 0) {
                        emptySlots.add(itemStack);
                    } else {
                        if (menu.getSize() > i) {
                            menu.setItem(i, itemStack);
                        }
                    }
                });
                Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> emptySlots.forEach(i -> {
                            int s = TrUtils.getInst().getEmptySlot(menu);
                            item.setCurSlots(player, Collections.singletonList(s));
                            menu.setItem(s, itemStack);
                        }
                ), 4);
                clearEmptySlots(player, menu);
            }
        }.runTaskTimerAsynchronously(TrMenu.getPlugin(), 0, update);
    }

    /**
     * 初始化打开菜单的事件
     *
     * @param player    玩家
     * @param byConsole 是否由控制台操作
     * @param args      参数
     * @return
     */
    private boolean initEvent(Player player, int shape, boolean byConsole, String... args) {
        ArgsCache.getArgs().put(player.getUniqueId(), args);
        MenuOpenEvent event = new MenuOpenEvent(player, byConsole, this);
        if (event.isCancelled()) {
            return false;
        }
        if (event.getMenu() != this) {
            event.getMenu().open(player, shape, event.isByConsole(), args);
            return false;
        }
        if (!Strings.isBlank(openRequirement) && !(boolean) JavaScript.run(player, openRequirement.replace("$openBy", event.isByConsole() ? "CONSOLE" : "COMMAND"))) {
            event.setCancelled(true);
            TrAction.runActions(openDenyActions, player);
            return false;
        }
        List<String> unInstalledDepends = checkDepends();
        if (unInstalledDepends.size() > 0) {
            TLocale.sendTo(player, "MENU.DEPEND-EXPANSIONS-REQUIRED", Arrays.toString(unInstalledDepends.toArray()));
            event.setCancelled(true);
            return false;
        }
        Catchers.getPlayerdata().remove(player.getName());
        if (shape >= 0) {
            getShapeIndex().put(player.getUniqueId(), shape);
        }
        return true;
    }

    /**
     * 取得玩家对应的动态标题
     *
     * @param player 玩家
     * @return 标题
     */
    private String getTitle(Player player) {
        if (!titleIndex.containsKey(player.getUniqueId())) {
            titleIndex.put(player.getUniqueId(), 0);
        } else {
            titleIndex.put(player.getUniqueId(), (titleIndex.get(player.getUniqueId()) + 1 >= titles.size()) ? 0 : titleIndex.get(player.getUniqueId()) + 1);
        }
        return titles.get(titleIndex.get(player.getUniqueId()));
    }

    /**
     * 取得玩家对应槽位的实际按钮
     *
     * @param player 玩家
     * @param slot   槽位
     * @return 按钮
     */
    public Button getButton(Player player, int slot) {
        if (slot < 0) {
            return null;
        }
        for (Map.Entry<Button, Loc> entry : buttons.entrySet()) {
            Icon icon = entry.getKey().getIcon(player);
            if (icon.getItem().getCurSlots(player) != null && icon.getItem().getCurSlots(player).contains(slot)) {
                return entry.getKey();
            } else if (entry.getValue().getSlots(getShape(player)).contains(slot) && icon.getItem().getSlots(player).isEmpty()) {
                return entry.getKey();
            }
        }
        return null;
    }


    /**
     * 清理槽位变动导致的物品残留
     *
     * @param player 玩家
     * @param menu   容器
     */
    public void clearEmptySlots(Player player, Inventory menu) {
        for (int i = 0; i < menu.getSize(); i++) {
            if (menu.getItem(i) != null && getButton(player, i) == null) {
                menu.setItem(i, null);
            }
        }
    }

    /**
     * 判断玩家是否正打开某容器
     *
     * @param player 玩家
     * @param menu   菜单
     * @return 是否打开该容器
     */
    private boolean isViewing(Player player, Inventory menu) {
        return player.getOpenInventory().getTopInventory().equals(menu);
    }

    /**
     * 判断玩家当前是否正在查看该菜单
     *
     * @param player 玩家
     * @return 是否正在查看该菜单
     */
    public boolean isViewing(Player player) {
        return TrMenuAPI.getMenu(player) == this;
    }


    /**
     * 取得在线玩家中所有正在查看该菜单的玩家集合
     *
     * @return 玩家
     */
    public List<Player> getViewers() {
        return Bukkit.getOnlinePlayers().stream().filter(this::isViewing).collect(Collectors.toList());
    }

    /**
     * 检测菜单需要的 PlaceholderAPI 依赖并自动下载
     *
     * @return 未安装的
     */
    @Deprecated
    private List<String> checkDepends() {
        List<String> unInstalled = Lists.newArrayList();

        if (PlaceholderAPIPlugin.getInstance().getExpansionCloud() == null) {
            return unInstalled;
        }
        try {
            if (dependExpansions != null && dependExpansions.size() > 0) {
                if (PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansions().isEmpty()) {
                    PlaceholderAPIPlugin.getInstance().getExpansionCloud().fetch(false);
                    return dependExpansions.stream().filter(d -> PlaceholderAPI.getExpansions().stream().noneMatch(e -> e.getName().equalsIgnoreCase(d))).collect(Collectors.toList());
                }
                unInstalled = dependExpansions.stream().filter(d -> PlaceholderAPI.getExpansions().stream().noneMatch(e -> e.getName().equalsIgnoreCase(d)) && PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(d) != null && !PlaceholderAPIPlugin.getInstance().getExpansionCloud().isDownloading(d)).collect(Collectors.toList());
                if (unInstalled.size() > 0) {
                    unInstalled.forEach(ex -> {
                        CloudExpansion cloudExpansion = PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(ex);
                        PlaceholderAPIPlugin.getInstance().getExpansionCloud().downloadExpansion(null, cloudExpansion);
                    });
                    Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> PlaceholderAPIPlugin.getInstance().reloadConf(Bukkit.getConsoleSender()), 20 * 5);
                }
            }
        } catch (Throwable ignored) {
        }
        return unInstalled;
    }

    public int getShape(Player player) {
        return getShapeIndex().getOrDefault(player.getUniqueId(), 0);
    }

    /*
    GETTERS & SETTERS
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InventoryType getType() {
        return type;
    }

    public void setType(InventoryType type) {
        this.type = type;
    }

    public HashMap<Integer, Integer> getRows() {
        return rows;
    }

    public void setRows(HashMap<Integer, Integer> rows) {
        this.rows = rows;
    }

    public Integer getRows(int shape) {
        return rows.get(shape);
    }

    public HashMap<Button, Loc> getButtons() {
        return buttons;
    }

    public void setButtons(HashMap<Button, Loc> buttons) {
        this.buttons = buttons;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public int getTitleUpdate() {
        return titleUpdate;
    }

    public void setTitleUpdate(int titleUpdate) {
        this.titleUpdate = titleUpdate;
    }

    public HashMap<UUID, Integer> getTitleIndex() {
        return titleIndex;
    }

    public void setTitleIndex(HashMap<UUID, Integer> titleIndex) {
        this.titleIndex = titleIndex;
    }

    public HashMap<UUID, Integer> getShapeIndex() {
        return shapeIndex;
    }

    public void setShapeIndex(HashMap<UUID, Integer> shapeIndex) {
        this.shapeIndex = shapeIndex;
    }

    public List<String> getOpenCommands() {
        return openCommands;
    }

    public void setOpenCommands(List<String> openCommands) {
        this.openCommands = openCommands;
    }

    public List<AbstractAction> getOpenActions() {
        return openActions;
    }

    public void setOpenActions(List<AbstractAction> openActions) {
        this.openActions = openActions;
    }

    public List<AbstractAction> getCloseActions() {
        return closeActions;
    }

    public void setCloseActions(List<AbstractAction> closeActions) {
        this.closeActions = closeActions;
    }

    public List<AbstractAction> getOpenDenyActions() {
        return openDenyActions;
    }

    public void setOpenDenyActions(List<AbstractAction> openDenyActions) {
        this.openDenyActions = openDenyActions;
    }

    public List<AbstractAction> getCloseDenyActions() {
        return closeDenyActions;
    }

    public void setCloseDenyActions(List<AbstractAction> closeDenyActions) {
        this.closeDenyActions = closeDenyActions;
    }

    public String getOpenRequirement() {
        return openRequirement;
    }

    public void setOpenRequirement(String openRequirement) {
        this.openRequirement = openRequirement;
    }

    public String getCloseRequirement() {
        return closeRequirement;
    }

    public void setCloseRequirement(String closeRequirement) {
        this.closeRequirement = closeRequirement;
    }

    public String getKeepOpenRequirement() {
        return keepOpenRequirement;
    }

    public void setKeepOpenRequirement(String keepOpenRequirement) {
        this.keepOpenRequirement = keepOpenRequirement;
    }

    public boolean isLockPlayerInv() {
        return lockPlayerInv;
    }

    public void setLockPlayerInv(boolean lockPlayerInv) {
        this.lockPlayerInv = lockPlayerInv;
    }

    public boolean isUpdateInventory() {
        return updateInventory;
    }

    public void setUpdateInventory(boolean updateInventory) {
        this.updateInventory = updateInventory;
    }

    public boolean isTransferArgs() {
        return transferArgs;
    }

    public void setTransferArgs(boolean transferArgs) {
        this.transferArgs = transferArgs;
    }

    public int getForceTransferArgsAmount() {
        return forceTransferArgsAmount;
    }

    public void setForceTransferArgsAmount(int forceTransferArgsAmount) {
        this.forceTransferArgsAmount = forceTransferArgsAmount;
    }

    public List<String> getBindItemLore() {
        return bindItemLore;
    }

    public void setBindItemLore(List<String> bindItemLore) {
        this.bindItemLore = bindItemLore;
    }

    public List<String> getDependExpansions() {
        return dependExpansions;
    }

    public void setDependExpansions(List<String> dependExpansions) {
        this.dependExpansions = dependExpansions;
    }

    public String getLoadedPath() {
        return loadedPath;
    }

    public void setLoadedPath(String loadedPath) {
        this.loadedPath = loadedPath;
    }

    public void reload() {
        File file = new File(getLoadedPath());
        if (file.exists()) {
            List<String> result = MenuLoader.loadMenu(file);
            if (result.size() <= 0 && TrMenu.getSettings().getBoolean("OPTIONS.MENU-FILE-LISTENER.NOTIFY", true)) {
                TLocale.sendToConsole("MENU.LOADED-AUTOLY", file.getName());
            } else {
                TLocale.sendToConsole("MENU.LOADED-AUTOLY-FAILED", file.getName());
                Bukkit.getConsoleSender().sendMessage("§8[§3Tr§bMenu§8] §6WARN §8| §6--------------------------------------------------");
                result.forEach(r -> Bukkit.getConsoleSender().sendMessage("§8[§3Tr§bMenu§8] §bINFO §8| " + r));
                Bukkit.getConsoleSender().sendMessage("§8[§3Tr§bMenu§8] §6WARN §8| §6--------------------------------------------------");
            }
        }
    }

    public static class Load {

        private Menu menu;
        private List<String> errors;

        public Load() {
            this.errors = Lists.newArrayList();
        }

        public Load(Menu menu, List<String> errors) {
            this.menu = menu;
            this.errors = errors;
        }

        public Menu getMenu() {
            return menu;
        }

        public void setMenu(Menu menu) {
            this.menu = menu;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

    }

}
