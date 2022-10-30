package com.zetcode.sprite;

import javax.swing.ImageIcon;

public class Shot extends Sprite {

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
}
