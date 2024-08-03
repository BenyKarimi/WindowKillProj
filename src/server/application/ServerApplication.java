package server.application;

import server.TcpServer;

public class ServerApplication implements Runnable {
    @Override
    public void run() {
        TcpServer tcpServer = new TcpServer();
        tcpServer.start();
    }
}
