package server.clientHandler;

import server.TcpServer;
import server.models.User;
import server.models.UserState;
import server.util.HashUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClientHandler extends Thread {
    private Socket socket;
    private TcpServer tcpServer;
    private BufferedReader reader;
    private PrintWriter writer;
    private User user;


    public TcpClientHandler(Socket socket, TcpServer tcpServer) {
        this.socket = socket;
        this.tcpServer = tcpServer;
        initIOStreams();
    }
    private void initIOStreams() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String request;
        try {
            while ((request = reader.readLine()) != null) {
                String[] parts = request.split("░░");

                if (parts[0].equals("LOGIN")) handleLogin(parts[1]);
                else if (parts[0].equals("SQUAD_INFO")) handleSquadInfo();
                else if (parts[0].equals("MAKE_SQUAD")) handleMakingSquad(parts[1], Integer.parseInt(parts[2]));
                else if (parts[0].equals("ASK_JOIN_SQUAD")) handleAskJoiningSquad(parts[1]);
                else if (parts[0].equals("RESPONSE_JOIN_SQUAD")) handleResponseForJoiningSquad(parts[1], parts[2]);
                else if (parts[0].equals("LEAVE_SQUAD")) handleLeaveSquad();
                else if (parts[0].equals("REMOVE_FROM_SQUAD")) handleRemoveFromSquad(parts[1]);
                else if (parts[0].equals("DELETE_SQUAD")) handleRemoveSquad();
                else if (parts[0].equals("MAKE_BUSY")) handleMakeBusy();
                else if (parts[0].equals("MAKE_ONLINE")) handleMakeOnline();
                else if (parts[0].equals("SAVE_DATA")) handleSavingData(parts[1], parts[2], parts[3]);
                else if (parts[0].equals("LEADERBOARD_INFO")) handleLeaderboardInfo();
                else if (parts[0].equals("SEND_XP_DONATION")) handleSendingDonation(Integer.parseInt(parts[1]));
                else if (parts[0].equals("BUY_FROM_VAULT")) handleBuyFromVault(parts[1], Integer.parseInt(parts[2]));
            }
        } catch (IOException ignored) {}
        tcpServer.handleChangeUserState(user, UserState.OFFLINE);
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLogin(String username) {
        user = tcpServer.handleLogin(username, this);
        sendQueuedMessages();
        sendMessage("LOGGED_IN" + "░░" + user.getXp() + "░░" + user.getSquadState() + "░░" + user.getSquadName() + "░░" + user.getBattleStatus() + "░░" + user.getXpDonation());
    }
    public void handleSquadInfo() {
        String res = "SQUAD_INFO" + "░░" + user.getSquadState() + "░░" + user.getSquadName() + "░░" + user.getBattleStatus() + "░░" + user.getXpDonation() + "░░" + user.getXp() + "░░" + tcpServer.handleSuadInfo(user);
        sendMessage(res);
    }
    private void handleBuyFromVault(String type, int minusXp) {
        tcpServer.handleBuyFromVault(type, minusXp, user);
    }
    public void handleLeaderboardInfo() {
        String res = "LEADERBOARD_INFO" + "░░" + tcpServer.handleLeaderboardInfo();
        sendMessage(res);
    }
    private void handleSendingDonation(int xp) {
        tcpServer.handleSendingDonation(xp, user);
    }
    private void handleLeaveSquad() {
        tcpServer.handleRemoveFromSquad(user.getUsername());
    }
    private void handleSavingData(String hash, String XP, String time) {
        String checkHash = HashUtil.generateHash(XP + "," + time);
        if (hash.equals(checkHash)) {
            tcpServer.handleSavingData(XP, time, user);
        }
    }
    private void handleRemoveFromSquad(String username) {
        tcpServer.sendRemovedFromSquad(username);
        tcpServer.handleRemoveFromSquad(username);
    }
    private void handleRemoveSquad() {
        tcpServer.handleRemoveSquad(user.getSquadName());
    }
    private void handleMakeBusy() {
        tcpServer.handleChangeUserState(user, UserState.BUSY);
    }
    private void handleMakeOnline() {
        tcpServer.handleChangeUserState(user, UserState.ONLINE);
        sendQueuedMessages();
    }
    private void handleMakingSquad(String name, int reduceXp) {
        tcpServer.handleMakingSquad(name, user, reduceXp);
        handleSquadInfo();
    }
    private void handleAskJoiningSquad(String name) {
        tcpServer.handleAskJoiningSquad(name, user);
    }
    public void handleMakeRequestForJoiningSquad(String applicatorName) {
        sendMessage("DO_YOU_ACCEPT_JOINING" + "░░" + applicatorName);
    }
    private void handleResponseForJoiningSquad(String res, String applicatorName) {
        tcpServer.handleAnswerJoiningSquad(res, applicatorName, user.getSquadName());
    }
    public void handleRejectForJoiningSquad() {
        sendMessage("REJECT_JOIN_SQUAD");
    }
    public void sendRemovedFromSquad() {
        sendMessage("REMOVED_FROM_SQUAD");
    }
    public void sendDeletedSquad() {
        sendMessage("SQUAD_DELETED");
    }
    public void sendBattleAnnouncement() {
        sendMessage("BATTLE_STARTED");
    }

    public void sendQueuedMessages() {
        for (int i = 0; i < user.getMessageQueue().size(); i++) {
            sendMessage(user.getMessageQueue().get(i));
            user.getMessageQueue().remove(i--);
        }
    }
    private void sendMessage(String message) {
        if (user.getUserState().equals(UserState.ONLINE)) writer.println(message);
        else user.getMessageQueue().add(message);
    }
}
