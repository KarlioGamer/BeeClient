package com.beeclient.util;

import org.lwjgl.input.Mouse;

import java.util.LinkedList;

public class ClickCounter {

    public static final ClickCounter INSTANCE = new ClickCounter();

    private final LinkedList<Long> lmb = new LinkedList<>();
    private final LinkedList<Long> rmb = new LinkedList<>();
    private boolean lastLmbDown = false;
    private boolean lastRmbDown = false;

    private ClickCounter() {
    }

    public void update() {
        long now = System.currentTimeMillis();
        boolean l = Mouse.isButtonDown(0);
        boolean r = Mouse.isButtonDown(1);
        if (l && !lastLmbDown) lmb.addLast(now);
        if (r && !lastRmbDown) rmb.addLast(now);
        lastLmbDown = l;
        lastRmbDown = r;
        prune(now);
    }

    public void click(int button) {
        long now = System.currentTimeMillis();
        if (button == 0) {
            lmb.addLast(now);
        } else if (button == 1) {
            rmb.addLast(now);
        }
        prune(now);
    }

    public int getLmbCps() {
        prune(System.currentTimeMillis());
        return lmb.size();
    }

    public int getRmbCps() {
        prune(System.currentTimeMillis());
        return rmb.size();
    }

    private void prune(long now) {
        while (!lmb.isEmpty() && now - lmb.peekFirst() > 1000) lmb.removeFirst();
        while (!rmb.isEmpty() && now - rmb.peekFirst() > 1000) rmb.removeFirst();
        while (lmb.size() > 40) lmb.removeFirst();
        while (rmb.size() > 40) rmb.removeFirst();
    }
}