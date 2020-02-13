package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.util.lite.SoundPack;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.commands.CommandSoundsPreview;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:05
 */
public class ActionSound extends AbstractAction {

    @Override
    public String getName() {
        return "sound|playsound";
    }

    @Override
    public void onExecute(Player player) {
        String sound = getContent(player);
        if ("stop".equalsIgnoreCase(sound)) {
            CommandSoundsPreview.stopSounds(player);
        } else {
            new SoundPack(sound).play(player);
        }
    }

    @Override
    public void setContent(String content) {
        String[] split = content.split("-");
        if (split.length == 1) {
            content += "-1-1";
        }
        super.setContent(content);
    }

}
