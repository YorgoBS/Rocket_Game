package com.zetcode.sprite;

import javax.swing.ImageIcon;

import com.zetcode.Commons;

public class Shot extends Sprite {

	Alien targetedAlien;
	private int speed;
    public Shot() {
    }

    public Shot(int x, int y) {
    	speed = 5;
        initShot(x, y);
    }

    private void initShot(int x, int y) {

        var shotImg = "src/images/shot.png";
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }

	public int getSpeed() {
		return speed;
	}
	
	public void targetAlien(Alien alien) {
		targetedAlien = alien;
	}
	
	public void move() {
		
		this.x += targetedAlien.speed*Math.sin(Math.toRadians(targetedAlien.angle));
		this.y -= (speed + (((double)5*targetedAlien.speed)/speed))*Math.cos(Math.toRadians(targetedAlien.angle));

	}
}
