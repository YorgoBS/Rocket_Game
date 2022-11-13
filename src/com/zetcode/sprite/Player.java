package com.zetcode.sprite;

import com.zetcode.Board;
import com.zetcode.Commons;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

/**
 * Rocket Handler
 * 
 * @author Toufic Lattouf and Yorgo Bou Samra
 *
 */
public class Player extends Sprite {

    private int width;
    /**
     * Default Constructor
     */
    public Player() {

        initPlayer();
    }
    /**
     * Initializer of Player
     */
    private void initPlayer() {

        var playerImg = "src/images/player.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = Commons.BOARD_WIDTH/2;
        setX(START_X);

        int START_Y = Commons.BOARD_HEIGHT-70;
        setY(START_Y);
    }
    /**
     * Movement action
     */
    public void act() {

        x += dx;

        if (x <= 2) {

            x = 2;
        }

        if (x >= Commons.BOARD_WIDTH - 2 * width) {

            x = Commons.BOARD_WIDTH - 2 * width;
        }
    }
    /**
     * Key press register
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if(Board.getUsername().equals("automated")) {
            return;
        }
        if (key == KeyEvent.VK_LEFT) {

            dx = -3;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 3;
        }
    }
    /**
     * Key Release Register
     * @param e key event
     */
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}
