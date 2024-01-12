package server.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindSecondPlayer extends JFrame {
    private JTextField nameField;
    private JTextField nameSecondPlayer;
    private JButton connectButton;
    private String clientName;
    private boolean isSubmitted = false;

    public FindSecondPlayer() {
        setTitle("Поиск игрока");
        setLayout(new FlowLayout());

        nameField = new JTextField(20);
        connectButton = new JButton("Найти");
        getContentPane().setBackground(Color.decode("#76ff70"));
        connectButton.setPreferredSize(new Dimension(250, 40)); // ширина 100, высота 40


        JLabel label = new JLabel("Введите имя второго игрока:");
        add(label);
        add(nameField);
        add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientName = nameField.getText();
                isSubmitted = true;
                synchronized (FindSecondPlayer.this) {
                    FindSecondPlayer.this.notifyAll();
                }
                dispose();
            }
        });

        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public synchronized String getSecondPlayerName() {
        while (!isSubmitted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return clientName;
    }
    public synchronized void remove(){
        dispose();
    }
}

