package server;

import server.handlers.ServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class StartServer {
    public static int SERVER_PORT = 50000;
    private Map<String, Socket> clientConnectionList;

    public static void main(String[] args) {
        new StartServer();
    }
    public StartServer() {
        clientConnectionList = new HashMap<>();
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerHandler(clientConnectionList, clientSocket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
