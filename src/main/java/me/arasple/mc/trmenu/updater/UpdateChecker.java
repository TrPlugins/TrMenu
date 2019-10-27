package me.arasple.mc.trmenu.updater;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.TrMenuPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/8/15 22:35
 */
@TListener(register = "init")
public class UpdateChecker implements Listener {

    private String url;
    private double version;
    private LatestInfo latest;

    public void init() {
        url = "https://api.github.com/repos/Arasple/" + TrMenu.getPlugin().getDescription().getName() + "/releases/latest";
        version = NumberUtils.toDouble(TrMenu.getPlugin().getDescription().getVersion().split("-")[0], -1);
        latest = new LatestInfo(false, -1, new String[]{});
        if (version == -1) {
            TLocale.sendToConsole("ERROR.VERSION");
            Bukkit.shutdown();
        }
        startTask();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (latest.hasLatest && !latest.noticed[1] && p.hasPermission("litechat.admin")) {
            latest.notifyUpdates(version, p);
            latest.noticed[1] = true;
        }
    }

    private void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // 若已抓取到新版本信息或插件关闭了更新检测, 则取消
                if (latest.hasLatest) {
                    cancel();
                    return;
                }

                String read;
                try (InputStream inputStream = new URL(url).openStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                    read = TrMenuPlugin.readFully(bufferedInputStream, StandardCharsets.UTF_8);
                    JsonObject json = (JsonObject) new JsonParser().parse(read);
                    double latestVersion = json.get("tag_name").getAsDouble();

                    // 如果抓取到新版本
                    if (latestVersion > version) {
                        latest.hasLatest = true;
                        latest.newVersion = latestVersion;
                        latest.noticed[0] = true;
                        latest.updates = json.get("body").getAsString().replace("\r", "").split("\n");
                        latest.notifyUpdates(version, Bukkit.getConsoleSender());
                    } else if (!latest.noticed[0]) {
                        latest.noticed[0] = true;
                        TLocale.sendToConsole(version > latestVersion ? "PLUGIN.UPDATE-NOTIFY.DEV" : "PLUGIN.UPDATE-NOTIFY.LATEST");
                    }
                } catch (Exception ignored) {
                }
            }
        }.runTaskTimerAsynchronously(TrMenu.getPlugin(), 20 * 5, 20 * 60 * 30);
    }

    private static class LatestInfo {

        private boolean hasLatest;
        private double newVersion;
        private String[] updates;
        private boolean[] noticed;

        public LatestInfo(boolean hasLatest, double newVersion, String[] updates) {
            this.hasLatest = hasLatest;
            this.newVersion = newVersion;
            this.updates = updates;
            this.noticed = new boolean[]{false, false};
        }

        public void notifyUpdates(double version, CommandSender sender) {
            List<String> messages = Lists.newArrayList();
            messages.addAll(TLocale.asStringList("PLUGIN.UPDATE-NOTIFY.HEADER", String.valueOf(version), String.valueOf(newVersion)));
            messages.addAll(Arrays.asList(updates));
            messages.addAll(TLocale.asStringList("PLUGIN.UPDATE-NOTIFY.FOOTER"));
            messages.forEach(sender::sendMessage);
        }
    }

}
