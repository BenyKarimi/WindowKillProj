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
        writer.println("LOGGED_IN" + "░░" + user.getXp() + "░░" + user.getSquadState());
    }
}
