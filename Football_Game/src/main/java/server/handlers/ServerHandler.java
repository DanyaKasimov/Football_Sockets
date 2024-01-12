package server.handlers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.models.CordsResponse;
import server.models.HelloClient;
import server.exception.ConnectionToOtherClientException;
import server.models.Command;
import server.models.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ServerHandler implements Runnable {

    private Map<String, Socket> clientConnectionMap;

    private Socket clientSocket;

    private String name;
    private Message message = new Message();

    public ServerHandler(Map<String, Socket> clientConnectionList, Socket clientSocket) {
        this.clientConnectionMap = clientConnectionList;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String cmd = bufferedReader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            HelloClient clientInfo = mapper.readValue(cmd, HelloClient.class);
            String response;
            if (clientInfo != null && clientInfo.name != null) {
                if (!clientConnectionMap.containsKey(clientInfo.name)) {
                    clientConnectionMap.put(clientInfo.name, clientSocket);
                    name = clientInfo.name;
                    response = message.startResponseServer(1);
                } else {
                    response = message.startResponseServer(2);
                }
            } else {
                response = message.startResponseServer(3);
            }
            clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
            while (true) {
                cmd = bufferedReader.readLine();
                JsonNode jsonObj = mapper.readTree(cmd);
                if (jsonObj.get("type") != null) {
                    String type = jsonObj.get("type").asText();
                    if (type.equals("cmd")) {
                        Command clientCommand = mapper.readValue(cmd, Command.class);
                        switch (clientCommand.cmd) {
                            case 1 -> invite(clientCommand);
                            case 2 -> startGame(clientCommand);
                        }
                    }
                    if (type.equals("game")) {
                        CordsResponse cords = mapper.readValue(cmd, CordsResponse.class);
                        switch (cords.id) {
                            case 1 -> firstPlayer(cords);
                            case 2 -> secondPlayer(cords);
                            case 3 -> ball(cords);
                            case 4 -> score(cords);
                        }
                    }
                }
            }
        } catch (IOException | ConnectionToOtherClientException e) {
            throw new RuntimeException(e);
        }
    }

    private void invite(Command command) throws ConnectionToOtherClientException, IOException {
        Socket playerSocket = clientConnectionMap.get(command.player);
        if (clientConnectionMap.containsKey(command.otherClientName)) {
            Socket otherClientSocket = clientConnectionMap.get(command.otherClientName);
            if (otherClientSocket != null && otherClientSocket.isConnected()) {
                String invitation = message.responseServerMessage("connect", command.player, command.otherClientName, 1);
                String waiting = message.responseServerMessage("connect", command.player, command.otherClientName, 3);
                this.sendMessage(otherClientSocket, invitation);
                this.sendMessage(playerSocket, waiting);
            } else {
                throw new ConnectionToOtherClientException();
            }
        } else {
            String menu = message.responseServerMessage("connect", command.player, command.otherClientName, 4);
            this.sendMessage(playerSocket, menu);
        }
    }
    private void startGame(Command command) throws ConnectionToOtherClientException, IOException {
        Socket otherClientSocket = clientConnectionMap.get(command.otherClientName);
        Socket playerSocket = clientConnectionMap.get(command.player);
        if (otherClientSocket != null && otherClientSocket.isConnected()) {
            String invitation;
            if (command.status){
                invitation = message.responseServerMessage( "connect" , command.player, command.otherClientName, 2);
                this.sendMessage(otherClientSocket, invitation);
                this.sendMessage(playerSocket, invitation);
            } else {
                invitation = message.responseServerMessage("connect" , command.player, command.otherClientName, 4);
                this.sendMessage(playerSocket, invitation);
            }
        } else {
            throw new ConnectionToOtherClientException();
        }
    }
    private void firstPlayer(CordsResponse cordsResponse) throws IOException {
        Socket player_2_Socket = clientConnectionMap.get(cordsResponse.player_1);
        Socket player_1_Socket = clientConnectionMap.get(cordsResponse.player_2);
        if (player_2_Socket != null && player_2_Socket.isConnected() && player_1_Socket != null && player_1_Socket.isConnected()) {
            String cords = message.cordsMessage("update", 1, cordsResponse.x , cordsResponse.y, cordsResponse.player_1, cordsResponse.player_2);
            this.sendMessage(player_2_Socket, cords);
        }
    }
    private void secondPlayer(CordsResponse cordsResponse) throws IOException {
        Socket player_2_Socket = clientConnectionMap.get(cordsResponse.player_1);
        Socket player_1_Socket = clientConnectionMap.get(cordsResponse.player_2);
        if (player_2_Socket != null && player_2_Socket.isConnected() && player_1_Socket != null && player_1_Socket.isConnected()) {
            String cords = message.cordsMessage("update", 2, cordsResponse.x, cordsResponse.y, cordsResponse.player_1, cordsResponse.player_2);
            this.sendMessage(player_1_Socket, cords);
        }
    }
    private void ball(CordsResponse cordsResponse) throws IOException {
        Socket player_2_Socket = clientConnectionMap.get(cordsResponse.player_1);
        Socket player_1_Socket = clientConnectionMap.get(cordsResponse.player_2);
        if (player_2_Socket != null && player_2_Socket.isConnected() && player_1_Socket != null && player_1_Socket.isConnected()) {
            String cords = message.cordsMessage("update", 3, cordsResponse.x, cordsResponse.y, cordsResponse.player_1, cordsResponse.player_2);
            this.sendMessage(player_1_Socket, cords);
        }
    }
    private void score(CordsResponse cordsResponse) throws IOException {
        Socket player_2_Socket = clientConnectionMap.get(cordsResponse.player_1);
        Socket player_1_Socket = clientConnectionMap.get(cordsResponse.player_2);
        if (player_2_Socket != null && player_2_Socket.isConnected() && player_1_Socket != null && player_1_Socket.isConnected()) {
            String cords = message.cordsMessage("update", 4, cordsResponse.x, cordsResponse.y, cordsResponse.player_1, cordsResponse.player_2);
            this.sendMessage(player_1_Socket, cords);
        }
    }
    private void sendMessage(Socket socket, String message) throws IOException {
        socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
        socket.getOutputStream().flush();
    }
}
