package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.item.Items;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.utils.Hastebin;
import me.arasple.mc.trmenu.utils.JsonItem;
import org.bukkit.Bukkit;
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
    public Argument[] getArguments() {
        return new Argument[]{new Argument("JSON", false)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        ItemStack item = getItemInHand((Player) sender);

        if (args.length >= 1) {
            String json = args[0];
            if (JsonItem.isJson(json)) {
                ItemStack itemFromJson = JsonItem.fromJson(json);
                if (itemFromJson != null) {
                    ((Player) sender).getInventory().addItem(itemFromJson);
                    TLocale.sendTo(sender, "COMMANDS.JSON-TO-ITEM.SUCCESS");
                } else {
                    TLocale.sendTo(sender, "COMMANDS.JSON-TO-ITEM.FAILED");
                }
            }
            return;
        }

        if (Items.isNull(item)) {
            TLocale.sendTo(sender, "COMMANDS.NO-ITEM");
        } else {
            String json = JsonItem.toJson(item);
            if (json.length() < 200) {
                TLocale.sendTo(sender, "COMMANDS.ITEM-TO-JSON", json);
            } else {
                TLocale.sendTo(sender, "HASTEBIN.PROCESSING");
                Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                    String url = Hastebin.paste(json);
                    TLocale.sendTo(sender, url != null ? "HASTEBIN.SUCCESS" : "HASTEBIN.FAILED", url);
                });
            }
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
