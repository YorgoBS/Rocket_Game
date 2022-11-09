package com.zetcode.sprite;

import java.util.Random;

import javax.swing.ImageIcon;
/**
 * Handling the Clay behavior and attributes with help from github repo found online from part 1 
 * 
 * @author Toufic Lattouf and Yorgo Bou Samra
 * 
 * 
 *
 */
public class Alien extends Sprite {

    private Bomb bomb;
    public double angle = 1;
    public int speed = 3;
    /**
     * Param Constructor
     * @param x X axis 
     * @param y Y axis
     * @param speed Speed 
     */
    public Alien(int x, int y, int speed) {
    	
        initAlien(x, y, speed);
    }
    /**
     * Initializer
     * @param x X axis 
     * @param y Y axis 
     * @param speed Speed
     */
    private void initAlien(int x, int y, int speed) {

        this.x = x;
        this.y = y;
        this.speed = speed+1;
        generateRandomAngle();
        
        bomb = new Bomb(x, y);

        var alienImg = "src/images/alien.png";
        var ii = new ImageIcon(alienImg);

        setImage(ii.getImage());
    }
    /**
     * Generating angle to shoot clay
     */
    public void generateRandomAngle() {
    	Random random = new Random();
		angle = (random.nextInt(34)-17)*5;
    }
    /**
     * Business logic for clay toss
     */
    public void act() {

        this.x += speed*Math.sin(Math.toRadians(angle));
        this.y -= speed*Math.cos(Math.toRadians(angle));
    }
    
    public void flip() {
    	this.angle *= -1;
    }

    public Bomb getBomb() {

        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            var bombImg = "src/images/bomb.png";
            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
