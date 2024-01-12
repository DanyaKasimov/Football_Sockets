package server.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameInputError extends JFrame {
    private JButton existButton;
    private boolean isSubmitted = false;

    public PlayerNameInputError() {
        setTitle("Ошибка");
        setLayout(new FlowLayout());

        existButton = new JButton("Выйти");
        JLabel label = new JLabel("Пользователь с таким именем уже существует");
        label.setForeground(Color.WHITE);
        getContentPane().setBackground(Color.RED);

        add(label);
        add(existButton);

        existButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSubmitted = true;
                synchronized (PlayerNameInputError.this) {
                    PlayerNameInputError.this.notifyAll();
                }
                dispose();
            }
        });

        setSize(350, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public synchronized boolean getStatus() {
        while (!isSubmitted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

