package com.company;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;

public class CrossHair implements MouseMotionListener, MouseListener {

    DrawPanel parent;
    List<CrossHairListener> listeners = new ArrayList<CrossHairListener>();

    CrossHair(DrawPanel parent) {
        this.parent = parent;
    }



    /* x, y to współrzedne celownika
       activated - flaga jest ustawiana po oddaniu strzału (naciśnięciu przyciku myszy)
    */
    int x;
    int y;
    boolean activated = false;

    void draw(Graphics g) {
        if (activated) g.setColor(Color.RED);
        else g.setColor(Color.WHITE);

        g.drawOval(x - 10, y - 10, 20, 20);
        g.drawLine(x, y + 20, x, y - 20);
        g.drawLine(x - 20, y, x + 20, y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        parent.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        activated = true;
        notifyListeners();

        Timer timer = new Timer("Timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activated = false;
                parent.repaint();
            }
        }, 300);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    void addCrossHairListener(CrossHairListener e) {
        listeners.add(e);
    }

    void notifyListeners() {
        for (var e : listeners) {
            e.onShotsFired(x, y);
        }
    }
}