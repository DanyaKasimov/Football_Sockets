package server.GUI;

import game.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MainMenu extends JFrame {
    private JButton findOtherPlayersButton;
    private JButton existGameButton;
    private int status = 0;
    private boolean isSubmitted = false;

    public MainMenu(String playerName) {
        setTitle("Главное меню");
        setLayout(new FlowLayout());

        URL img = Player.class.getClassLoader().getResource("images/ball.png");
        Image image = new ImageIcon(img.getPath()).getImage();
        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        add(imageLabel);

        findOtherPlayersButton = new JButton("Найти игрока");
        existGameButton = new JButton( "Выйти из игры");
        findOtherPlayersButton.setPreferredSize(new Dimension(250, 60));
        existGameButton.setPreferredSize(new Dimension(250, 60));

        add(new JLabel("Игрок: " + playerName));
        add(findOtherPlayersButton);
        add(existGameButton);


        findOtherPlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = 1;
                isSubmitted = true;
                synchronized (MainMenu.this) {
                    MainMenu.this.notifyAll();
                }
                dispose();
            }
        });
        existGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = 2;
                isSubmitted = true;
                synchronized (MainMenu.this) {
                    MainMenu.this.notifyAll();
                }
                dispose();
            }
        });


        getContentPane().setBackground(Color.decode("#76ff70"));

        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public synchronized int getStatusMenu() {
        while (!isSubmitted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
    public synchronized void remove(){
        dispose();
    }
}

