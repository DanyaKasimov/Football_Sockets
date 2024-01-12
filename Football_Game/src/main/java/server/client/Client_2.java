package server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.GUI.FindSecondPlayer;
import server.GUI.MainMenu;
import server.GUI.PlayerNameInput;
import server.GUI.PlayerNameInputError;
import server.handlers.ClientHandler;
import server.models.HelloClientResponse;
import server.models.Message;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client_2 {

    public static int SERVER_PORT = 50000;

    public static void main(String[] args) {
        PlayerNameInput nameInput = new PlayerNameInput();
        String name = nameInput.getClientName();
        Message message = new Message();
        System.out.println(name);
        String server_address = args.length > 1 ? args[1] : "127.0.0.1";
        int server_port = args.length > 2 ? Integer.parseInt(args[2]) : SERVER_PORT;
        String status;
        try {
            Socket socket = new Socket(server_address, server_port);
            socket.getOutputStream().write(message.startMessage(name).getBytes(StandardCharsets.UTF_8));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String cmd = bufferedReader.readLine();

            ObjectMapper mapper = new ObjectMapper();
            HelloClientResponse serverResponse = mapper.readValue(cmd, HelloClientResponse.class);
            if (serverResponse != null && serverResponse.status != null) {
                status = serverResponse.status;
            } else {
                throw new RuntimeException("Не смогли обработать ответ сервера!");
            }

            Thread thread = new Thread(new ClientHandler(socket));
            thread.setDaemon(true);
            thread.start();
            if (status.equals("1")) {
                try {
                    MainMenu mainMenu = new MainMenu(name);
                    switch (mainMenu.getStatusMenu()) {
                        case 1 -> {
                            FindSecondPlayer findSecondPlayer = new FindSecondPlayer();
                            String gamer2 = findSecondPlayer.getSecondPlayerName();
                            socket.getOutputStream().write(message.cmdMessage("cmd", 1, gamer2, name, false).getBytes(StandardCharsets.UTF_8));
                            socket.getOutputStream().flush();
                        }
                        case 2 -> socket.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                PlayerNameInputError playerNameInputError = new PlayerNameInputError();
                if (playerNameInputError.getStatus()) {
                    socket.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
