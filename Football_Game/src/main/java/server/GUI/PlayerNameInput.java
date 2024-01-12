package server.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameInput extends JFrame {
    private JTextField nameField;
    private JButton connectButton;
    private String clientName;
    private boolean isSubmitted = false;

    public PlayerNameInput() {
        setTitle("Введите ваше имя");
        setLayout(new FlowLayout());

        nameField = new JTextField(20);
        connectButton = new JButton("Подключиться");

        getContentPane().setBackground(Color.decode("#76ff70"));
        connectButton.setPreferredSize(new Dimension(250, 40));


        JLabel label = new JLabel("Введите ваше имя:");
        add(label);
        add(nameField);
        add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientName = nameField.getText();
                isSubmitted = true;
                synchronized (PlayerNameInput.this) {
                    PlayerNameInput.this.notifyAll();
                }
                dispose();
            }
        });

        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public synchronized String getClientName() {
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

