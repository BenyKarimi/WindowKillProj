package server;

import server.clientHandler.TcpClientHandler;
import server.dataBase.DataBase;
import server.models.User;
import server.models.UserState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            return out;
        }
        else {
            User toAdd = new User(0, username, UserState.ONLINE, clientHandler);
            dataBase.addUser(toAdd);
            return toAdd;
        }
    }
}
