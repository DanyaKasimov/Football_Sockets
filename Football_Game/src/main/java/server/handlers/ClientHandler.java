package server.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.GameFrame;
import server.GUI.FindSecondPlayer;
import server.GUI.GameInvitation;
import server.GUI.MainMenu;
import server.GUI.WaitingSecondPlayer;
import server.models.CordsResponse;
import server.models.Message;
import server.models.ServerResponse;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


import java.io.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private WaitingSecondPlayer waitingSecondPlayer = new WaitingSecondPlayer();

    private String player_1;
    private String player_2;
    GameFrame game;
    private Message message = new Message();

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String cmd = bufferedReader.readLine();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonObj = mapper.readTree(cmd);
                String type = jsonObj.get("type").asText();
                if (type.equals("update")) {
                    CordsResponse cords = mapper.readValue(cmd, CordsResponse.class);
                    switch (cords.id) {
                        case 1 -> game.setPlayer1(cords.x,cords.y);
                        case 2 -> game.setPlayer2(cords.x,cords.y);
                        case 3 -> game.setBall(cords.x, cords.y);
                        case 4 -> game.setScore(cords.x,cords.y);
                    }
                }
                if (type.equals("connect")) {
                    ServerResponse response = mapper.readValue(cmd, ServerResponse.class);
                    this.player_1 = response.player_1;
                    this.player_2 = response.player_2;
                    switch (response.status) {
                        case 1 -> inviteShow(player_1, player_2);
                        case 2 -> openWindowGame();
                        case 3 -> waitingPlayer();
                        case 4 -> menuWindow(player_1);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void inviteShow(String player_1, String player_2) {
        JFrame mainFrame = new JFrame();
        GameInvitation invitationDialog = new GameInvitation(mainFrame, player_1);
        invitationDialog.setVisible(true);

        boolean statusInvitation = invitationDialog.getStatusInvitation();
        sendInvitationResponse(statusInvitation, player_1, player_2);
    }

    private void openWindowGame() {
        game = new GameFrame(this);
    }
    private void waitingPlayer() {
        waitingSecondPlayer.init();
    }
    private void menuWindow(String player_1) throws IOException {
        waitingSecondPlayer.remove();
        MainMenu mainMenu = new MainMenu(player_1);
        switch (mainMenu.getStatusMenu()) {
            case 1 -> {
                FindSecondPlayer findSecondPlayer = new FindSecondPlayer();
                String player_2 = findSecondPlayer.getSecondPlayerName();
                out.println(message.cmdMessage("cmd",1, player_2, player_1, false));
            }
            case 2 -> socket.close();
        }
    }

    private void sendInvitationResponse(boolean accepted, String player_1, String player_2) {
        String status = message.cmdMessage("cmd" ,2, player_2, player_1, accepted);
        out.println(status);
    }
    public void sendBallPosition(int x, int y) {
        String send = message.cordsMessage("game",3, x,y,player_1,player_2);
        out.println(send);
    }
    public void sendPlayerPosition(int id, int x, int y) {
        String send = message.cordsMessage("game",id, x,y,player_1,player_2);
        out.println(send);
    }
    public void sendScore(int player1, int player2){
        String send = message.cordsMessage("game",4, player1, player2,player_1,player_2);
        out.println(send);
    }
}
