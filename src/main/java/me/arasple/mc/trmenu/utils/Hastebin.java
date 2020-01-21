package me.arasple.mc.trmenu.utils;

import io.izzel.taboolib.internal.gson.JsonElement;
import io.izzel.taboolib.internal.gson.JsonParser;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Arasple
 * @date 2020/1/20 16:40
 */
public class Hastebin {

    private static final String HASTEBIN_URL = "https://hasteb.in/";

    public static String paste(String content) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(HASTEBIN_URL + "documents").openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Charset", "UTF-8");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(content.getBytes(StandardCharsets.UTF_8));
            JsonElement json = new JsonParser().parse(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            return HASTEBIN_URL + json.getAsJsonObject().get("key").getAsString();
        } catch (Throwable e) {
            return null;
        }
    }

}
