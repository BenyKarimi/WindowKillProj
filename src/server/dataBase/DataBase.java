package server.dataBase;

import server.models.User;

import java.util.ArrayList;

public class DataBase {
    private ArrayList<User> usersList;

    public DataBase() {
        usersList = new ArrayList<>();
    }

    public User getUser(String username) {
        for (User ptr : usersList) {
            if (ptr.getUsername().equals(username)) return ptr;
        }
        return null;
    }
    public void addUser(User user) {
        usersList.add(user);
    }
}
