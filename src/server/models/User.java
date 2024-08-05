package server.models;

import server.clientHandler.TcpClientHandler;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private int xp;
    private String username;
    private UserState userState;
    private TcpClientHandler clientHandler;
    private SquadState squadState;
    private String squadName;
    private ArrayList<String> messageQueue;

    public User(int xp, String username, UserState userState, TcpClientHandler clientHandler) {
        this.xp = xp;
        this.username = username;
        this.userState = userState;
        this.clientHandler = clientHandler;
        this.squadState = SquadState.NO_SQUAD;
        this.messageQueue = new ArrayList<>();
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public ArrayList<String> getMessageQueue() {
        return messageQueue;
    }

    public TcpClientHandler getClientHandler() {
        return clientHandler;
    }

    public void setSquadName(String squadName) {
        this.squadName = squadName;
    }

    public void setClientHandler(TcpClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public SquadState getSquadState() {
        return squadState;
    }

    public void setSquadState(SquadState squadState) {
        this.squadState = squadState;
    }

    public String getSquadName() {
        return squadName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }
}
