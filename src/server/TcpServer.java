package server;

import server.clientHandler.TcpClientHandler;
import server.fileManager.DataBase;
import server.fileManager.FileManager;
import server.models.*;
import server.util.Pair;
import server.util.RandomUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static client.controller.constant.Constants.TCP_PORT;

public class TcpServer extends Thread {
    private DataBase dataBase;
    private ServerSocket tcpSocket;
    private Thread serverConsole;
    private Scanner consoleScanner;

    public TcpServer() {
        this.dataBase = new DataBase();
        consoleScanner = new Scanner(System.in);
        initiateConsoleThread();

        try {
            tcpSocket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initiateConsoleThread() {
        serverConsole = new Thread(() -> {
            while (true) {
                String init = consoleScanner.next();
                if (init.equals("initiateSquadBattle")) {
                    initiateSquadBattle();
                }
                else if (init.equals("terminateSquadBattle")) {
                    terminateSquadBattle();
                }
            }
        });
        serverConsole.start();
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

    private void initiateSquadBattle() {
        ArrayList<Pair<Integer, Integer>> pointers = RandomUtil.randomSquadInitiation(dataBase.getSquadsList());

        for (Pair<Integer, Integer> ptr : pointers) {
            Squad first = dataBase.getSquadsList().get(ptr.getFirst());
            Squad second = dataBase.getSquadsList().get(ptr.getSecond());

            first.setEnemySquad(second);
            second.setEnemySquad(first);
        }
        battleAnnouncement();
    }
    private void handleWonAndLostBattle(Squad winner, Squad looser) {
        int addXp = (winner.isGefjon() ? 1000 : 500);

        for (int i = 0; i < winner.getMembers().size(); i++) {
            winner.getMembers().get(i).setXp(winner.getMembers().get(i).getXp() + addXp);
            winner.getMembers().get(i).setBattleStatus(BattleStatus.NO);
            winner.getMembers().get(i).setXpDonation(0);
        }
        winner.setAdonis(false);
        winner.setGefjon(false);
        winner.getHistory().add(looser.getName() + "█" + "Won");

        int reduceXp = 300;
        if (looser.isPalioxis()) reduceXp = 100;
        if (looser.isGefjon()) reduceXp *= 2;

        for (int i = 0; i < looser.getMembers().size(); i++) {
            looser.getMembers().get(i).setXp(looser.getMembers().get(i).getXp() - reduceXp);
            looser.getMembers().get(i).setBattleStatus(BattleStatus.NO);
            looser.getMembers().get(i).setXpDonation(0);
        }
        looser.setPalioxis(false);
        looser.setAdonis(false);
        looser.setGefjon(false);
        looser.getHistory().add(looser.getName() + "█" + "Lost");
    }
    private void terminateSquadBattle() {
        for (int i = 0; i < dataBase.getSquadsList().size(); i++) {
            Squad first = dataBase.getSquadsList().get(i);
            Squad second = first.getEnemySquad();

            if (second == null) continue;

            if (first.getGainedXp() > second.getGainedXp()) {
                handleWonAndLostBattle(first, second);
            }
            else if (first.getGainedXp() < second.getGainedXp()) {
                handleWonAndLostBattle(second, first);
            }
            else {
                if (first.getMonomachiaWinCounter() > second.getMonomachiaWinCounter()) {
                    handleWonAndLostBattle(first, second);;
                }
                else if (first.getMonomachiaWinCounter() < second.getMonomachiaWinCounter()) {
                    handleWonAndLostBattle(second, first);
                }
                else {
                    if (first.isGefjon() && !second.isGefjon()) {
                        handleWonAndLostBattle(first, second);
                    }
                    else if (!first.isGefjon() && second.isGefjon()) {
                        handleWonAndLostBattle(second, first);
                    }
                    else {
                        if (RandomUtil.findRandomWinner() == 0) {
                            handleWonAndLostBattle(first, second);
                        }
                        else {
                            handleWonAndLostBattle(second, first);
                        }
                    }
                }
            }

            second.setEnemySquad(null);
            first.setEnemySquad(null);
        }
    }
    private void battleAnnouncement() {
        for (int i = 0; i < dataBase.getSquadsList().size(); i++) {
            Squad tmp = dataBase.getSquadsList().get(i);

            if (tmp.getEnemySquad() != null) {
                for (int j = 0; j < tmp.getMembers().size(); j++) {
                    User tmpUser = tmp.getMembers().get(j);
                    tmpUser.setBattleStatus(BattleStatus.YES);
                    tmpUser.setXpDonation(0);
                    tmpUser.getClientHandler().sendBattleAnnouncement();
                }
            }
        }
        dataBase.saveUsers();
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
            User toAdd = new User(1000, username, UserState.ONLINE, clientHandler);
            dataBase.addUser(toAdd);
            dataBase.saveUsers();
            return toAdd;
        }
    }
    public String handleSuadInfo(User user) {
        StringBuilder out = new StringBuilder();
        if (user.getBattleStatus().equals(BattleStatus.YES)) {

            Squad userSquad = dataBase.getSquad(user.getSquadName());
            ArrayList<User> mem = userSquad.getMembers();
            for (int i = 0; i < mem.size(); i++) {
                out.append(mem.get(i).getUsername()).append("█").append(mem.get(i).getXp()).append("█").append(mem.get(i).getUserState()).append("╬");
            }
            out.append("░░");

            Squad enemySquad = userSquad.getEnemySquad();
            ArrayList<User> enemyMem = enemySquad.getMembers();
            for (int i = 0; i < enemyMem.size(); i++) {
                out.append(enemyMem.get(i).getUsername()).append("█").append(enemyMem.get(i).getXp()).append("█").append(enemyMem.get(i).getUserState()).append("╬");
            }
            out.append("░░");

            out.append(userSquad.getSquadXP()).append("█").append(userSquad.isPalioxis()).append("█").append(userSquad.isAdonis()).append("█").append(userSquad.isGefjon()).append("░░");

            out.append("╬");
            for (int i = 0; i < userSquad.getHistory().size(); i++) {
                out.append(userSquad.getHistory().get(i));
                if (i != userSquad.getHistory().size() - 1) out.append("╬");
            }
        }
        else if (user.getSquadState().equals(SquadState.NO_SQUAD)) {
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
    public void handleSavingData(String XP, String time, User user) {
        user.setXp(Integer.parseInt(XP));
        dataBase.saveUsers();

        FileManager.saveInformationForUser(user.getUsername(), XP, time);
    }
    public String handleLeaderboardInfo() {
        StringBuilder out = new StringBuilder();
        ArrayList<User> users = dataBase.getUsersList();

        for (int i = 0; i < users.size(); i++) {
            String tmp = FileManager.loadInformationForUser(users.get(i).getUsername());
            if (tmp != null) out.append(tmp).append("░░");
        }

        return out.toString();
    }
    public void handleSendingDonation(int xp, User user) {
        Squad squad = dataBase.getSquad(user.getSquadName());
        squad.setSquadXP(squad.getSquadXP() + xp);
        user.setXpDonation(user.getXpDonation() + xp);
        user.setXp(user.getXp() - xp);
        dataBase.saveUsers();
    }
    public void handleBuyFromVault(String type, int minusXp, User user) {
        Squad squad = dataBase.getSquad(user.getSquadName());

        squad.setSquadXP(squad.getSquadXP() - minusXp);
        if (type.equals("PALIOXIS")) squad.setPalioxis(true);
        else if (type.equals("ADONIS")) squad.setAdonis(true);
        else if (type.equals("GEFJON")) squad.setGefjon(true);
    }
}
