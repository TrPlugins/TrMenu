package me.arasple.mc.trmenu.inv;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.api.events.MenuOpenEvent;
import me.arasple.mc.trmenu.bstats.Metrics;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.display.Button;
import me.arasple.mc.trmenu.display.Icon;
import me.arasple.mc.trmenu.display.Item;
import me.arasple.mc.trmenu.utils.JavaScript;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arasple
 * @date 2019/10/4 14:09
 */
public class Menur {

    private String name;
    private String title;
    private int rows;
    private HashMap<Button, List<Integer>> buttons;
    private List<String> openCommands;
    private List<BaseAction> openActions, closeActions;

    private String openRequirement;
    private List<BaseAction> openDenyActions;

    private boolean lockPlayerInv;
    private boolean transferArgs;
    private int forceTransferArgsAmount;
    private List<String> bindItemLore;

    public Menur(String name, String title, int rows, HashMap<Button, List<Integer>> buttons, String openRequirement, List<BaseAction> openDenyActions, List<String> openCommands, List<BaseAction> openActions, List<BaseAction> closeActions, boolean lockPlayerInv, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore) {
        this.name = name;
        this.title = title;
        this.rows = rows;
        this.buttons = buttons;
        this.openRequirement = openRequirement;
        this.openDenyActions = openDenyActions;
        this.openCommands = openCommands;
        this.openActions = openActions;
        this.closeActions = closeActions;
        this.lockPlayerInv = lockPlayerInv;
        this.transferArgs = transferArgs;
        this.forceTransferArgsAmount = forceTransferArgsAmount;
        this.bindItemLore = bindItemLore;
    }

    public void open(Player player, String... args) {
        Metrics.increase(0);

        MenuOpenEvent event = new MenuOpenEvent(player, this);
        if (event.isCancelled()) {
            return;
        }
        if (event.getMenu() != this) {
            event.getMenu().open(player, args);
            return;
        }
        if (!player.hasPermission("trmenu.admin") && !Strings.isBlank(openRequirement) && !(boolean) JavaScript.run(player, openRequirement, null, args)) {
            event.setCancelled(true);
            if (getOpenDenyActions() != null) {
                getOpenDenyActions().forEach(action -> action.onExecute(player, null, args));
            }
            return;
        }

        Inventory menu = Bukkit.createInventory(new MenurHolder(this), 9 * rows, TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(title, args)));
        // 初始化设置
        buttons.forEach((button, slots) -> Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            button.refreshConditionalIcon(player, null);
            Item item = button.getCurrentIcon().getItem();
            ItemStack itemStack = item.createItemStack(player, args);
            for (int i : item.getSlots().size() > 0 ? item.getNextSlots() : slots) {
                menu.setItem(i, itemStack);
            }
        }));

        // 开始刷新
        buttons.forEach((button, slots) -> {
            // 判断刷新周期是否合法
            if (slots != null && button.getUpdatePeriod() >= 1) {
                // 创建异步 BukkitRunnable
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // 如果该菜单已被玩家关闭, 则取消刷新进程
                        if (!player.getOpenInventory().getTopInventory().equals(menu)) {
                            cancel();
                            return;
                        }
                        Item item = button.getCurrentIcon().getItem();
                        ItemStack itemStack = item.createItemStack(player, args);
                        for (int i : item.getSlots().size() > 0 ? item.getNextSlots() : slots) {
                            menu.setItem(i, itemStack);
                        }
                        // 清理残留
                        clearEmptySlots(menu, item.getSlots());
                    }
                }.runTaskTimerAsynchronously(TrMenu.getPlugin(), button.getUpdatePeriod(), button.getUpdatePeriod());
            }
            // 判断重新计算优先级
            if (slots != null && button.getRefreshConditions() >= 1 && button.getConditionalIcons().size() > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        button.refreshConditionalIcon(player, null);
                    }
                }.runTaskTimerAsynchronously(TrMenu.getPlugin(), button.getRefreshConditions(), button.getRefreshConditions());
            }
        });

        // 为玩家打开此菜单
        Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> {
            ArgsCache.getArgs().put(player.getUniqueId(), args);
            player.openInventory(menu);
            if (getOpenActions() != null) {
                getOpenActions().forEach(action -> action.onExecute(player, null, args));
            }
        }, 2);
    }

    public Button getButton(int slot) {
        if (slot < 0) {
            return null;
        }
        for (Map.Entry<Button, List<Integer>> entry : buttons.entrySet()) {
            Icon icon = entry.getKey().getCurrentIcon();
            if (icon.getItem().getCurrentSlots() != null && icon.getItem().getCurrentSlots().contains(slot)) {
                return entry.getKey();
            } else if (entry.getValue().contains(slot)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void clearEmptySlots(Inventory menu, List<List<Integer>> slots) {
        slots.forEach(s -> s.forEach(i -> {
            if (menu.getItem(i) != null && getButton(i) == null) {
                menu.setItem(i, null);
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

    public List<BaseAction> getOpenActions() {
        return openActions;
    }

    public void setOpenActions(List<BaseAction> openActions) {
        this.openActions = openActions;
    }

    public List<BaseAction> getCloseActions() {
        return closeActions;
    }

    public void setCloseActions(List<BaseAction> closeActions) {
        this.closeActions = closeActions;
    }

    public String getOpenRequirement() {
        return openRequirement;
    }

    public void setOpenRequirement(String openRequirement) {
        this.openRequirement = openRequirement;
    }

    public List<BaseAction> getOpenDenyActions() {
        return openDenyActions;
    }

    public void setOpenDenyActions(List<BaseAction> openDenyActions) {
        this.openDenyActions = openDenyActions;
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

}
