package server.fileManager;

import server.models.Squad;
import server.models.User;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private static final String serverStoragePath = "C:\\Users\\benya\\Documents\\GitHub\\WindowKillProj\\server_storage/";
    public static void saveUsersInFile(ArrayList<User> users) {
        File usersFile = new File(serverStoragePath + "information" + "/users.txt");

        try (FileWriter writer = new FileWriter(usersFile, false)) {
            for (User ptr : users) {
                writer.write(ptr.getUsername() + "░░" + ptr.getXp() + "░░" + ptr.getUserState() + "░░" + ptr.getSquadState() + "░░" + ptr.getSquadName() + "░░" + ptr.getBattleStatus() + "░░" + ptr.getXpDonation() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void saveSquadsInFile(ArrayList<Squad> squads) {
        File squadFile = new File(serverStoragePath + "information" + "/squads.txt");

        try (FileWriter writer = new FileWriter(squadFile, false)) {
            for (int i = 0; i < squads.size(); i++) {
                Squad ptr = squads.get(i);

                StringBuilder toWrite = new StringBuilder();
                toWrite.append(ptr.getName()).append("░░").append(ptr.getLeader().getUsername()).append("░░");

                for (User mem : ptr.getMembers()) toWrite.append(mem.getUsername()).append("█");
                toWrite.append("░░");

                if (ptr.getEnemySquad() != null) toWrite.append(ptr.getEnemySquad().getName()).append("░░");
                else toWrite.append("null").append("░░");

                toWrite.append(ptr.isPalioxis()).append("░░").append(ptr.isAdonis()).append("░░").append(ptr.isGefjon()).append("░░").append(ptr.getSquadXP()).append("░░");
                toWrite.append(ptr.getMonomachiaWinCounter()).append("░░").append(ptr.getGainedXp()).append("░░");

                for (String his : ptr.getHistory()) toWrite.append(his).append("╬");

                writer.write(toWrite + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void saveInformationForUser(String username, String XP, String time) {
        File userFolder = new File(serverStoragePath + username);
        if (!userFolder.exists()) userFolder.mkdir();

        File userInfo = new File(serverStoragePath + username + "/information.txt");
        try (FileWriter writer = new FileWriter(userInfo, false)) {
            writer.write(XP + "█" + time);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String loadInformationForUser(String username) {
        File userInfo = new File(serverStoragePath + username + "/information.txt");
        if (!userInfo.exists()) return null;

        try {
            FileReader fileReader = new FileReader(userInfo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            return username + "█" + bufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
