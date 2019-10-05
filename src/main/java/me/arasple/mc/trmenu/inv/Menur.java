package me.arasple.mc.trmenu.inv;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.BaseAction;
import me.arasple.mc.trmenu.api.events.MenuOpenEvent;
import me.arasple.mc.trmenu.data.ArgsCache;
import me.arasple.mc.trmenu.display.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

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

    private boolean lockPlayerInv;
    private boolean transferArgs;
    private int forceTransferArgsAmount;
    private List<String> bindItemLore;

    public Menur(String name, String title, int rows, HashMap<Button, List<Integer>> buttons, List<String> openCommands, List<BaseAction> openActions, List<BaseAction> closeActions, boolean lockPlayerInv, boolean transferArgs, int forceTransferArgsAmount, List<String> bindItemLore) {
        this.name = name;
        this.title = title;
        this.rows = rows;
        this.buttons = buttons;
        this.openCommands = openCommands;
        this.openActions = openActions;
        this.closeActions = closeActions;
        this.lockPlayerInv = lockPlayerInv;
        this.transferArgs = transferArgs;
        this.forceTransferArgsAmount = forceTransferArgsAmount;
        this.bindItemLore = bindItemLore;
    }

    public void open(Player player, String... args) {
        MenuOpenEvent event = new MenuOpenEvent(player, this);
        if (event.isCancelled()) {
            return;
        }
        if (event.getMenu() != this) {
            event.getMenu().open(player, args);
            return;
        }

        Inventory menu = Bukkit.createInventory(new MenurHolder(this), 9 * rows, TLocale.Translate.setPlaceholders(player, Strings.replaceWithOrder(title, args)));
        // 初始化设置
        buttons.forEach((button, slots) -> Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            button.refreshConditionalIcon(player, null, null);
            ItemStack itemStack = button.getCurrentIcon().getItem().createItemStack(player, args);
            for (int i : slots) {
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
                        ItemStack itemStack = button.getCurrentIcon().getItem().createItemStack(player, args);
                        for (Integer slot : slots) {
                            menu.setItem(slot, itemStack);
                        }
                    }
                }.runTaskTimerAsynchronously(TrMenu.getPlugin(), button.getUpdatePeriod(), button.getUpdatePeriod());
            }
            // 判断重新计算优先级
            if (slots != null && button.getRefreshConditions() >= 20 && button.getConditionalIcons().size() > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        button.refreshConditionalIcon(player, null, null);
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
        if (slot < 0 || buttons.entrySet().stream().noneMatch(entry -> entry.getValue().contains(slot))) {
            return null;
        }
        return buttons.entrySet().stream().filter(entry -> entry.getValue().contains(slot)).findFirst().get().getKey();
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

    public boolean isTransferArgs() {
        return transferArgs;
    }

    public void setTransferArgs(boolean transferArgs) {
        this.transferArgs = transferArgs;
    }

    public boolean isLockPlayerInv() {
        return lockPlayerInv;
    }

    public void setLockPlayerInv(boolean lockPlayerInv) {
        this.lockPlayerInv = lockPlayerInv;
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
