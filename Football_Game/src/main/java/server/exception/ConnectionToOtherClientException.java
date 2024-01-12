package server.exception;

public class ConnectionToOtherClientException extends Exception {
    public ConnectionToOtherClientException() {
        super("Ошибка соединения с другим игроком");
    }


}
