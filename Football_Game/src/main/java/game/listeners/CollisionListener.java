package game.listeners;

import game.constans.Constants;
import game.entity.Ball;
import game.entity.Barbell;
import game.entity.Player;

import java.awt.*;

public class CollisionListener {
    public static void collisionBallAndEntity(Ball ball, Rectangle entity){
        Rectangle ballRect = new Rectangle(ball.x + ball.xVelocity, ball.y + ball.yVelocity, Constants.BALL_DIAMETER, Constants.BALL_DIAMETER);
        if(ballRect.intersects(entity)) {
            int deltaY = ball.y - (entity.y + entity.height / 2);
            int deltaX = ball.x - (entity.x + entity.width / 2);

            ball.yVelocity = (int) (deltaY * 0.1);
            ball.xVelocity = (int) (deltaX * 0.1);

            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);

            if (entity.x > ball.x) {
                ball.x = entity.x - entity.width;
            }
            if (entity.x < ball.x) {
                ball.x = entity.x + entity.width;
            }

        }
    }
    public static void collisionPlayerAndEntity(Rectangle entity, Player player) {
        if (player.x + player.width > entity.x + entity.width) {
            if (entity.getClass() == Barbell.class) {
                if (player.intersects(entity)) {
                    player.x = entity.x + player.width / 2 - 10;
                }
            } else {
                if (player.intersects(entity)) {
                    player.x = entity.x + player.width - 10;
                }
            }
        } else {
            if (player.intersects(entity)) {
                player.x = entity.x - player.width;
            }
        }
        if (player.y + player.height > entity.y + entity.height) {
            if (player.intersects(entity)) {
                player.y = entity.y + player.height - 20;
            }
        } else {
            if (player.intersects(entity)) {
                player.y = entity.y - player.height;
            }
        }
    }
    public static void collisionBallWindow(Ball ball){
        if(ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= Constants.GAME_HEIGHT - Constants.BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.x <= 0) {
            ball.setXDirection(-ball.xVelocity);
        }
        if(ball.x >= Constants.GAME_WIDTH - Constants.BALL_DIAMETER) {
            ball.setXDirection(-ball.xVelocity);
        }
    }
    public static void collisionPlayerWindow(Player player){
        if(player.x <= 0)
            player.x = 0;
        if(player.x >= Constants.GAME_WIDTH - Constants.PLAYER_DIAMETER)
            player.x = Constants.GAME_WIDTH - Constants.PLAYER_DIAMETER;
        if(player.y <= 0)
            player.y = 0;
        if(player.y >= Constants.GAME_HEIGHT - Constants.PLAYER_DIAMETER)
            player.y = Constants.GAME_HEIGHT - Constants.PLAYER_DIAMETER;

    }

}
