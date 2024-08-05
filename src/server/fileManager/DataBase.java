package server.fileManager;

import server.models.Squad;
import server.models.User;

import java.util.ArrayList;

public class DataBase {
    private ArrayList<User> usersList;
    private ArrayList<Squad> squadsList;

    public DataBase() {
        usersList = new ArrayList<>();
        squadsList = new ArrayList<>();
    }

    public User getUser(String username) {
        for (User ptr : usersList) {
            if (ptr.getUsername().equals(username)) return ptr;
        }
        return null;
    }
    public Squad getSquad(String name) {
        for (int i = 0; i < squadsList.size(); i++) {
            if (squadsList.get(i).getName().equals(name)) return squadsList.get(i);
        }
        return null;
    }
    public void addUser(User user) {
        usersList.add(user);
    }
    public void addSquad(Squad squad) {
        squadsList.add(squad);
    }
    public void saveUsers() {
        FileManager.saveUsersInFile(usersList);
    }
    public ArrayList<Squad> getSquadsList() {
        return squadsList;
    }
}
