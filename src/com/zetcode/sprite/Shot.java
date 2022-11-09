package com.zetcode.sprite;

import javax.swing.ImageIcon;
/**
 * Bullet shot handler
 * @author yorgo
 *
 */
public class Shot extends Sprite {

	private int speed;
	/**
	 * default constructor
	 */
    public Shot() {
    }
    /**
     * parametrized  constructor 
     * @param x X axis 
     * @param y Y axis 
     */
    public Shot(int x, int y) {
    	speed = 5;
        initShot(x, y);
    }
    /**
     * Shot initializer 
     * @param x X axis
     * @param y Y axis
     */
    private void initShot(int x, int y) {

        var shotImg = "src/images/shot.png";
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
    /**
     * Shot speed getter
     * @return speed value
     */
	public int getSpeed() {
		return speed;
	}
}
