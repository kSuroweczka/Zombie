package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public enum ZombieFactory implements SpriteFactory{
    INSTANCE;
    BufferedImage tape=null;

    ZombieFactory(){
        try {
            tape = ImageIO.read(getClass().getResource("/image/walkingdead.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Sprite newSprite(int x, int y) {
        Random r = new Random();
        double scale = r.nextDouble(1.8) + 0.2;// wylosuj liczbę z zakresu 0.2 do 2.0
        Zombie z = new Zombie(x, y, scale, tape);
        return z;
    }
}
