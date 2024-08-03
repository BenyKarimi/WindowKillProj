package client;

import client.controller.constant.Constants;
import server.models.SquadState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread{
    private final String SERVER_ADDRESS;
    private final int TCP_PORT;
    private Socket tcpSocket;
    private BufferedReader tcpReader;
    private PrintWriter tcpWriter;
    private String username;
    private ClientState clientState;
    private SquadState squadState;

    public Client(String username, ClientState clientState) {
        this.username = username;
        this.SERVER_ADDRESS = Constants.SERVER_ADDRESS;
        this.TCP_PORT = Constants.TCP_PORT;
        this.clientState = clientState;
        this.squadState = SquadState.NO_SQUAD;
    }

    public boolean canMakeConnection() {
        if (clientState.equals(ClientState.OFFLINE)) return true;
        try {
            initSocket();
            initIOStreams();
            handleLogin();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void initSocket() throws IOException {
        this.tcpSocket = new Socket(SERVER_ADDRESS, TCP_PORT);
    }
    private void initIOStreams() throws IOException {
        this.tcpReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        this.tcpWriter = new PrintWriter(tcpSocket.getOutputStream(), true);
    }
    private void handleLogin() {
        tcpWriter.println("LOGIN" + "░░" + username);
    }
    private void handleLoggedIn(int xp, String squadType) {
        Constants.INITIAL_XP = xp;
        if (squadType.equals("NO_SQUAD")) squadState = SquadState.NO_SQUAD;
        else if (squadType.equals("LEADER")) squadState = SquadState.LEADER;
        else squadState = SquadState.MEMBER;
    }

    @Override
    public void run() {
        String request;
        try {
            while ((request = tcpReader.readLine()) != null) {
                String[] parts = request.split("░░");

                if (parts[0].equals("LOGGED_IN")) handleLoggedIn(Integer.parseInt(parts[1]), parts[2]);
            }
        } catch (IOException ignored) {}
        clientState = ClientState.OFFLINE;
        try {
            tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientState getClientState() {
        return clientState;
    }
}
