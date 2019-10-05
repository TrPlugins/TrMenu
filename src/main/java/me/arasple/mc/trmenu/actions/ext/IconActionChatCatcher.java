package me.arasple.mc.trmenu.actions.ext;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Variables;
import io.izzel.taboolib.util.lite.Catchers;
import me.arasple.mc.trmenu.TrMenu;
import me.arasple.mc.trmenu.actions.ActionRunner;
import me.arasple.mc.trmenu.actions.ActionType;
import me.arasple.mc.trmenu.actions.BaseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2019/10/5 21:31
 */
public class IconActionChatCatcher extends BaseAction {

    private List<BaseAction> before, execute, after;

    public IconActionChatCatcher(String command) {
        super(command);

        for (Variables.Variable variable : new Variables(command).find().getVariableList()) {
            if (variable.isVariable()) {
                String[] args = variable.getText().split("=", 2);
                if (args.length == 2) {
                    String type = args[0].toUpperCase();
                    List<String> values = Lists.newArrayList();
                    values.addAll(Arrays.asList(args[0].split("\\|")));
                    switch (type) {
                        case "BEFORE":
                            this.before = ActionType.readAction(values);
                            continue;
                        case "EXECUTE":
                            this.execute = ActionType.readAction(values);
                            continue;
                        case "AFTER":
                            this.after = ActionType.readAction(values);
                            continue;
                        default:
                    }
                }
            }
        }
    }

    // CATCHER: BEFORE=action1|action2---EXECUTE=action1|action2---AFTER=action1|action2
    // catch: <before=动作1|动作2><execute=动作组><after=动作组>

    @Override
    public void onExecute(Player player, InventoryEvent e, String... args) {

        if ((before == null || before.isEmpty()) && (execute == null || execute.isEmpty()) && (after == null && after.isEmpty())) {
            TrMenu.getTLogger().error("§c聊天捕获器配置异常, 请检查 §8" + getCommand());
            return;
        }

        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                ActionRunner.runActions(before, player, e instanceof InventoryClickEvent ? (InventoryClickEvent) e : null);
                return this;
            }

            @Override
            public boolean after(String type) {
                ActionRunner.runActions(execute, player, e instanceof InventoryClickEvent ? (InventoryClickEvent) e : null, new HashMap<String, String>() {{
                    put("{INPUT}", type);
                }});
                return false;
            }

            @Override
            public void cancel() {
                ActionRunner.runActions(after, player, e instanceof InventoryClickEvent ? (InventoryClickEvent) e : null);
            }
        });
    }

}
