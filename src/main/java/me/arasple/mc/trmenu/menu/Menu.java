package me.arasple.mc.trmenu.menu;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.traction.TrAction;
import me.arasple.mc.traction.base.AbstractAction;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.api.events.MenuOpenEvent;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.utils.JavaScript;
import me.arasple.mc.trmenu.utils.Vars;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.cloud.CloudExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Menu {

    private String name;
    private InventoryType inventoryType;
    private String title;
    private int rows;
    private HashMap<Button, List<Integer>> buttons;
    private List<String> openCommands;
    private List<AbstractAction> openActions;
    private List<AbstractAction> closeActions;
    private List<AbstractAction> openDenyActions;
    private List<AbstractAction> closeDenyActions;
    private String openRequirement;
    private String closeRequirement;
    private boolean lockPlayerInv;
    private boolean transferArgs;
    private int forceTransferArgsAmount;
    private List<String> bindItemLore;
    private List<String> dependExpansions;
    private File loadedFrom;

    public Menu(String name, String title, InventoryType inventoryType, int rows, HashMap<Button, List<Integer>> buttons, String openRequirement, List<AbstractAction> openDenyActions, String closeRequirement, List<AbstractAction> closeDenyActions, List<String> openCommands, List<AbstractAction> openActions, List<AbstractAction> closeActions, boolean lockPlayerInv, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore, List<String> dependExpansions) {
        setValues(name, title, inventoryType, rows, buttons, openRequirement, openDenyActions, closeRequirement, closeDenyActions, openCommands, openActions, closeActions, lockPlayerInv, transferArgs, forceTransferArgsAmount, bindItemLore, dependExpansions);
    }

    public void setValues(String name, String title, InventoryType inventoryType, int rows, HashMap<Button, List<Integer>> buttons, String openRequirement, List<AbstractAction> openDenyActions, String closeRequirement, List<AbstractAction> closeDenyActions, List<String> openCommands, List<AbstractAction> openActions, List<AbstractAction> closeActions, boolean lockPlayerInv, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore, List<String> dependExpansions) {
        this.name = name;
        this.title = title;
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
        this.transferArgs = transferArgs;
        this.forceTransferArgsAmount = forceTransferArgsAmount;
        this.bindItemLore = bindItemLore;
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
        MenuOpenEvent event = new MenuOpenEvent(player, this);
        if (shouldCancelEvent(event, player)) {
            event.setCancelled(true);
            return;
        }

        ArgsCache.getArgs().put(player.getUniqueId(), args);
        Inventory menu = inventoryType == null ? Bukkit.createInventory(new MenuHolder(this), 9 * rows, Vars.replace(player, title)) : Bukkit.createInventory(new MenuHolder(this), inventoryType, Vars.replace(player, title));

        buttons.forEach((button, slots) -> {
                    Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                        button.refreshConditionalIcon(player, null);
                        Item item = button.getIcon().getItem();
                        ItemStack itemStack = item.createItemStack(player, args);
                        for (int i : item.getSlots().size() > 0 ? item.getNextSlots(menu) : slots) {
                            if (menu.getSize() > i) {
                                menu.setItem(i, itemStack);
                            }
                        }
                    });

                    if (slots != null) {
                        if (button.getUpdate() >= 1) {
                            int update = Math.max(button.getUpdate(), 3);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    long start = System.currentTimeMillis();
                                    if (!player.getOpenInventory().getTopInventory().equals(menu)) {
                                        cancel();
                                        return;
                                    }
                                    Item item = button.getIcon().getItem();
                                    ItemStack itemStack = item.createItemStack(player, args);
                                    for (int i : item.getSlots().size() > 0 ? item.getNextSlots(menu) : slots) {
                                        if (menu.getSize() > i) {
                                            menu.setItem(i, itemStack);
                                        }
                                    }
                                    clearEmptySlots(menu, item.getSlots());
                                }
                            }.runTaskTimerAsynchronously(TrMenu.getPlugin(), update, update);
                        }
                        if (button.getRefresh() >= 1 && button.getIcons().size() > 0) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    button.refreshConditionalIcon(player, null);
                                }
                            }.runTaskTimer(TrMenu.getPlugin(), button.getRefresh(), button.getRefresh());
                        }
                    }
                }
        );

        Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> {
            player.openInventory(menu);
            if (openActions != null) {
                openActions.forEach(action -> action.run(player));
            }
        }, 2);
    }

    private boolean shouldCancelEvent(MenuOpenEvent event, Player player, String... args) {
        if (event.isCancelled()) {
            return true;
        }
        if (event.getMenu() != this) {
            event.getMenu().open(player, args);
            return true;
        }
        if (!Strings.isBlank(openRequirement) && !(boolean) JavaScript.run(player, openRequirement)) {
            event.setCancelled(true);
            TrAction.runActions(openActions.listIterator(), player);
            return true;
        }
        List<String> unInstalledDepends = checkDepends();
        if (unInstalledDepends.size() > 0) {
            TLocale.sendTo(player, "MENU.DEPEND-EXPANSIONS-REQUIRED", Arrays.toString(unInstalledDepends.toArray()));
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    /**
     * 检测菜单需要的 PlaceholderAPI 依赖并自动下载
     *
     * @return 未安装的
     */
    private List<String> checkDepends() {
        List<String> unInstalled = Lists.newArrayList();
        if (dependExpansions != null && dependExpansions.size() > 0) {
            if (PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansions().isEmpty()) {
                PlaceholderAPIPlugin.getInstance().getExpansionCloud().fetch(false);
            }
            unInstalled = dependExpansions.stream().filter(d -> PlaceholderAPI.getExpansions().stream().noneMatch(e -> e.getName().equalsIgnoreCase(d)) && PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(d) != null && !PlaceholderAPIPlugin.getInstance().getExpansionCloud().isDownloading(d)).collect(Collectors.toList());
            if (unInstalled.size() > 0) {
                unInstalled.forEach(ex -> {
                    CloudExpansion cloudExpansion = PlaceholderAPIPlugin.getInstance().getExpansionCloud().getCloudExpansion(ex);
                    PlaceholderAPIPlugin.getInstance().getExpansionCloud().downloadExpansion(null, cloudExpansion);
                });
                Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> PlaceholderAPIPlugin.getInstance().getExpansionManager().registerAllExpansions(), 20);
            }
        }
        return unInstalled;
    }

    public Button getButton(int slot) {
        if (slot < 0) {
            return null;
        }
        for (Map.Entry<Button, List<Integer>> entry : buttons.entrySet()) {
            Icon icon = entry.getKey().getIcon();
            if (icon.getItem().getCurSlots() != null && icon.getItem().getCurSlots().contains(slot)) {
                return entry.getKey();
            } else if (entry.getValue().contains(slot)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void clearEmptySlots(Inventory menu, List<List<Integer>> slots) {
        slots.forEach(s -> s.forEach(i -> {
            if (menu.getItem(i) != null && getButton(i) == null) {
                if (menu.getSize() > i) {
                    menu.setItem(i, null);
                }
            }
        }));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public File getLoadedFrom() {
        return loadedFrom;
    }

    public void setLoadedFrom(File loadedFrom) {
        this.loadedFrom = loadedFrom;
    }

    List<Player> getViewers() {
        return Bukkit.getOnlinePlayers().stream().filter(p -> {
            InventoryHolder holder = p.getOpenInventory().getTopInventory().getHolder();
            return holder instanceof MenuHolder && ((MenuHolder) holder).getMenu() == this;
        }).collect(Collectors.toList());
    }

}
