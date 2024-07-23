package controller.handeler;

import controller.Controller;
import controller.constant.GameValues;
import model.charactersModel.EpsilonModel;
import model.movement.ImpactMechanism;
import view.container.GamePanel;
import view.container.GlassFrame;

public class StoreActionHandle {
    private static boolean doImpact = false;
    private static boolean threeBullet = false;
    private static boolean addHp = false;
    private static boolean banishHovering = false;
    private static boolean freezing = false;
    private static boolean hotBullet = false;
    private static int startThreeBullet;
    private static int startBanish;
    private static int startFreezing;
    private static int hotBulletCoolDown;

    public static void handelActions() {
        for (GamePanel panel : GamePanel.gamePanelList) panel.setVisible(true);
        if (!GameValues.secondRoundFinish) {
            Controller.getINSTANCE().updater.getViewUpdater().start();
            Controller.getINSTANCE().updater.getModelUpdater().start();
        }
        else {
            Controller.getINSTANCE().logic.getBossUpdater().getViewUpdater().start();
            Controller.getINSTANCE().logic.getBossUpdater().getModelUpdater().start();
        }
        GlassFrame.getINSTANCE().getTimer().Start();

        EpsilonModel epsilon = Controller.getINSTANCE().logic.epsilon;

        if (doImpact) {
            ImpactMechanism.applyImpact(epsilon.getCenter(), 15);
            doImpact = false;
        }
        else if (threeBullet) {
            startThreeBullet = GlassFrame.getINSTANCE().getTimer().getSeconds();
        }
        else if (addHp) {
            epsilon.setHp(epsilon.getHp() + 10);
            addHp = false;
        }
        else if (banishHovering) {
            startBanish = GlassFrame.getINSTANCE().getTimer().getSeconds();
        }
        else if (freezing) {
            startFreezing = GlassFrame.getINSTANCE().getTimer().getSeconds();
        }
        else if (hotBullet) {
            hotBulletCoolDown = GlassFrame.getINSTANCE().getTimer().getSeconds();
        }
    }

    public static int getStartThreeBullet() {
        return startThreeBullet;
    }
    public static int getStartBanish() {
        return startBanish;
    }
    public static int getStartFreezing() {
        return startFreezing;
    }
    public static int getHotBulletCoolDown() {
        return hotBulletCoolDown;
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
        StoreActionHandle.doImpact = doImpact;
    }

    public static void setThreeBullet(boolean threeBullet) {
        StoreActionHandle.threeBullet = threeBullet;
    }

    public static void setAddHp(boolean addHp) {
        StoreActionHandle.addHp = addHp;
    }

    public static boolean isBanishHovering() {
        return banishHovering;
    }

    public static void setBanishHovering(boolean banishHovering) {
        StoreActionHandle.banishHovering = banishHovering;
    }

    public static boolean isFreezing() {
        return freezing;
    }

    public static void setFreezing(boolean freezing) {
        StoreActionHandle.freezing = freezing;
    }

    public static boolean isHotBullet() {
        return hotBullet;
    }

    public static void setHotBullet(boolean hotBullet) {
        StoreActionHandle.hotBullet = hotBullet;
    }
}
