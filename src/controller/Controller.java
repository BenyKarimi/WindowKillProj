package controller;


import view.charecterViews.SquareEnemyView;
import view.charecterViews.TriangleEnemyView;

public final class Controller {
    private static Controller INSTANCE;
    Updater updater;
    Logic logic;
    public Controller() {
        logic = new Logic();
        updater = new Updater();
        updater.epsilon = logic.epsilon;
    }
    public void createTriangleEnemyView(String id) {
        new TriangleEnemyView(id);
    }
    public void createSquareEnemyView(String id) {
        new SquareEnemyView(id);
    }
    public static Controller getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Controller();
        return INSTANCE;
    }
}
