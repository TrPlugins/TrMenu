package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.trmenu.utils.JsonItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Arasple
 * @date 2020/1/20 11:54
 */
public class CommandItemToJson extends BaseSubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        ItemStack item = getItemInHand((Player) sender);
        if (Items.isNull(item)) {
            TLocale.sendTo(sender, "COMMANDS.NO-ITEM");
        } else {
            TLocale.sendTo(sender, "COMMANDS.ITEM-TO-JSON", JsonItem.toJson(item));
        }
    }

    @SuppressWarnings("deprecation")
    private ItemStack getItemInHand(Player player) {
        try {
            return player.getInventory().getItemInMainHand();
        } catch (Throwable e) {
            return player.getInventory().getItemInHand();
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

}
