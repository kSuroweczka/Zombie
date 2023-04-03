package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Zombie implements Sprite{
    BufferedImage tape;
    int x = 500;
    int y = 500;
    double scale = 1;

    int index = 0;  // numer wyświetlanego obrazka
    int HEIGHT = 312; // z rysunku;
    int WIDTH = 200; // z rysunku;

    Zombie(int x,int y, double scale, BufferedImage tape){
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.tape=tape;
    }

    Zombie(int x, int y, double scale){
        this.x = x;
        this.y = y;
        this.scale = scale;
        try {
            this.tape = ImageIO.read(getClass().getResource("/image/walkingdead.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Pobierz odpowiedni podobraz klatki (odpowiadającej wartości zmiennej index)
     * i wyświetl go w miejscu o współrzędnych (x,y)
     *
     * @param g
     * @param parent
     */

    public void draw(Graphics g, JPanel parent) {
        Image img = tape.getSubimage(index*200,0,200,312); // pobierz klatkę
        g.drawImage(img, x, y - (int) (HEIGHT * scale*0.5) / 2, (int) (WIDTH * scale*0.5), (int) (HEIGHT * scale*0.5), parent);
    }

    /**
     * Zmień stan - przejdź do kolejnej klatki
     */

    public void next() {
        x -= 5*scale;
        index = (index + 1) % 10;
    }
    public boolean isVisible(){
        if(x+(int)(WIDTH*scale*0.5)<0) return false;
        return true;
    }
    public boolean isHit(int _x,int _y){
        if(_x>x && _x<x+(int)(WIDTH*scale*0.5) && _y>y-(int)(HEIGHT*scale*0.5)/2 && _y<y+(int)(HEIGHT*scale*0.5)/2) return true;
        return false;
    }
    public boolean isCloser(Sprite other){
        if(other instanceof Zombie z) {
            if(z.scale>=scale) return true;
        }
        return false;
    }
}
