package server;

import server.clientHandler.TcpClientHandler;
import server.fileManager.DataBase;
import server.models.Squad;
import server.models.SquadState;
import server.models.User;
import server.models.UserState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static client.controller.constant.Constants.TCP_PORT;

public class TcpServer extends Thread {
    private DataBase dataBase;
    private ServerSocket tcpSocket;

    public TcpServer() {
        this.dataBase = new DataBase();

        try {
            tcpSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("TCP server started on port " + TCP_PORT);

        while (true) {
            Socket socket = null;
            try {
                socket = tcpSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("A client connected with Address: " + socket.getRemoteSocketAddress());
            new TcpClientHandler(socket, this).start();
        }
    }

    public User handleLogin(String username, TcpClientHandler clientHandler) {
        User out = dataBase.getUser(username);
        if (out != null) {
            out.setUserState(UserState.ONLINE);
            out.setClientHandler(clientHandler);
            dataBase.saveUsers();
            return out;
        }
        else {
            User toAdd = new User(0, username, UserState.ONLINE, clientHandler);
            dataBase.addUser(toAdd);
            dataBase.saveUsers();
            return toAdd;
        }
    }
    public String handleSuadInfo(User user) {
        StringBuilder out = new StringBuilder();
        if (user.getSquadState().equals(SquadState.NO_SQUAD)) {
            ArrayList<Squad> squads = dataBase.getSquadsList();
            for (int i = 0; i < squads.size(); i++) {
                out.append(squads.get(i).getName()).append("█").append(squads.get(i).getMembers().size()).append("░░");
            }
        }
        else {
            Squad userSquad = dataBase.getSquad(user.getSquadName());
            ArrayList<User> mem = userSquad.getMembers();
            for (int i = 0; i < mem.size(); i++) {
                out.append(mem.get(i).getUsername()).append("█").append(mem.get(i).getXp()).append("█").append(mem.get(i).getUserState()).append("░░");
            }
        }
        return out.toString();
    }
    public void handleMakingSquad(String name, User leader) {
        Squad newSquad = new Squad(name);
        newSquad.setLeader(leader);
        newSquad.getMembers().add(leader);
        leader.setSquadName(name);
        leader.setSquadState(SquadState.LEADER);
        dataBase.saveUsers();
        dataBase.addSquad(newSquad);
    }
    public void handleAskJoiningSquad(String name, User applicator) {
        Squad dest = dataBase.getSquad(name);
        User destLeader = dest.getLeader();
        destLeader.getClientHandler().handleMakeRequestForJoiningSquad(applicator.getUsername());
    }
    public void handleAnswerJoiningSquad(String res, String applicatorName, String squadName) {
        User applicator = dataBase.getUser(applicatorName);
        Squad destSquad = dataBase.getSquad(squadName);

        if (res.equals("YES")) {
            applicator.setSquadState(SquadState.MEMBER);
            applicator.setSquadName(squadName);
            destSquad.getMembers().add(applicator);
            dataBase.saveUsers();
            applicator.getClientHandler().handleSquadInfo();
        }
        else {
            applicator.getClientHandler().handleRejectForJoiningSquad();
        }
    }
    public void handleRemoveFromSquad(String username) {
        User user = dataBase.getUser(username);
        Squad userSquad = dataBase.getSquad(user.getSquadName());

        for (int i = 0; i < userSquad.getMembers().size(); i++) {
            if (userSquad.getMembers().get(i).equals(user)) {
                userSquad.getMembers().remove(i);
                break;
            }
        }

        user.setSquadName(null);
        user.setSquadState(SquadState.NO_SQUAD);
        dataBase.saveUsers();
        user.getClientHandler().handleSquadInfo();
    }
    public void sendRemovedFromSquad(String username) {
        User user = dataBase.getUser(username);

        user.getClientHandler().sendRemovedFromSquad();
    }
    public void handleRemoveSquad(String squadName) {
        Squad squad = dataBase.getSquad(squadName);

        for (int i = 0; i < squad.getMembers().size(); i++) {
            User user = squad.getMembers().get(i);

            user.setSquadName(null);
            user.setSquadState(SquadState.NO_SQUAD);
            dataBase.saveUsers();
            user.getClientHandler().sendDeletedSquad();
            user.getClientHandler().handleSquadInfo();
        }

        dataBase.deleteSquad(squad);
    }
    public void handleChangeUserState(User user, UserState state) {
        user.setUserState(state);
        dataBase.saveUsers();
    }
}
