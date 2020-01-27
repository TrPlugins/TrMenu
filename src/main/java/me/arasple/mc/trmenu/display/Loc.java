package me.arasple.mc.trmenu.display;

import java.util.HashMap;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/27 12:04
 */
public class Loc {

    private HashMap<Integer, List<Integer>> slots;

    public Loc(HashMap<Integer, List<Integer>> slots) {
        this.slots = slots;
    }


    public Loc() {
        this.slots = new HashMap<>();
    }

    public Loc(int size, List<List<Integer>> slots) {
        this.slots = new HashMap<>();
        for (int i = 0; i < size; i++) {
            this.slots.put(i, slots.get(0));
        }
    }

    public List<Integer> getSlots(int shape) {
        return slots.get(shape);
    }

    public HashMap<Integer, List<Integer>> getSlots() {
        return slots;
    }

    public void setSlots(HashMap<Integer, List<Integer>> slots) {
        this.slots = slots;
    }

}
