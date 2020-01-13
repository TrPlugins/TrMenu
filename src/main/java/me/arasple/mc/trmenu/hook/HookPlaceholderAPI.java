package me.arasple.mc.trmenu.hook;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.THook;
import io.izzel.taboolib.module.inject.TSchedule;
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
 * - %trmenu_tapapi_[玩家ID] | {PAPI变量}% ———— 若目标玩家在线，则返回目标玩家为请求玩家的PAPI变量
 * - %trmenu_query_[URL] | [QUERY] ---- 读取URL中JSON信息, 缓存读取中则返回 Loading... %
 * <p>
 * %trmenu_query_https://afdian.net/api/user/get-profile-by-slug?url_slug=Arasple|data.user.creator.monthly_income%
 */
@THook
public class HookPlaceholderAPI extends PlaceholderExpansion {

    private static List<Query> queries = new ArrayList<>();

    @TSchedule(period = 20 * 10)
    static void cleanQueries() {
        queries.removeIf(query -> System.currentTimeMillis() - query.getLastUsed() > 30 * 60 * 20);
    }

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

        String[] type = params.split("_", 2);

        if (type.length != 2) {
            return null;
        }

        String[] args = type[1].split("\\|");

        if ("tapapi".equals(type[0])) {
            if (args.length == 2) {
                Player player = Bukkit.getPlayerExact(args[0]);
                player = player == null ? p : player;
                return PlaceholderAPI.setBracketPlaceholders(player, args[1]);
            }
        } else if ("query".equals(type[0])) {
            if (args.length == 2) {
                String url = PlaceholderAPI.setBracketPlaceholders(p, args[0]);
                String query = PlaceholderAPI.setBracketPlaceholders(p, args[1]);
                if (queries.stream().anyMatch(q -> q.getUrl().equals(url))) {
                    JsonObject object = queries.stream().filter(q -> q.getUrl().equals(url)).findFirst().get().getJson();
                    if (object == null) {
                        return "Loading...";
                    } else {
                        JsonElement element = object;
                        for (String s : query.split("\\.")) {
                            if (element.isJsonObject()) {
                                element = element.getAsJsonObject().get(s);
                            }
                        }
                        return element.getAsJsonPrimitive().getAsString();
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
            setLastUsed(System.currentTimeMillis());
            if (System.currentTimeMillis() - getLastUpdated() > 3 * 60 * 20) {
                update();
            }
            return json;
        }

        public void setJson(JsonObject json) {
            this.json = json;
        }
    }

}
