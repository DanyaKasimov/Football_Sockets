package game.entity;

import server.handlers.ClientHandler;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;

public class Ball extends Rectangle{

	public Random random;
	public int xVelocity;
	public int yVelocity;
	Image image;
	ClientHandler client;
	public Ball(int x, int y, int width, int height, ClientHandler client){
		super(x,y,width,height);
		random = new Random();
		this.client = client;
		URL img = Player.class.getClassLoader().getResource("images/ball.png");
		image = new ImageIcon(img.getPath()).getImage();
	}
	
	public void setXDirection(int randomXDirection) {
		xVelocity = randomXDirection;
	}
	public void setYDirection(int randomYDirection) {
		yVelocity = randomYDirection;
	}
	public void move() {
		x += xVelocity;
		y += yVelocity;
		client.sendBallPosition(x,y);
	}
	public void draw(Graphics g) {
		g.drawImage(image, x, y, width, height, null);
	}
}