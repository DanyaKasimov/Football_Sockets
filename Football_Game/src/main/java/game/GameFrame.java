package game;

import server.handlers.ClientHandler;

import javax.swing.*;
public class GameFrame extends JFrame{
	GameCode game;
	public GameFrame(ClientHandler client){
		game = new GameCode(client);
		add(game);
		setTitle("Football");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
	public void setPlayer1(int x, int y){
		game.setPlayer1(x, y);
	}
	public void setPlayer2(int x, int y){
		game.setPlayer2(x, y);
	}
	public void setBall(int x, int y){
		game.setBall(x, y);
	}
	public void setScore(int p1, int p2){
		game.setScore(p1,p2);
	}
}