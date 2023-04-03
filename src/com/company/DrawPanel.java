package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;

class DrawPanel extends JPanel implements CrossHairListener{
    ZombieFactory factory;
    BufferedImage background;
//    Zombie zombie = new Zombie(800, 500, 1);
    List<Sprite> sprites = new ArrayList<>();
    CrossHair crossHair;
    Semaphore mutex = new Semaphore(1);
    boolean zakoncz=false;
//    DrawPanel(URL backgroundImagageURL) {
//        try {
//            background = ImageIO.read(backgroundImagageURL);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//        (new AnimationThread()).start();
//    }
    DrawPanel(URL backgroundImagageURL, ZombieFactory factory) throws InterruptedException {
        try {
            background = ImageIO.read(backgroundImagageURL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.factory = factory;
        crossHair=new CrossHair(this);
        addMouseMotionListener(crossHair);
        addMouseListener(crossHair);
        crossHair.addCrossHairListener(this);
        (new AnimationThread()).start();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
//        zombie.draw(g,this);
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Sprite s: sprites){
            s.draw(g,this);
        }
        mutex.release();
        crossHair.draw(g);

    }

    @Override
    public void onShotsFired(int x, int y) {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=sprites.size()-1;i>=0;i--){
            if(sprites.get(i).isHit(x,y)){
                sprites.remove(i);
                break;
            }
        }
        mutex.release();
    }

    class AnimationThread extends Thread {
        public void run() {
            for (int i = 1; ; i++) {
                try {
                    mutex.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(Sprite s: sprites){
                    s.next();
                }

                if(i%30==0) {
                    sprites.add(factory.newSprite(getWidth(),(int)(0.7*getHeight())));
                    sprites.sort((o1, o2) -> {
                        if(o1.isCloser(o2)) return -1;
                        return 1;
                    });
                }

                for(int k=0; k<sprites.size();k++){
                    if(sprites.get(k) instanceof Zombie z) {
                        if(!sprites.get(k).isVisible()){
                            sprites.remove(k);

                        }
                    }
                }
                mutex.release();
                repaint();
                try {
                    sleep(1000 / 60);  // 30 klatek na sekundę
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(zakoncz==true){
                    return;
                }
            }

        }
    }
}