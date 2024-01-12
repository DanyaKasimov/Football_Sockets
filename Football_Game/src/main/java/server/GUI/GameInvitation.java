package server.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameInvitation extends JDialog {

    private boolean status = false;
    private boolean isSubmitted = false;
    public GameInvitation(JFrame parent, String invitingPlayer) {
        super(parent, "Приглашение в игру", true);
        setLayout(new FlowLayout());
        setSize(300, 120);
        setLocationRelativeTo(parent);

        JLabel messageLabel = new JLabel(invitingPlayer + " приглашает вас в игру!");
        JButton acceptButton = new JButton("Принять");
        acceptButton.setBackground(Color.GREEN);

        JButton declineButton = new JButton("Отклонить");

        acceptButton.setPreferredSize(new Dimension(120, 40));
        declineButton.setPreferredSize(new Dimension(120, 40));
        getContentPane().setBackground(Color.decode("#fff0c3"));

        add(messageLabel);
        add(acceptButton);
        add(declineButton);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = true;
                isSubmitted = true;
                synchronized (GameInvitation.this) {
                    GameInvitation.this.notifyAll();
                }
                dispose();
            }
        });

        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = false;
                isSubmitted = true;
                synchronized (GameInvitation.this) {
                    GameInvitation.this.notifyAll();
                }
                dispose();
            }
        });
    }
    public synchronized boolean getStatusInvitation() {
        return status;
    }
    public synchronized void remove(){
        dispose();
    }

}

