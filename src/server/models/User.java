package server.models;

import server.clientHandler.TcpClientHandler;

public class User {
    private int xp;
    private String username;
    private UserState userState;
    private TcpClientHandler clientHandler;
    private SquadState squadState;

    public User(int xp, String username, UserState userState, TcpClientHandler clientHandler) {
        this.xp = xp;
        this.username = username;
        this.userState = userState;
        this.clientHandler = clientHandler;
        this.squadState = SquadState.NO_SQUAD;
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

    public TcpClientHandler getClientHandler() {
        return clientHandler;
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
}
