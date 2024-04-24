package controller;


import view.bulletView.BulletView;
import view.charecterViews.SquareEnemyView;
import view.charecterViews.TriangleEnemyView;
import view.collectibleView.CollectibleView;

public class Controller {
    private static Controller INSTANCE;
    public Updater updater;
    public Logic logic;
    public Controller() {
        INSTANCE = this;
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
    public void createBulletView(String id) {
        new BulletView(id);
    }
    public void createCollectibleView(String id) {
        new CollectibleView(id);
    }
    public static Controller getINSTANCE() {
//        if (INSTANCE == null) INSTANCE = new Controller();
        return INSTANCE;
    }
    public static void setINSTANCE(Controller INSTANCE) {
        Controller.INSTANCE = INSTANCE;
    }
}
