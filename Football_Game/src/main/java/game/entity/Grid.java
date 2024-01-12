package game.entity;

import java.awt.*;

public class Grid extends Rectangle {

    private int id;
    private Color transparentColor;


    public Grid(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id){
        super(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
        this.id=id;
        this.transparentColor = new Color(255, 255, 255, 50);

    }

    public void draw(Graphics g) {
        g.setColor(transparentColor);
        g.fillRect(x, y, width, height);
    }
}