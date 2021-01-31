import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author mayur.somani
 * @date 31-10-2020
 */
public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1502897126851292548L;

	static final int SCREEN_WIDTH = 900;
	static final int SCREEN_HEIGHT = 600;
	// size of objects (25 seems ideal)
	static final int UNIT_SIZE = 25;
	// number of objects that can fit in the screen
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	// score text size
	static final int SCORE_TEXT_SIZE = (SCREEN_WIDTH * SCREEN_HEIGHT) / ((SCREEN_WIDTH + SCREEN_HEIGHT) * 15);
	// gameOver text size
	static final int GAME_OVER_TEXT_SIZE = SCORE_TEXT_SIZE * 4;
	// higher the number, slower the game (75 seems ideal)
	static final int DELAY = 75;
	// all the 'X' coordinates for all the body parts & the head of the snake
	final int[] X = new int[GAME_UNITS];
	// all the 'Y' coordinates for all the body parts & the head of the snake
	final int[] Y = new int[GAME_UNITS];
	// initial amount of body parts of the snake (5 seems ideal)
	int bodyParts = 1;
	int applesEaten;
	// 'X' coordinate of the where the apple is located
	int appleX;
	// 'X' coordinate of the where the apple is located
	int appleY;
	// R = Right, L = Left, U = Up, D = Down; initial is set to R(Right)
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {

		if (running) {

			/**
			 * to draw check lines so that we can see better
			 */
			// vertical lines
//			for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
//				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//			}
//			// horizontal lines
//			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//			}

			// set color and shape of apple
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// draw the head and body of snake
			for (int i = 0; i < bodyParts; i++) {
				// body
				if (i != 0) {
					// g.setColor(new Color(45, 80, 0));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fill3DRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE, true);
				}
				// head
				else {
					g.setColor(Color.GREEN);
					g.fill3DRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE, true);
				}
			}

			// display score while playing
			g.setColor(Color.BLUE);
			g.setFont(new Font("Ink Free", Font.BOLD, SCORE_TEXT_SIZE));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten,
					((SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) - (int) (SCREEN_WIDTH / 20)),
					g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
	}

	// moving the snake
	public void move() {

		// iterate all the body parts of the snake
		for (int i = bodyParts; i > 0; i--) {
			X[i] = X[i - 1];
			Y[i] = Y[i - 1];
		}

		switch (direction) {
		case 'U':
			Y[0] = Y[0] - UNIT_SIZE;
			break;
		case 'D':
			Y[0] = Y[0] + UNIT_SIZE;
			break;
		case 'L':
			X[0] = X[0] - UNIT_SIZE;
			break;
		case 'R':
			X[0] = X[0] + UNIT_SIZE;
			break;
		default:
			break;
		}
	}

	public void checkApple() {

		if ((X[0] == appleX) && (Y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}

	}

	public void checkCollisions() {

		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((X[0] == X[i]) && (Y[0] == Y[i])) {
				running = false;
			}
		}

		// checks if head touches border
		if (X[0] < 0 || X[0] >= SCREEN_WIDTH || Y[0] < 0 || Y[0] >= SCREEN_HEIGHT) {
			running = false;
		}

		// stop timer
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {

		// display score in gameOver screen
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, SCORE_TEXT_SIZE));
		FontMetrics scoreMetrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten,
				((SCREEN_WIDTH - scoreMetrics.stringWidth("Score: " + applesEaten)) - (int) (SCREEN_WIDTH / 20)),
				g.getFont().getSize());

		// display gameOver text in gameOver screen
		g.setColor(Color.RED);
		g.setFont(new Font("Ink Free", Font.BOLD, GAME_OVER_TEXT_SIZE));
		FontMetrics gameOverTextMetrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - gameOverTextMetrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
			case KeyEvent.VK_NUMPAD4:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
			case KeyEvent.VK_NUMPAD6:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
			case KeyEvent.VK_NUMPAD8:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
			case KeyEvent.VK_NUMPAD2:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_ESCAPE:
				running = false;
				break;
			default:
				break;
			}
		}
	}
}
