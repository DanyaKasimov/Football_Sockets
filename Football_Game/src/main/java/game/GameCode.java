package game;

import game.boarders.Score;
import game.constans.Constants;
import game.entity.*;
import game.listeners.CollisionListener;
import server.handlers.ClientHandler;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;

public class GameCode extends JPanel implements Runnable{

	private Thread gameThread;
	private Image image;
	private Graphics graphics;
	private Player player1;
	private Player player2;
	private Gates gates1;
	private Gates gates2;
	private Ball ball;
	private Score score;

	private Barbell barbell1;
	private Barbell barbell2;
	private Barbell barbell3;
	private Barbell barbell4;
	private Grid grid1;
	private Grid grid2;


	private Image backgroundImage;
	private ClientHandler client;

	public GameCode(ClientHandler client){
		this.client = client;
		newPlayer();
		newBarbell();
		newBall(client);
		newGates();
		score = new Score(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, client);
		this.setFocusable(true);
		this.addKeyListener(new KeyListener());
		this.setPreferredSize(Constants.SCREEN_SIZE);
		gameThread = new Thread(this);
		gameThread.start();

		URL img = Player.class.getClassLoader().getResource("images/field.png");
		ImageIcon backgroundImageIcon = new ImageIcon(img.getPath());
		backgroundImage = backgroundImageIcon.getImage().getScaledInstance(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, Image.SCALE_DEFAULT);

	}
	public void newBall(ClientHandler client) {
		ball = new Ball((Constants.GAME_WIDTH / 2) - (Constants.BALL_DIAMETER / 2) - 5,(Constants.GAME_HEIGHT / 2) - (Constants.BALL_DIAMETER / 2) - 8, Constants.BALL_DIAMETER, Constants.BALL_DIAMETER, client);
	}
	public void newPlayer() {
		player1 = new Player(2 * Constants.GRID_WIDTH,(Constants.GAME_HEIGHT / 2) - (Constants.PLAYER_DIAMETER / 2), Constants.PLAYER_DIAMETER, Constants.PLAYER_DIAMETER,1, client);
		player2 = new Player(Constants.GAME_WIDTH - Constants.PLAYER_DIAMETER - 2 * Constants.GRID_WIDTH,(Constants.GAME_HEIGHT / 2) - (Constants.PLAYER_DIAMETER / 2), Constants.PLAYER_DIAMETER, Constants.PLAYER_DIAMETER,2, client);
	}
	public void newGates() {
		grid1 = new Grid(0,(Constants.GAME_HEIGHT / 2) - (Constants.GRID_HEIGHT / 2), Constants.GRID_WIDTH, Constants.GRID_HEIGHT,1);
		grid2 = new Grid(Constants.GAME_WIDTH - Constants.GRID_WIDTH,(Constants.GAME_HEIGHT / 2) - (Constants.GRID_HEIGHT / 2), Constants.GRID_WIDTH, Constants.GATES_HEIGHT,2);
		gates1 = new Gates(0,(Constants.GAME_HEIGHT/2)-(Constants.GATES_HEIGHT/2),Constants.GATES_WIDTH,Constants.GATES_HEIGHT,1);
		gates2 = new Gates(Constants.GAME_WIDTH-Constants.GATES_WIDTH,(Constants.GAME_HEIGHT/2)-(Constants.GATES_HEIGHT/2),Constants.GATES_WIDTH,Constants.GATES_HEIGHT,2);
	}
	public void newBarbell() {
		barbell1 = new Barbell(0,(Constants.GAME_HEIGHT / 2) - (Constants.GATES_HEIGHT / 2 + Constants.BARBELL_HEIGHT), Constants.BARBELL_WIDTH, Constants.BARBELL_HEIGHT,1);
		barbell2 = new Barbell(0,(Constants.GAME_HEIGHT / 2) + (Constants.GATES_HEIGHT / 2), Constants.BARBELL_WIDTH, Constants.BARBELL_HEIGHT,1);
		barbell3 = new Barbell(Constants.GAME_WIDTH - Constants.BARBELL_WIDTH,(Constants.GAME_HEIGHT / 2) - (Constants.GATES_HEIGHT / 2 + Constants.BARBELL_HEIGHT),Constants.BARBELL_WIDTH,Constants.BARBELL_HEIGHT,1);
		barbell4 = new Barbell(Constants.GAME_WIDTH - Constants.BARBELL_WIDTH,(Constants.GAME_HEIGHT / 2) + (Constants.GATES_HEIGHT / 2), Constants.BARBELL_WIDTH, Constants.BARBELL_HEIGHT,1);

	}
	public void paint(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	public void draw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, this);
		grid1.draw(g);
		grid2.draw(g);
		barbell1.draw(g);
		barbell2.draw(g);
		barbell3.draw(g);
		barbell4.draw(g);
		gates1.draw(g);
		gates2.draw(g);
		player1.draw(g);
		player2.draw(g);
		ball.draw(g);
		score.draw(g);
		Toolkit.getDefaultToolkit().sync();

	}
	public void move() {
		player1.move();
		player2.move();
		ball.move();
	}
	public void checkCollision() {
		CollisionListener.collisionBallWindow(ball);

		CollisionListener.collisionBallAndEntity(ball, player1);
		CollisionListener.collisionBallAndEntity(ball, player2);

		CollisionListener.collisionBallAndEntity(ball, barbell1);
		CollisionListener.collisionBallAndEntity(ball, barbell2);
		CollisionListener.collisionBallAndEntity(ball, barbell3);
		CollisionListener.collisionBallAndEntity(ball, barbell4);

		CollisionListener.collisionPlayerAndEntity(grid1, player1);
		CollisionListener.collisionPlayerAndEntity(grid1, player2);
		CollisionListener.collisionPlayerAndEntity(grid2, player2);
		CollisionListener.collisionPlayerAndEntity(grid2, player1);

		CollisionListener.collisionPlayerAndEntity(barbell1, player1);
		CollisionListener.collisionPlayerAndEntity(barbell2, player1);
		CollisionListener.collisionPlayerAndEntity(barbell3, player1);
		CollisionListener.collisionPlayerAndEntity(barbell4, player1);

		CollisionListener.collisionPlayerAndEntity(barbell1, player2);
		CollisionListener.collisionPlayerAndEntity(barbell2, player2);
		CollisionListener.collisionPlayerAndEntity(barbell3, player2);
		CollisionListener.collisionPlayerAndEntity(barbell4, player2);

		CollisionListener.collisionPlayerWindow(player1);
		CollisionListener.collisionPlayerWindow(player2);

		if(ball.intersects(gates1)) {
			score.player2++;
			newPlayer();
			newBall(client);
		}
		if(ball.intersects(gates2)) {
			score.player1++;
			newPlayer();
			newBall(client);
			newGates();
		}
	}
	public void run() {
		long lastTime = System.nanoTime();
		double ticks = 60.0;
		double ns = 1000000000 / ticks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
	public class KeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			player1.keyPressed(e);
			player2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			player1.keyReleased(e);
			player2.keyReleased(e);
		}
	}
	public void setPlayer1(int x, int y){
		player1.x = x;
		player1.y = y;
	}
	public void setPlayer2(int x, int y){
		player2.x = x;
		player2.y = y;
	}
	public void setBall(int x, int y){
		ball.x = x;
		ball.y = y;
	}
	public void setScore(int p1, int p2){
		score.player1 = p1;
		score.player2 = p2;
	}
}
