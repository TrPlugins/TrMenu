package me.arasple.mc.trmenu.menu.objs;

import com.google.common.collect.Lists;
import me.arasple.mc.trmenu.menu.Menu;

import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/14 11:05
 */
public class LoadedMenu {

    private LoadedState state;
    private Menu menu;
    private List<String> errors;

    public LoadedMenu() {
        this.errors = Lists.newArrayList();
    }

    public LoadedMenu(LoadedState state, Menu menu, List<String> errors) {
        this.state = state;
        this.menu = menu;
        this.errors = errors;
    }

    public LoadedState getState() {
        return state;
    }

    public void setState(LoadedState state) {
        this.state = state;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public enum LoadedState {

        FAILED,
        SUCCESS

    }

}
