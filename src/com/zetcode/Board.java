
package com.zetcode;

import com.zetcode.sprite.Alien;

import com.zetcode.sprite.Player;
import com.zetcode.sprite.Shot;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

/**
 * This serves as the JFrame for the Clay Shooting Game
 * 
 * @author Toufic Lattouf and Yorgo Bou Samra
 * @version 2.0
 * 
 */
@SuppressWarnings("serial")
public class Board extends JPanel {

	private JButton newGameBtn;
	private JButton quitGameBtn;
	private JButton logInLogOutBtn;
	private JLabel pause;
	private JLabel scoreDisplay;
	private JSlider alienSlider;
	private JSlider shotSlider;
	private JSlider numberSlider;
	private JLabel shotSpeedLabel;
	private JLabel fireFreqLabel;
	private JLabel targetNumberLabel;
	private JPasswordField passwordField;

	private Dimension d;
	private List<Alien> aliens;
	private Player player;
	private Shot shot;
	private ArrayList<Shot> shots;

	private static String uname = "";
	private String pass = "";
	private double highScore = 0;
	private int deaths = 0;
	private int score = 0;
	private int fireFrequency = 1;
	private int fireSpeed = 1;
	private int maxBullets = 0;
	private int shotsFired = 0;
	private int numberOfTargets = 0;

	private boolean inGame = false;
	private boolean isLoggedIn = false;
	private String explImg = "src/images/explosion.png";
	private String message = "Welcome to Tyro";

	private Timer timer;
	long start;
	long end;

	private String username;
	private JTextField usernameField;

	class MyChangeListenerFiring implements ChangeListener {
		/**
		 * Default Constructor
		 */
		MyChangeListenerFiring() {
		}

		public synchronized void stateChanged(ChangeEvent e) {
			fireFrequency = alienSlider.getValue();
		}
	}

	class MyChangeListenerShot implements ChangeListener {
		/**
		 * Default Constructor
		 */
		MyChangeListenerShot() {
		}

		public synchronized void stateChanged(ChangeEvent e) {
			fireSpeed = shotSlider.getValue();
		}
	}

	class MyChangeListenerNumber implements ChangeListener {
		/**
		 * Default Constructor
		 */
		MyChangeListenerNumber() {
		}

		public synchronized void stateChanged(ChangeEvent e) {
			numberOfTargets = numberSlider.getValue();
		}
	}

	/**
	 * Initializer Function
	 */
	public Board() {

		initBoard();
		gameInit();
		doGameCycle();

	}

	/**
	 * Initializes the game board
	 */
	private void initBoard() {

		addKeyListener(new TAdapter());
		setFocusable(true);
		d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
		setBackground(Color.black);
		setLayout(null);

		newGameBtn = new JButton("New Game");
		newGameBtn.addActionListener(e -> launchGame());
		newGameBtn.setBounds(124, 94, 104, 21);
		add(newGameBtn);

		pause = new JLabel("Esc to Pause");
		pause.setForeground(Color.GREEN);
		pause.setBounds(10, 10, 79, 13);
		add(pause);

		scoreDisplay = new JLabel("Score: ");
		scoreDisplay.setForeground(Color.GREEN);
		scoreDisplay.setBounds(240, 10, 100, 13);
		add(scoreDisplay);

		quitGameBtn = new JButton("Quit");
		quitGameBtn.setBounds(124, 125, 104, 21);
		quitGameBtn.addActionListener(e -> quitGame());
		add(quitGameBtn);

		logInLogOutBtn = new JButton("Logout");
		logInLogOutBtn.setBounds(124, 156, 104, 21);
		logInLogOutBtn.addActionListener(e -> logInLogOut());
		add(logInLogOutBtn);

		// speed slider
		fireFreqLabel = new JLabel("fire frequency: ");
		fireFreqLabel.setForeground(Color.GREEN);
		fireFreqLabel.setBounds(21, 200, 93, 21);
		add(fireFreqLabel);
		alienSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 1);
		alienSlider.setBorder(null);
		alienSlider.setForeground(Color.GREEN);
		alienSlider.setBackground(Color.BLACK);
		alienSlider.setSnapToTicks(true);
		alienSlider.setMinorTickSpacing(1);
		alienSlider.setPaintTicks(true);
		alienSlider.setPaintLabels(true);
		alienSlider.setMajorTickSpacing(10);
		alienSlider.setBounds(10, 216, 104, 44);
		add(alienSlider);
		fireFrequency = 1;
		MyChangeListenerFiring lst = new MyChangeListenerFiring();
		alienSlider.addChangeListener(lst);
		// gameInit();
		shotSpeedLabel = new JLabel("target speed: ");
		shotSpeedLabel.setForeground(Color.GREEN);
		shotSpeedLabel.setBounds(251, 200, 93, 21);
		add(shotSpeedLabel);
		shotSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 5, 1);
		shotSlider.setBorder(null);
		shotSlider.setForeground(Color.GREEN);
		shotSlider.setBackground(Color.BLACK);
		shotSlider.setSnapToTicks(true);
		shotSlider.setMinorTickSpacing(1);
		shotSlider.setPaintTicks(true);
		shotSlider.setPaintLabels(true);
		shotSlider.setMajorTickSpacing(5);
		shotSlider.setBounds(240, 216, 104, 44);
		add(shotSlider);
		fireSpeed = 1;
		MyChangeListenerShot lst2 = new MyChangeListenerShot();
		shotSlider.addChangeListener(lst2);

		targetNumberLabel = new JLabel("target number: ");
		targetNumberLabel.setForeground(Color.GREEN);
		targetNumberLabel.setBounds(135, 200, 93, 21);
		add(targetNumberLabel);
		numberSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 1);
		numberSlider.setBorder(null);
		numberSlider.setForeground(Color.GREEN);
		numberSlider.setBackground(Color.BLACK);
		numberSlider.setSnapToTicks(true);
		numberSlider.setMinorTickSpacing(1);
		numberSlider.setPaintTicks(true);
		numberSlider.setPaintLabels(true);
		numberSlider.setMajorTickSpacing(5);
		numberSlider.setBounds(124, 219, 104, 39);
		add(numberSlider);
		numberOfTargets = 1;
		MyChangeListenerNumber lst3 = new MyChangeListenerNumber();
		numberSlider.addChangeListener(lst3);

		passwordField = new JPasswordField();
		passwordField.setBounds(124, 126, 113, 19);
		add(passwordField);

		usernameField = new JTextField();
		usernameField.setBounds(124, 95, 113, 19);
		add(usernameField);
		usernameField.setColumns(10);
	}

	/**
	 * Connects to the MySQL Database
	 * 
	 * @return Connection entity
	 */
	public static Connection getConnection() {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:users.db");
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS USERS (id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT NOT NULL,password TEXT NOT NULL,highscore DOUBLE NOT NULL);";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			System.out.println("HERE");
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}

	/**
	 * The User Log in Functionality with MySQL - Allowing Log In and Creating
	 * Accounts
	 */
	private void logInLogOut() {

		Statement stmt = null;
		PreparedStatement pstmt = null;
		uname = usernameField.getText();
		pass = String.valueOf(passwordField.getText());
		String query = "SELECT * FROM `USERS` WHERE `username` =? AND `password` =?";

		if (!isLoggedIn) {

			try {
				Connection c = getConnection();
				pstmt = c.prepareStatement(query);
				pstmt.setString(1, uname);
				pstmt.setString(2, pass);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					isLoggedIn = true;
					message = "Welcome to Tyro " + uname;
					highScore = rs.getDouble("highscore");
				}

				else {
					query = "INSERT INTO users VALUES(?,?,0)";
					int result = JOptionPane.showConfirmDialog(null,
							"No registered username or password. Create a new account?", "Uh Oh!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						try {
							String sql = "INSERT INTO USERS (id,username,password,highscore) " + "VALUES (?,?,?,?);";
							pstmt = c.prepareStatement(sql);
							pstmt.setString(2, uname);
							pstmt.setString(3, pass);
							pstmt.setDouble(4, highScore);
							pstmt.executeUpdate();
							isLoggedIn = true;
							message = "Welcome to Tyro " + uname;

						} catch (SQLException ex) {
							System.out.println(ex);
						}

					}

				}
				c.close();
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		} else {
			isLoggedIn = false;

		}
		doGameCycle();

	}

	/**
	 * Toggling Visibilities for the Log-In Screen
	 */
	private void logInScreen() {
		newGameBtn.setVisible(false);
		quitGameBtn.setVisible(false);
		alienSlider.setVisible(false);
		shotSlider.setVisible(false);
		numberSlider.setVisible(false);
		shotSpeedLabel.setVisible(false);
		fireFreqLabel.setVisible(false);
		targetNumberLabel.setVisible(false);
		logInLogOutBtn.setVisible(true);
		logInLogOutBtn.setText("Login");
		pause.setVisible(false);
		scoreDisplay.setVisible(false);
		passwordField.setVisible(true);
		usernameField.setVisible(true);
	}

	/**
	 * Quit Game Functionality
	 */
	private void quitGame() {

		if (inGame) {
			resetGame();
		} else {
			System.exit(1);
		}

	}

	private void settings() {
		if (timer.isRunning()) {
			timer.stop();
			quitGameBtn.setVisible(true);
		} else {
			timer.start();
			quitGameBtn.setVisible(false);
		}
	}

	/**
	 * New Game Initializer
	 */
	private void launchGame() {
		if (!inGame) {
			inGame = true;
			// newGameBtn.setVisible(false);
			timer = new Timer(Commons.DELAY, new GameCycle());
			timer.start();
			gameInit();
			pause.setVisible(true);
			scoreDisplay.setVisible(true);
			deaths = 0;
			score = 0;
			shotsFired = 0;
			start = System.currentTimeMillis() - 5000 / fireFrequency;
			System.out.println(start);

		}
	}

	/**
	 * Toggle visibilities for in-game screen
	 */
	private void gameScreen() {
		newGameBtn.setVisible(false);
		quitGameBtn.setVisible(false);
		alienSlider.setVisible(false);
		shotSlider.setVisible(false);
		numberSlider.setVisible(false);
		shotSpeedLabel.setVisible(false);
		fireFreqLabel.setVisible(false);
		targetNumberLabel.setVisible(false);
		logInLogOutBtn.setVisible(false);

	}

	private void gameInit() {

		gameScreen();

		aliens = new ArrayList<>();

		username = null;
		player = new Player();
		shot = new Shot();
		shots = new ArrayList<Shot>();
	}

	private void drawAliens(Graphics g) {

		for (Alien alien : aliens) {

			if (alien.isVisible()) {

				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}

			if (alien.isDying()) {

				alien.die();
			}
		}
	}

	private void drawPlayer(Graphics g) {

		if (player.isVisible()) {

			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}

		if (player.isDying()) {

			player.die();
			inGame = false;
		}
	}

	private void drawShot(Graphics g) {
		for (Shot shot : shots) {
			if (shot.isVisible()) {

				g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
			}
		}
	}
	
	private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.green);

		if (inGame) {

			g.drawLine(0, Commons.GROUND, Commons.BOARD_WIDTH, Commons.GROUND);

			drawAliens(g);
			drawPlayer(g);
			drawShot(g);
            drawBombing(g);

		} else {

			if (timer != null && timer.isRunning()) {
				timer.stop();
			}

			gameOver(g);

		}

		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Toggle visibilities for Title Main Screen
	 */
	private void titleScreen() {
		newGameBtn.setVisible(true);
		quitGameBtn.setVisible(true);
		logInLogOutBtn.setVisible(true);
		logInLogOutBtn.setText("Logout");
		alienSlider.setVisible(true);
		shotSlider.setVisible(true);
		numberSlider.setVisible(true);
		shotSpeedLabel.setVisible(true);
		fireFreqLabel.setVisible(true);
		targetNumberLabel.setVisible(true);
		pause.setVisible(false);
		scoreDisplay.setVisible(true);
		passwordField.setVisible(false);
		usernameField.setVisible(false);
		displayHighScore();

	}

	/**
	 * Allows for setting, updating, and displaying the current user's high score
	 */
	private void displayHighScore() {
		Connection c = null;
		// add logic to display high score
		String query = "SELECT * FROM `users` WHERE `username` =? AND `password` =?";
		PreparedStatement ps;
		ResultSet rs;
		try {
			c = getConnection();
			ps = c.prepareStatement(query);

			ps.setString(1, uname);
			ps.setString(2, pass);

			rs = ps.executeQuery();
			while (rs.next()) {
				highScore = rs.getDouble("highscore");

			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scoreDisplay.setText("Highscore: " + highScore);
	}

	/**
	 * Game Over Screen
	 * 
	 * @param g Graphic
	 */
	private void gameOver(Graphics g) {
		if (!isLoggedIn) {
			// add input logic
			message = "Please Log In";
			logInScreen();
		} else {
			titleScreen();

		}

		g.setColor(Color.black);
		g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

		var small = new Font("Helvetica", Font.BOLD, 14);
		var fontMetrics = this.getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
				Commons.BOARD_WIDTH / 2 - 120);

	}

	/**
	 * Ongoing updater for in-game scores
	 */
	private void update() {
		scoreDisplay.setText("Score: " + score + "/" + shotsFired);
		if (deaths == numberOfTargets) {

			resetGame();
			double accuracy = (double) score / shotsFired;
			Double truncatedAccuracy = BigDecimal.valueOf(accuracy).setScale(3, RoundingMode.HALF_UP).doubleValue()
					* 100;
			System.out.println(score);
			message = "Your accuracy is: " + truncatedAccuracy + "%";
			if (truncatedAccuracy > highScore) {
				try {
					PreparedStatement ps;
					String query = "update users set highscore=? where username=?";

					ps = getConnection().prepareStatement(query);
					ps.setDouble(1, truncatedAccuracy);
					ps.setString(2, uname);
					ps.executeUpdate();
				} catch (SQLException ex) {
					System.out.println(ex);
				}
			}

			displayHighScore();

		}

		// player
		player.act();

		// shot
		for (int i = 0; i < shots.size(); i++) {
			shot = shots.get(i);
			if (shot.isVisible()) {

				int shotX = shot.getX();
				int shotY = shot.getY();

				for (Alien alien : aliens) {

					int alienX = alien.getX();
					int alienY = alien.getY();

					if (alien.isVisible() && shot.isVisible()) {
						if (shotX >= (alienX) && shotX <= (alienX + Commons.ALIEN_WIDTH) && shotY >= (alienY)
								&& shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

							File file = new File(explImg);

							var ii = new ImageIcon(explImg);
							alien.setImage(ii.getImage());
							alien.setDying(true);
							// remove alien from aliens
							deaths++;
							score++;
							maxBullets -= 2;
							shot.die();
						}
					}
					
					Alien.Bomb bomb = alien.getBomb();
		            int bombX = bomb.getX();
		            int bombY = bomb.getY();
		            
		            
		
		            if (shot.isVisible() && !bomb.isDestroyed()) {
		
		                if (bombX >= (shotX)
		                        && bombX <= (shotX + 1)
		                        && bombY >= (shotY)
		                        && bombY <= (shotY + 4)) {
		
		                    var ii = new ImageIcon(explImg);
		                    shot.setImage(ii.getImage());
		                    shot.die();
		                    bomb.setDestroyed(true);
		                }
		            }
					
				}

				int y = shot.getY();
				y -= shot.getSpeed();

				if (y < 0) {
					shot.die();

				} else {
					if (uname.equals("automated")) {
						shot.move();
					} else {
						shot.setY(y);
					}
				}
			} else {
				shots.remove(shot);

			}
		}
		// aliens
		end = System.currentTimeMillis();
		if (end - start > 5000.0 / fireFrequency) {
			if (aliens.size() < numberOfTargets) {
				Alien alien = new Alien(Commons.ALIEN_INIT_X, Commons.ALIEN_INIT_Y, fireSpeed);
				aliens.add(alien);
				if (uname.equals("automated")) {
					int x = player.getX();
					int y = player.getY();
					Shot shot = new Shot(x, y);
					shot.targetAlien(alien);
					shots.add(shot);
					shotsFired++;
				}
				if (inGame) {
					maxBullets += 2;
				}
				start = System.currentTimeMillis();
			}
		}
		deaths = 0;
		for (Alien alien : aliens) {

			if (!alien.isVisible()) {
				deaths++;
			}

			int x = alien.getX();
			// alien.act();
			if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT + Commons.ALIEN_WIDTH && alien.angle > 0.0) {

				//alien.flip();
				alien.setDying(true);
			

			}

			if (x <= Commons.BORDER_LEFT - Commons.ALIEN_WIDTH && alien.angle < 0.0) {

				// direction = 1;
				//alien.flip();
				alien.setDying(true);

			}
		}

		Iterator<Alien> it = aliens.iterator();

		while (it.hasNext()) {

			Alien alien = it.next();

			if (alien.isVisible()) {

				int y = alien.getY();

				if (y <= 0) {

					alien.setDying(true);
					maxBullets -= 2;

				}

				alien.act();
			}
		}
		
		// bombs
        var generator = new Random();

        for (Alien alien : aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == Commons.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }


            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 2);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
		

	}

	/**
	 * Game resetter
	 */
	private void resetGame() {
		
		deaths = 0;
		maxBullets = 0;
		inGame = false;
		if (timer.isRunning()) {
			timer.stop();
		}
		doGameCycle();
		
	}

	private void doGameCycle() {

		update();
		repaint();
	}

	private class GameCycle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			doGameCycle();
		}
	}

	/**
	 * Class to take care of keyboard inputs
	 * 
	 *
	 */
	private class TAdapter extends KeyAdapter {
		/**
		 * key release
		 */
		@Override
		public void keyReleased(KeyEvent e) {

			player.keyReleased(e);
		}

		/**
		 * key press register
		 */
		@Override
		public void keyPressed(KeyEvent e) {

			player.keyPressed(e);

			int x = player.getX();
			int y = player.getY();

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_SPACE) {

				// System.out.print(inGame);
				if (inGame) {
					if (shots.size() < maxBullets) {
						if (!uname.equals("automated")) {
							shots.add(new Shot(x, y));
							shotsFired++;
						}
					}

//                    if (!shot.isVisible()) {
//                    	
//                        shot = new Shot(x, y);
//                    }
				}
			}
			if (key == KeyEvent.VK_ESCAPE) {
				if (inGame) {
					settings();
				}
			}
		}
	}

	public static String getUsername() {
		return uname;
	}
}
