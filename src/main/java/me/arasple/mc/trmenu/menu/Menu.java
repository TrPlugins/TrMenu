package me.arasple.mc.trmenu.menu;

import com.google.common.collect.Lists;
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
import me.arasple.mc.trmenu.nms.InvTitler;
import me.arasple.mc.trmenu.utils.JavaScript;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Menu {

    private String name;
    private InventoryType inventoryType;
    private int rows;
    private HashMap<Button, List<Integer>> buttons;
    private List<String> titles;
    private int titleUpdate;
    private HashMap<UUID, Integer> titleIndex;
    private List<String> openCommands;
    private List<AbstractAction> openActions;
    private List<AbstractAction> closeActions;
    private List<AbstractAction> openDenyActions;
    private List<AbstractAction> closeDenyActions;
    private String openRequirement;
    private String closeRequirement;
    private boolean lockPlayerInv;
    private boolean updateInventory;
    private boolean transferArgs;
    private int forceTransferArgsAmount;
    private List<String> bindItemLore;
    private List<String> dependExpansions;
    private String loadedPath;

    public Menu(String name, List<String> titles, int titleUpdate, InventoryType inventoryType, int rows, HashMap<Button, List<Integer>> buttons, String openRequirement, List<AbstractAction> openDenyActions, String closeRequirement, List<AbstractAction> closeDenyActions, List<String> openCommands, List<AbstractAction> openActions, List<AbstractAction> closeActions, boolean lockPlayerInv, boolean updateInventory, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore, List<String> dependExpansions) {
        setValues(name, titles, titleUpdate, inventoryType, rows, buttons, openRequirement, openDenyActions, closeRequirement, closeDenyActions, openCommands, openActions, closeActions, lockPlayerInv, updateInventory, transferArgs, forceTransferArgsAmount, bindItemLore, dependExpansions);
    }

    private void setValues(String name, List<String> title, int titleUpdate, InventoryType inventoryType, int rows, HashMap<Button, List<Integer>> buttons, String openRequirement, List<AbstractAction> openDenyActions, String closeRequirement, List<AbstractAction> closeDenyActions, List<String> openCommands, List<AbstractAction> openActions, List<AbstractAction> closeActions, boolean lockPlayerInv, boolean updateInventory, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore, List<String> dependExpansions) {
        this.name = name;
        this.titles = title;
        this.titleUpdate = titleUpdate;
        this.titleIndex = new HashMap<>();
        this.inventoryType = inventoryType;
        this.rows = rows;
        this.buttons = buttons;
        this.openRequirement = openRequirement;
        this.openDenyActions = openDenyActions;
        this.closeRequirement = closeRequirement;
        this.closeDenyActions = closeDenyActions;
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
        open(player, false, args);
    }

    /**
     * 为一名玩家打开这个菜单
     *
     * @param player    玩家
     * @param byConsole 是否由控制台命令操作
     * @param args      传递参数
     */
    public void open(Player player, boolean byConsole, String... args) {
        if (!initEvent(player, byConsole, args)) {
            return;
        }
        // 创建容器
        Inventory menu = inventoryType == null ? Bukkit.createInventory(new MenuHolder(this), 9 * rows, Vars.replace(player, titles.get(0))) : Bukkit.createInventory(new MenuHolder(this), inventoryType, Vars.replace(player, titles.get(0)));
        // 初始化容器
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            // 布置按钮
            buttons.forEach((button, slots) -> {
                        button.refreshConditionalIcon(player, null);
                        Item item = button.getIcon(player).getItem();
                        ItemStack itemStack = item.createItemStack(player, args);
                        for (int i : item.getSlots(player).size() > 0 ? item.getNextSlots(player, menu) : slots) {
                            if (menu.getSize() > i) {
                                menu.setItem(i, itemStack);
                            }
                        }
                        if (slots != null && !slots.isEmpty()) {
                            newUpdateTask(player, button, menu, slots, args);
                            newRefreshTask(player, button, menu);
                        }
                    }
            );
            // 设置标题
            newTitleUpdateTask(player, menu);
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
            // 打开菜单
            Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> {
                player.openInventory(menu);
                if (openActions != null) {
                    openActions.forEach(action -> action.run(player));
                }
            }, 2);
        });
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
                    InvTitler.setTitle(player, menu, Vars.replace(player, getTitle(player)));
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
                for (int i : item.getSlots(player).size() > 0 ? item.getNextSlots(player, menu) : slots) {
                    if (menu.getSize() > i) {
                        menu.setItem(i, itemStack);
                    }
                }
                clearEmptySlots(player, menu);
            }
        }.runTaskTimerAsynchronously(TrMenu.getPlugin(), update, update);
    }

    /**
     * 初始化打开菜单的事件
     *
     * @param player    玩家
     * @param byConsole 是否由控制台操作
     * @param args      参数
     * @return
     */
    private boolean initEvent(Player player, boolean byConsole, String... args) {
        MenuOpenEvent event = new MenuOpenEvent(player, byConsole, this);
        if (event.isCancelled()) {
            return false;
        }
        if (event.getMenu() != this) {
            event.getMenu().open(player, event.isByConsole(), args);
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
        ArgsCache.getArgs().put(player.getUniqueId(), args);
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
        for (Map.Entry<Button, List<Integer>> entry : buttons.entrySet()) {
            Icon icon = entry.getKey().getIcon(player);
            if (icon.getItem().getCurSlots(player) != null && icon.getItem().getCurSlots(player).contains(slot)) {
                return entry.getKey();
            } else if (entry.getValue().contains(slot) && icon.getItem().getSlots(player).isEmpty()) {
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

    /*
    GETTERS & SETTERS
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public HashMap<Button, List<Integer>> getButtons() {
        return buttons;
    }

    public void setButtons(HashMap<Button, List<Integer>> buttons) {
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

}
