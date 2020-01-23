package me.arasple.mc.trmenu.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.TrMenuPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/11/29 21:04
 */
public class Updater implements Listener {

    private static List<UUID> noticed = new ArrayList<>();
    private static String url;
    private static double version;
    private static UpdateInfo latest;

    public static void init(Plugin plugin) {
        url = "https://api.github.com/repos/Arasple/" + plugin.getName() + "/releases/latest";
        version = TrMenu.getTrVersion();
        latest = new UpdateInfo();

        if (!String.valueOf(version).equalsIgnoreCase(plugin.getDescription().getVersion().split("-")[0])) {
            TLocale.sendToConsole("ERROR.VERSION");
            Bukkit.shutdown();
        }

        Bukkit.getPluginManager().registerEvents(new Updater(), plugin);
        startTask();
    }

    private static void startTask() {
        grabInfo();
        notifyOld();
    }

    private static void notifyOld() {
        if (latest.newVersion - version >= 0.03) {
            int last = Math.min((int) (1 * ((latest.newVersion - version) / 0.01)), 5);
            TLocale.sendToConsole("PLUGIN.UPDATE-NOTIFY.TOO-OLD", last);
            try {
                Thread.sleep(last * 1000);
            } catch (InterruptedException ignored) {
            }
        } else {
            if (latest.hasLatest) {
                latest.notifyUpdates(version, Bukkit.getConsoleSender());
            } else {
                TLocale.sendToConsole("PLUGIN.UPDATE-NOTIFY." + (version > latest.newVersion ? "DEV" : "LATEST"));
            }
        }
    }

    @TSchedule(delay = 60 * 5, period = 10 * 60 * 20, async = true)
    private static void grabInfo() {
        if (latest.hasLatest) {
            return;
        }
        String read;
        try (InputStream inputStream = new URL(url).openStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            read = TrMenuPlugin.readFully(bufferedInputStream, StandardCharsets.UTF_8);
            JsonObject json = (JsonObject) new JsonParser().parse(read);
            double latestVersion = json.get("tag_name").getAsDouble();
            if (latestVersion > version) {
                latest.hasLatest = true;
            }
            latest.newVersion = latestVersion;
            latest.updates = json.get("body").getAsString().replace("\r", "").split("\n");
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (latest.hasLatest && !noticed.contains(p.getUniqueId()) && p.hasPermission("trmenu.admin")) {
            noticed.add(p.getUniqueId());
            Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.getPlugin(), () -> latest.notifyUpdates(version, p), 1);
        }
    }

    private static class UpdateInfo {

        private List<String> info;
        private boolean hasLatest;
        private double newVersion;
        private String[] updates;

        public UpdateInfo() {
            this(false, -1, new String[]{});
        }

        public UpdateInfo(boolean hasLatest, double newVersion, String[] updates) {
            this.hasLatest = hasLatest;
            this.newVersion = newVersion;
            this.updates = updates;
            this.info = new ArrayList<>();
        }

        public void notifyUpdates(double version, CommandSender sender) {
            if (info.isEmpty()) {
                info.addAll(TLocale.asStringList("PLUGIN.UPDATE-NOTIFY.HEADER", String.valueOf(version), String.valueOf(newVersion)));
                info.addAll(Arrays.asList(updates));
                info.addAll(TLocale.asStringList("PLUGIN.UPDATE-NOTIFY.FOOTER"));
            }
            info.forEach(sender::sendMessage);
        }

    }

}
