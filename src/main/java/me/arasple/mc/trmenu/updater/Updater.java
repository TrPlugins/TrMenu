package me.arasple.mc.trmenu.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.IO;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;
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
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2019/11/29 21:04
 */
public class Updater implements Listener {

    private static boolean autoUpdate;
    private static List<UUID> noticed = new ArrayList<>();
    private static String url;
    private static double version;
    private static boolean old;
    private static double newVersion;

    public static void init(Plugin plugin) {
        url = "https://api.github.com/repos/Arasple/" + plugin.getName() + "/releases/latest";
        version = TrMenu.getTrVersion();
        newVersion = version;
        setAutoUpdate();

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
        if (newVersion - version >= 0.03) {
            int last = Math.min((int) (1 * ((newVersion - version) / 0.01)), 5);
            TLocale.sendToConsole("PLUGIN.UPDATER.TOO-OLD", last);
            try {
                Thread.sleep(last * 1000);
            } catch (InterruptedException ignored) {
            }
        } else {
            if (old) {
                TLocale.sendToConsole("PLUGIN.UPDATER.OLD", newVersion);
            } else {
                TLocale.sendToConsole("PLUGIN.UPDATER." + (version > newVersion ? "DEV" : "LATEST"));
            }
        }
    }

    @TSchedule(delay = 60 * 5, period = 10 * 60 * 20, async = true)
    private static void grabInfo() {
        if (old) {
            return;
        }
        String read;
        try (InputStream inputStream = new URL(url).openStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            read = IO.readFully(bufferedInputStream, StandardCharsets.UTF_8);
            JsonObject json = (JsonObject) new JsonParser().parse(read);
            double latestVersion = json.get("tag_name").getAsDouble();
            if (latestVersion > version) {
                old = true;
            }
            newVersion = latestVersion;
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (old && !noticed.contains(p.getUniqueId()) && p.hasPermission("trmenu.admin")) {
            noticed.add(p.getUniqueId());
            Bukkit.getScheduler().runTaskLaterAsynchronously(TrMenu.getPlugin(), () -> TLocale.sendTo(p, "PLUGIN.UPDATER.OLD", newVersion), 1);
        }
    }

    public static boolean isOld() {
        return old;
    }

    public static double getNewVersion() {
        return newVersion;
    }

    public static double getVersion() {
        return version;
    }

    public static boolean isAutoUpdate() {
        return autoUpdate;
    }

    public static void setAutoUpdate() {
        autoUpdate = TrMenu.SETTINGS.getBoolean("OPTIONS.AUTO-UPDATE", false);
    }

}
