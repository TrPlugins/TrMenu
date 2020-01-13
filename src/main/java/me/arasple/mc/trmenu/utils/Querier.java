package me.arasple.mc.trmenu.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/13 20:50
 */
public class Querier {

    private static final String QUERING = "Loading...";
    private static final String INVAILD = "Invaild";
    private static List<Query> queries = new ArrayList<>();

    @TSchedule(period = 20 * 10)
    static void cleanQueries() {
        queries.removeIf(Query::shouldClean);
    }

    // https://bstats.org/api/v1/plugins/5742/charts/servers/data?maxElements=1
    // https://afdian.net/api/user/get-profile-by-slug?url_slug=Arasple|data.user.creator.monthly_income

    public static String query(String url, String params) {
        Query query = queries.stream().filter(q -> q.getUrl().equals(url)).findFirst().orElse(null);
        if (query != null) {
            JsonElement data = query.getJson();
            if (data == null) {
                query.update();
                return QUERING;
            } else if (data.isJsonArray()) {
                return getArrayValue(data.getAsJsonArray());
            } else if (data.isJsonObject()) {
                JsonElement element = data;
                for (String s : params.split("\\.")) {
                    element = element.getAsJsonObject().get(s);
                }
                return element.getAsJsonPrimitive().getAsString();
            } else {
                return INVAILD;
            }
        } else {
            queries.add(new Query(url));
            return QUERING;
        }
    }

    private static String getArrayValue(JsonArray array) {
        if (array.size() <= 0) {
            return INVAILD;
        } else {
            int index = array.size() - 1;
            if (array.get(index).isJsonArray()) {
                return getArrayValue(array.get(index).getAsJsonArray());
            } else {
                return array.get(index).toString();
            }
        }
    }

    public static class Query {

        private boolean updating;
        private String url;
        private long lastUsed;
        private long lastUpdated;
        private JsonElement json;

        Query(String url) {
            this.url = url;
            update();
        }

        public void update() {
            if (!updating) {
                updating = true;
                Bukkit.getScheduler().runTaskAsynchronously(TrMenu.getPlugin(), () -> {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; GTB7.5; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)");
                        InputStream inputStream = connection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = bufferedInputStream.read(buf)) > 0) {
                            stream.write(buf, 0, len);
                        }
                        String raw = new String(stream.toByteArray(), StandardCharsets.UTF_8);

                        if (raw != null) {
                            json = new JsonParser().parse(raw);
                        }
                        updating = false;
                        lastUpdated = (System.currentTimeMillis());
                    } catch (Throwable e) {
                        updating = false;
                    }
                });
            }
        }

        boolean shouldClean() {
            return System.currentTimeMillis() - lastUsed > 30 * 60 * 20;
        }

        String getUrl() {
            return url;
        }

        JsonElement getJson() {
            lastUsed = System.currentTimeMillis();
            if (System.currentTimeMillis() - lastUpdated > 3 * 60 * 20) {
                update();
            }
            return json;
        }

        public void setJson(JsonObject json) {
            this.json = json;
        }

    }


}
