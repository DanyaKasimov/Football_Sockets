package server.models;

public class Command {
    public String type;
    public Integer cmd;
    public String player;
    public String otherClientName;

    public boolean status;



    @Override
    public String toString() {
        return "Cmd{" +
                "type=" + type +
                ", cmd=" + cmd +
                ", otherClientName='" + otherClientName + '\'' +
                ", player='" + player + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
