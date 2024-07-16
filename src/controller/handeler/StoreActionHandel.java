package controller.handeler;

import controller.Controller;
import model.charactersModel.EpsilonModel;
import model.movement.ImpactMechanism;
import view.container.GamePanel;

public class StoreActionHandel {
    private static boolean doImpact = false;
    private static boolean threeBullet = false;
    private static boolean addHp = false;
    private static long startThreeBullet;

    public static void handelActions() {
        GamePanel.getINSTANCE().setVisible(true);
        Controller.getINSTANCE().updater.getViewUpdater().start();
        Controller.getINSTANCE().updater.getModelUpdater().start();
        GamePanel.getINSTANCE().getTimer().Start();

        EpsilonModel epsilon = Controller.getINSTANCE().logic.epsilon;

        if (doImpact) {
            ImpactMechanism.applyImpact(epsilon.getCenter(), 15);
            doImpact = false;
        }
        else if (threeBullet) {
            startThreeBullet = GamePanel.getINSTANCE().getTimer().getSeconds();
        }
        else if (addHp) {
            epsilon.setHp(epsilon.getHp() + 10);
            addHp = false;
        }
    }

    public static long getStartThreeBullet() {
        return startThreeBullet;
    }

    public static boolean isDoImpact() {
        return doImpact;
    }

    public static boolean isThreeBullet() {
        return threeBullet;
    }

    public static boolean isAddHp() {
        return addHp;
    }

    public static void setDoImpact(boolean doImpact) {
        StoreActionHandel.doImpact = doImpact;
    }

    public static void setThreeBullet(boolean threeBullet) {
        StoreActionHandel.threeBullet = threeBullet;
    }

    public static void setAddHp(boolean addHp) {
        StoreActionHandel.addHp = addHp;
    }
}
