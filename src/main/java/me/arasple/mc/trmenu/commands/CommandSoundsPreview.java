package me.arasple.mc.trmenu.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.inventory.ClickType;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import io.izzel.taboolib.util.lite.Materials;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bukkit.event.inventory.ClickType.*;

/**
 * @author Arasple
 * @date 2020/2/9 16:12
 */
public class CommandSoundsPreview extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Filter", false)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        open((Player) sender, 1, args.length > 0 ? args[0] : null);
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    private void open(Player player, int page, String filter) {
        Map<Integer, Sound> soundMap = new HashMap<>();
        final boolean[] has = {false, false};
        MenuBuilder.builder()
                .title("Sounds " + ((Strings.nonEmpty(filter) ? "[" + filter + "] " : " ") + "(" + page + ")"))
                .rows(6)
                .items(
                        "$########",
                        "         ",
                        "         ",
                        "         ",
                        "         ",
                        "#########"
                )
                .put('#', new ItemBuilder((Materials.GRAY_STAINED_GLASS_PANE.parseItem())).name("§7Tr§fMenu §3Sounds").build())
                .put('$', new ItemBuilder((Materials.GREEN_STAINED_GLASS_PANE.parseItem())).name("§2Stop playing sounds").build())
                .event(e -> {
                    e.setCancelled(true);
                    Player clicker = e.getClicker();

                    if (e.getSlot() == '$') {
                        stopSounds(clicker);
                        return;
                    }

                    Sound sound = soundMap.get(e.getRawSlot());
                    Object type = e.getClickType() == ClickType.CLICK ? e.castClick().getClick() : null;
                    if (has[0] && e.getRawSlot() == 53) {
                        open(player, page + 1, filter);
                        return;
                    } else if (has[1] && e.getRawSlot() == 45) {
                        open(player, page - 1, filter);
                        return;
                    }
                    if (clicker != null && sound != null && type != null) {
                        if (type == DROP) {
                            clicker.playSound(clicker.getLocation(), sound, 1f, 0f);
                        } else if (type == LEFT) {
                            clicker.playSound(clicker.getLocation(), sound, 1f, 1f);
                        } else if (type == RIGHT) {
                            clicker.playSound(clicker.getLocation(), sound, 1f, 2f);
                        } else if (type == MIDDLE) {
                            TellrawJson.create().newLine().append("§8▶ §7CLICK TO COPY: ").append("§a§n" + sound.name()).clickSuggest(sound.name()).hoverText("§8Click this text").newLine().send(clicker);
                        }
                    }
                })
                .buildAsync(inventory -> {
                    List<Sound> sounds = Arrays.stream(Sound.values()).filter(s -> Strings.isEmpty(filter) || s.name().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
                    sounds = sounds.subList(36 * (page - 1), sounds.size());
                    has[0] = sounds.size() > 36;
                    has[1] = page > 1;
                    for (int i = 9; i < 45; i++) {
                        if (i >= sounds.size()) {
                            break;
                        }
                        soundMap.put(i, sounds.get(i));
                        inventory.setItem(i,
                                new ItemBuilder(Material.PAPER)
                                        .name("§f§n" + sounds.get(i).name())
                                        .lore(
                                                "",
                                                "§8· §7Press §fQ §7for Pitch 0",
                                                "§8· §7Left Click for Pitch 1",
                                                "§8· §7Right Click for Pitch 2",
                                                "",
                                                "§e▶ §6Middle-click to copy the name."
                                        )
                                        .build()
                        );
                        if (has[0]) {
                            inventory.setItem(53, new ItemBuilder(Materials.LIME_STAINED_GLASS_PANE.parseItem()).name("§aNext Page").build());
                        }
                        if (has[1]) {
                            inventory.setItem(45, new ItemBuilder(Materials.CYAN_STAINED_GLASS_PANE.parseItem()).name("§3Previous Page").build());
                        }
                    }
                }).open(player);
    }

    public static void stopSounds(Player clicker) {
        Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
            for (Sound value : Sound.values()) {
                clicker.stopSound(value);
            }
        });
    }

}
