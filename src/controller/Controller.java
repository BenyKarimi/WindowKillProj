package controller;


public final class Controller {
    private static Controller INSTANCE;
    Updater updater;
    Logic logic;
    public Controller() {
        logic = new Logic();
        updater = new Updater();
        updater.epsilon = logic.epsilon;
    }

    public static Controller getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new Controller();
        return INSTANCE;
    }
}
