package server.fileManager;

import server.models.User;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private static final String serverStoragePath = "C:\\Users\\benya\\Documents\\GitHub\\WindowKillProj\\server_storage/";
    public static void saveUsersInFile(ArrayList<User> users) {
        File usersFile = new File(serverStoragePath + "information" + "/users.txt");

        try (FileWriter writer = new FileWriter(usersFile, false)) {
            for (User ptr : users) {
                writer.write(ptr.getUsername() + "░░" + ptr.getXp() + "░░" + ptr.getUserState() + "░░" + ptr.getSquadState() + "░░" + ptr.getSquadName() + "\n");
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
