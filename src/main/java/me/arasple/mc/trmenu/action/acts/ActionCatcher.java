package me.arasple.mc.trmenu.action.acts;

import io.izzel.taboolib.util.ArrayUtil;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.lite.Catchers;
import io.izzel.taboolib.util.lite.Signs;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.action.base.AbstractAction;
import me.arasple.mc.trmenu.utils.JavaScript;
import me.arasple.mc.trmenu.utils.TrUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * @author Arasple
 * @date 2020/1/15 15:37
 */
public class ActionCatcher extends AbstractAction {

    private static boolean isCancelWord(String word) {
        return TrMenu.getSettings().getList("OPTIONS.CATCHER-CANCEL-WORDS", Collections.singletonList("quit|exit|cancel|return|break")).stream().anyMatch(k -> word.split(" ")[0].matches("(?i)" + k));
    }

    private int type;
    private String require;
    private String beforeInputAction;
    private String inputValidAction;
    private String inputInvalidAction;
    private String cancelAction;

    @Override
    public String getName() {
        return "catch(er)?";
    }

    @Override
    public void onExecute(Player player) {
        if (type == 0) {
            runChatCatcher(player);
        } else {
            runSignCatcher(player);
        }
    }

    private void runChatCatcher(Player player) {
        player.closeInventory();
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> runActions(player, beforeInputAction), 3);
                return this;
            }

            @Override
            public boolean after(String input) {
                if (isCancelWord(input)) {
                    cancel();
                    return false;
                } else if (require != null && !(boolean) JavaScript.run(player, require.replace("$input", input))) {
                    runActions(player, inputInvalidAction, input);
                    return true;
                } else {
                    runActions(player, inputValidAction, input);
                    return false;
                }
            }

            @Override
            public void cancel() {
                TrUtils.getInst().runAction(player, cancelAction);
            }
        });
    }

    private void runSignCatcher(Player player) {
        Bukkit.getScheduler().runTaskLater(TrMenu.getPlugin(), () -> runActions(player, beforeInputAction), 3);
        Signs.fakeSign(player, lines -> {
            String input = ArrayUtil.arrayJoin(lines, 0);
            if (isCancelWord(input)) {
                runActions(player, cancelAction);
                return;
            }
            if (require != null && !(boolean) JavaScript.run(player, require.replace("$input", input))) {
                runActions(player, inputInvalidAction, input);
                runSignCatcher(player);
            } else {
                runActions(player, inputValidAction, input);
            }
        });
    }

    private void runActions(Player player, String actions) {
        runActions(player, actions, null);
    }

    private void runActions(Player player, String actions, String input) {
        actions = actions.replace(";", "_||_");
        if (Strings.nonEmpty(input)) {
            actions = actions.replace("$input", input);
        }
        TrUtils.getInst().runAction(player, actions);
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);

        if (getContent() == null) {
            return;
        }

        type = 0;
        require = "";
        beforeInputAction = "";
        inputValidAction = "";
        inputInvalidAction = "";
        cancelAction = "";

        for (Variables.Variable v : new Variables(getContent()).find().getVariableList()) {
            if (v.isVariable()) {
                String[] x = v.getText().split("=", 2);
                if (x.length >= 2) {
                    String type = x[0].toLowerCase();
                    String value = ArrayUtil.arrayJoin(x, 1);
                    switch (type) {
                        case "type":
                            this.type = "CHAT".equalsIgnoreCase(value) ? 0 : 1;
                            continue;
                        case "require":
                            this.require = value;
                            continue;
                        case "before":
                            this.beforeInputAction = value;
                            continue;
                        case "valid":
                            this.inputValidAction = value;
                            continue;
                        case "invalid":
                            this.inputInvalidAction = value;
                            continue;
                        case "cancel":
                            this.cancelAction = value;
                            continue;
                        default:
                    }
                }
            }
        }
    }


}
