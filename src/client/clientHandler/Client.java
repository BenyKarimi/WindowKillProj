package client.clientHandler;

import client.controller.constant.Constants;
import client.controller.saveAndLoad.FileManager;
import client.view.container.GlassFrame;
import client.view.container.LeaderboardPanel;
import client.view.container.SquadPanel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static client.clientHandler.ClientUtil.getBattleStateWithString;
import static client.clientHandler.ClientUtil.getStateWithString;

public class Client extends Thread{
    private final String SERVER_ADDRESS;
    private final int TCP_PORT;
    private Socket tcpSocket;
    private BufferedReader tcpReader;
    private PrintWriter tcpWriter;
    private final String username;
    private ClientState clientState;
    private SquadState squadState;
    private String squadName;
    private String vaultInfo;
    private BattleStatus battleStatus;
    private ArrayList<String> squadMembers;
    private ArrayList<String> enemySquadMember;
    private int xpDonation;

    public Client(String username, ClientState clientState) {
        this.username = username;
        this.SERVER_ADDRESS = Constants.SERVER_ADDRESS;
        this.TCP_PORT = Constants.TCP_PORT;
        this.clientState = clientState;
        this.squadState = SquadState.NO_SQUAD;
        battleStatus = BattleStatus.NO;
        squadMembers = new ArrayList<>();
        enemySquadMember = new ArrayList<>();
    }

    public boolean canMakeConnection() {
        if (clientState.equals(ClientState.OFFLINE)) return true;
        try {
            initSocket();
            initIOStreams();
            this.start();
            handleLogin();
            handleOfflineSave();
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

    /// handle responses and requests
    private void handleLogin() {
        tcpWriter.println("LOGIN" + "░░" + username);
    }
    private void handleOfflineSave() {
        String tmp = FileManager.loadFinishGameInformation();

        if (tmp != null) {
            String[] parts = tmp.split("█");
            handleSavingData(Integer.parseInt(parts[0]), parts[1]);
        }
    }
    private void handleLoggedIn(int xp, String squadType, String squadName, String battleType, int donation) {
        Constants.INITIAL_XP = xp;
        squadState = getStateWithString(squadType);
        this.squadName = squadName;
        battleStatus = getBattleStateWithString(battleType);
        xpDonation = donation;
    }
    public void handleSquadRequest() {
        tcpWriter.println("SQUAD_INFO");
    }
    public void handleLeaderboardRequest() {
        tcpWriter.println("LEADERBOARD_INFO");
    }
    private void handleSquadResponse(String[] parts) {
        squadState = getStateWithString(parts[1]);
        squadName = parts[2];
        battleStatus = getBattleStateWithString(parts[3]);
        xpDonation = Integer.parseInt(parts[4]);
        Constants.INITIAL_XP = Integer.parseInt(parts[5]);

        if (battleStatus.equals(BattleStatus.YES)) {
            squadMembers = new ArrayList<>(Arrays.asList(parts[6].split("╬")));
            enemySquadMember = new ArrayList<>(Arrays.asList(parts[7].split("╬")));
            vaultInfo = parts[8];
            ArrayList<String> history = new ArrayList<>(Arrays.asList(parts[9].split("╬")));
            SquadPanel.getINSTANCE().setBattleHistory(history);
        }
        else if (squadState.equals(SquadState.NO_SQUAD)) {
            ArrayList<String> squads = new ArrayList<>(Arrays.asList(parts).subList(6, parts.length));
            SquadPanel.getINSTANCE().setSquads(squads);
        }
        else {
            squadMembers = new ArrayList<>(Arrays.asList(parts).subList(6, parts.length));
        }
    }
    private void handleLeaderboardResponse(String[] parts) {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
        LeaderboardPanel.getINSTANCE().setInformation(info);
    }
    public void handleMakeSquad(String name) {
        tcpWriter.println("MAKE_SQUAD" + "░░" + name);
    }
    public void handleBuyFromVault(String type, int minusXp) {
        tcpWriter.println("BUY_FROM_VAULT" + "░░" + type + "░░" + minusXp);
    }
    public void handleJoinToSquad(String name) {
        tcpWriter.println("ASK_JOIN_SQUAD" + "░░" + name);
    }
    private void handleRequestForJoiningSquad(String applicatorName) {
        int response = JOptionPane.showConfirmDialog(GlassFrame.getINSTANCE(), "Do you accept " + applicatorName + " to be added to your team?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) tcpWriter.println("RESPONSE_JOIN_SQUAD" + "░░" + "YES" + "░░" + applicatorName);
        else tcpWriter.println("RESPONSE_JOIN_SQUAD" + "░░" + "NO" + "░░" + applicatorName);
    }
    private void handleRejectJoiningSquad() {
        JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You have been rejected to join squad by leader", "Rejection", JOptionPane.INFORMATION_MESSAGE);
    }
    private void handleRemovedFromSquad() {
        JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You have been removed from squad by leader", "Removed", JOptionPane.INFORMATION_MESSAGE);
    }
    private void handleDeletedSquad() {
        JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Your squad have been deleted by leader", "Squad Deleted", JOptionPane.INFORMATION_MESSAGE);
    }
    private void handleBattleAnnouncement() {
        JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Battle has started", "Battle Announcement", JOptionPane.INFORMATION_MESSAGE);
    }
    public void handleLeaveSquad() {
        tcpWriter.println("LEAVE_SQUAD");
    }
    public void removeFromSquad(String name) {
        tcpWriter.println("REMOVE_FROM_SQUAD" + "░░" + name);
    }
    public void handleDeleteSquad() {
        tcpWriter.println("DELETE_SQUAD");
    }
    public void makeClientBusy() {
        tcpWriter.println("MAKE_BUSY");
    }
    public void makeClientOnline() {
        tcpWriter.println("MAKE_ONLINE");
    }
    public void handleSendXPDonation(int xp) {
        tcpWriter.println("SEND_XP_DONATION" + "░░" + xp);
    }
    public void handleSavingData(int XP, String time) {
        String data = XP + "," + time;
        String hash = HashUtil.generateHash(data);

        tcpWriter.println("SAVE_DATA" + "░░" + hash + "░░" + XP + "░░" + time);
    }
    @Override
    public void run() {
        String request;
        try {
            while ((request = tcpReader.readLine()) != null) {
                String[] parts = request.split("░░");

                if (parts[0].equals("LOGGED_IN")) handleLoggedIn(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
                else if (parts[0].equals("SQUAD_INFO")) handleSquadResponse(parts);
                else if (parts[0].equals("DO_YOU_ACCEPT_JOINING")) handleRequestForJoiningSquad(parts[1]);
                else if (parts[0].equals("REJECT_JOIN_SQUAD")) handleRejectJoiningSquad();
                else if (parts[0].equals("REMOVED_FROM_SQUAD")) handleRemovedFromSquad();
                else if (parts[0].equals("SQUAD_DELETED")) handleDeletedSquad();
                else if (parts[0].equals("LEADERBOARD_INFO")) handleLeaderboardResponse(parts);
                else if (parts[0].equals("BATTLE_STARTED")) handleBattleAnnouncement();
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

    public ArrayList<String> getEnemySquadMember() {
        return enemySquadMember;
    }

    public String getVaultInfo() {
        return vaultInfo;
    }

    public int getXpDonation() {
        return xpDonation;
    }

    public SquadState getSquadState() {
        return squadState;
    }

    public ArrayList<String> getSquadMembers() {
        return squadMembers;
    }

    public String getSquadName() {
        return squadName;
    }

    public BattleStatus getBattleStatus() {
        return battleStatus;
    }
}
