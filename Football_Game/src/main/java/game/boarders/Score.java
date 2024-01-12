package game.boarders;

import server.handlers.ClientHandler;

import java.awt.*;

public class Score extends Rectangle{

	public static int GAME_WIDTH;
	public static int GAME_HEIGHT;
	public int player1;
	public int player2;
	ClientHandler client;

	public Score(int GAME_WIDTH, int GAME_HEIGHT, ClientHandler client){
		this.client = client;
		Score.GAME_WIDTH = GAME_WIDTH - 20;
		Score.GAME_HEIGHT = GAME_HEIGHT -200;
	}
	public void draw(Graphics g) {
		client.sendScore(player1, player2);

		g.setColor(Color.white);
		g.setFont(new Font("Consolas",Font.PLAIN,60));


		g.drawString((player1/10)+String.valueOf(player1%10), (GAME_WIDTH/2)-85, 50);
		g.drawString((player2/10)+String.valueOf(player2%10), (GAME_WIDTH/2)+20, 50);

	}
}