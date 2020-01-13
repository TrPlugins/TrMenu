package me.arasple.mc.trmenu.hook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.THook;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.TrMenuPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/6 8:08
 * -
 * - %trmenu_tapapi_[玩家ID]_{PAPI变量}% ———— 若目标玩家在线，则返回目标玩家为请求玩家的PAPI变量
 * - %trmenu_query_[URL]_[QUERY] ---- 读取URL中JSON信息, 缓存读取中则返回 Loading... %
 */
@THook
public class HookPlaceholderAPI extends PlaceholderExpansion {

    private static List<Query> queries = new ArrayList<>();

    @Override
    public String getIdentifier() {
        return "trmenu";
    }

    @Override
    public String getAuthor() {
        return "Arasple";
    }

    @Override
    public String getVersion() {
        return TrMenu.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (p == null || !p.isOnline()) {
            return null;
        }

        String[] args = params.toLowerCase().split("_");

        if ("tapapi".equals(args[0])) {
            if (args.length == 3) {
                Player player = Bukkit.getPlayerExact(args[1]);
                player = player == null ? p : player;
                return PlaceholderAPI.setBracketPlaceholders(player, args[2]);
            }
        } else if ("query".equals(args[0])) {
            if (args.length == 3) {
                String url = PlaceholderAPI.setBracketPlaceholders(p, args[1]);
                String query = PlaceholderAPI.setBracketPlaceholders(p, args[2]);
                if (queries.stream().anyMatch(q -> q.getUrl().equals(url))) {
                    JsonObject object = queries.stream().filter(q -> q.getUrl().equals(url)).findFirst().get().getJson();
                    if (object == null) {
                        return "Loading...";
                    } else {
                        return object.get(query).getAsString();
                    }
                } else {
                    queries.add(new Query(url));
                    return "Loading...";
                }
            }
        }
        return null;
    }

    public static class Query {

        private boolean updating;
        private String url;
        private long lastUsed;
        private long lastUpdated;
        private String data;
        private JsonObject json;

        Query(String url) {
            this.url = url;
            update();
        }

        public void update() {
            if (!isUpdating()) {
                setUpdating(true);
                Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                    try {
                        setData(TrMenuPlugin.readFromURL(url));
                        if (data != null) {
                            json = (JsonObject) new JsonParser().parse(getData());
                        }
                    } catch (Throwable ignored) {
                    }
                    setUpdating(false);
                    setLastUpdated(System.currentTimeMillis());
                });
            }
        }

        public boolean isUpdating() {
            return updating;
        }

        public void setUpdating(boolean updating) {
            this.updating = updating;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getLastUsed() {
            return lastUsed;
        }

        public void setLastUsed(long lastUsed) {
            this.lastUsed = lastUsed;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public JsonObject getJson() {
            return json;
        }

        public void setJson(JsonObject json) {
            this.json = json;
        }
    }

}
