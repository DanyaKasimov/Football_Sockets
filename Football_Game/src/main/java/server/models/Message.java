package server.models;


public class Message {
    public Message() {}
    public  String startMessage(String name) {
        return  "{\"name\":\"" + name + "\"}\n";
    }
    public  String startResponseServer(int status){
        return  "{\"status\":\""+ status + "\"}\n";
    }
    public  String cmdMessage(String type, int cmd, String player_2, String player_1, boolean status){
        return  "{\"type\":\"" + type + "\", \"cmd\":\"" + cmd + "\", \"otherClientName\":\"" + player_2 + "\", \"player\":\"" + player_1 + "\",\"status\":\"" + status + "\" }\n";
    }
    public String responseServerMessage(String type, String player_1, String player_2, int status){
        return  "{\"type\":\"" + type + "\", \"player_1\":\"" + player_1 +"\", \"player_2\":\"" + player_2 +"\",\"status\":\"" + status +"\" }\n";
    }
    public String cordsMessage(String type, int id, int x, int y, String player1, String player2){
        return  "{\"type\":\"" + type + "\", \"id\":\"" + id + "\", \"x\":\"" + x + "\", \"y\":\"" + y + "\", \"player_1\":\"" + player1 + "\", \"player_2\":\"" + player2 + "\"}\n";
    }
}
