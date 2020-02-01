package me.arasple.mc.trmenu.utils;

import com.google.common.collect.Lists;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.izzel.taboolib.module.dependency.Dependency;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trmenu.TrMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arasple
 * @date 2020/1/31 21:09
 */
@Dependency(mavenRepo = "https://maven.aliyun.com/repository/central", maven = "org.codehaus.groovy:groovy:3.0.0-rc-3")
public class ItemSources {

    private static List<Source> sources = Lists.newArrayList();

    public static void loadSources() {
        sources.clear();
        TrMenu.getSettings().getConfigurationSection("ITEM-SOURCES").getKeys(false).forEach(name -> {
            String depend = TrMenu.getSettings().getString("ITEM-SOURCES." + name + ".depend");
            String source = TrMenu.getSettings().getString("ITEM-SOURCES." + name + ".source");
            if (Strings.nonEmpty(depend) && Strings.nonEmpty(source)) {
                sources.add(new Source(name, depend, source));
            }
        });
    }

    public static List<Source> getSources() {
        return sources;
    }

    public static class Source {

        private final String name;
        private final String depend;
        private final String source;
        private HashMap<String, ItemStack> item = new HashMap<>();

        public Source(String name, String depend, String source) {
            this.name = name;
            this.depend = depend;
            this.source = source;
        }

        public void refreshItem(String input) {
            if (!Strings.isEmpty(depend) && Bukkit.getPluginManager().getPlugin(depend) == null) {
                return;
            }
            Binding bindings = new Binding();
            GroovyShell groovyShell = new GroovyShell(bindings);
            try {
                Object output = groovyShell.evaluate(source.replace("{input}", input));
                if (!(output instanceof ItemStack)) {
                    return;
                }
                item.put(input, (ItemStack) output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        public String getDepend() {
            return depend;
        }

        public String getSource() {
            return source;
        }

        public Map<String, ItemStack> getItem() {
            return item;
        }

    }

}
