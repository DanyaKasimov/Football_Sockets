package server.GUI;

import javax.swing.*;
import java.awt.*;

public class WaitingSecondPlayer extends JFrame {

    public  WaitingSecondPlayer() {

    }
    public void remove(){
        dispose();
    }
    public void init(){
        setTitle("Ожидание второго игрока");
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Ожидание второго игрока...", SwingConstants.CENTER);

        add(label, BorderLayout.CENTER);

        getContentPane().setBackground(Color.decode("#76ff70"));



        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}

