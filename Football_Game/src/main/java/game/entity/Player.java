package game.entity;

import server.handlers.ClientHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class Player extends Rectangle{

	public int id;
	public int yVelocity;
	public int xVelocity;
	public int speed = 10;
	private Image image1;
	private Image image2;
	private ClientHandler client;
	
	public Player(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id, ClientHandler client){
		super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		this.id=id;
		this.client = client;
		URL img1 = Player.class.getClassLoader().getResource("images/player_1.png");
		URL img2 = Player.class.getClassLoader().getResource("images/player_2.png");
		image1 = new ImageIcon(img1.getPath()).getImage();
		image2 = new ImageIcon(img2.getPath()).getImage();
	}
	
	public void keyPressed(KeyEvent e) {
		switch (id) {
			case 1 -> {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					setYDirection(-speed);
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					setYDirection(speed);
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					setXDirection(-speed);
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					setXDirection(speed);
				}
			}
			case 2 -> {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					setYDirection(-speed);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					setYDirection(speed);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					setXDirection(-speed);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					setXDirection(speed);
				}
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (id) {
			case 1 -> {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_A) {
					setXDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					setXDirection(0);
				}
			}
			case 2 -> {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					setXDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					setXDirection(0);
				}
			}
		}
	}
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}
	public void setXDirection(int xDirection) {
		xVelocity = xDirection;
	}
	public void move() {
		y = y + yVelocity;
		x = x + xVelocity;
		client.sendPlayerPosition(id,x,y);
	}
	public void draw(Graphics g) {
		if(id==1)
			g.drawImage(image1, x, y, width, height, null);
		else
			g.drawImage(image2, x, y, width, height, null);
	}
}