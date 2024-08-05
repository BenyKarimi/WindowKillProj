package server.clientHandler;

import server.TcpServer;
import server.models.User;
import server.models.UserState;

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
                else if (parts[0].equals("MAKE_SQUAD")) handleMakingSquad(parts[1]);
                else if (parts[0].equals("ASK_JOIN_SQUAD")) handleAskJoiningSquad(parts[1]);
                else if (parts[0].equals("RESPONSE_JOIN_SQUAD")) handleResponseForJoiningSquad(parts[1], parts[2]);
            }
        } catch (IOException ignored) {}
        user.setUserState(UserState.OFFLINE);
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLogin(String username) {
        user = tcpServer.handleLogin(username, this);
        sendQueuedMessages();
        sendMessage("LOGGED_IN" + "░░" + user.getXp() + "░░" + user.getSquadState() + "░░" + user.getSquadName());
    }
    public void handleSquadInfo() {
        String res = "SQUAD_INFO" + "░░" + user.getSquadState() + "░░" + user.getSquadName() + "░░" + tcpServer.handleSuadInfo(user);
        sendMessage(res);
    }
    private void handleMakingSquad(String name) {
        tcpServer.handleMakingSquad(name, user);
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