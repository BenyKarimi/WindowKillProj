package server.fileManager;

import client.view.container.GlassFrame;
import server.models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static client.controller.constant.GameValues.*;

public class FileManager {
    public static void saveUsersInFile(ArrayList<User> users) {
        File usersFile = new File("C:\\Users\\benya\\Documents\\GitHub\\WindowKillProj\\server_storage\\information" + "/users.txt");

        try (FileWriter writer = new FileWriter(usersFile, false)) {
            for (User ptr : users) {
                writer.write(ptr.getUsername() + "░░" + ptr.getXp() + "░░" + ptr.getUserState() + "░░" + ptr.getSquadState() + "░░" + ptr.getSquadName() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
