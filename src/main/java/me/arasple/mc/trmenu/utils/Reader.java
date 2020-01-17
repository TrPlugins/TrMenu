package me.arasple.mc.trmenu.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Arasple
 * @date 2020/1/17 14:50
 */
public class Reader {

    public static JsonElement readFileAsJson(File file) {
        try {
            return new JsonParser().parse(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
