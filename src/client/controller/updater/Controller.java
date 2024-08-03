package client.controller.updater;


import client.view.bulletView.BulletView;
import client.view.bulletView.EnemyNonRigidBulletView;
import client.view.charecterViews.*;
import client.view.charecterViews.bossView.BossHeadView;
import client.view.charecterViews.bossView.BossLeftHandView;
import client.view.charecterViews.bossView.BossPunchView;
import client.view.charecterViews.bossView.BossRightHandView;
import client.view.checkPointView.CheckPointView;
import client.view.collectibleView.CollectibleView;
import client.view.container.GamePanel;

import java.awt.geom.Point2D;

public class Controller {
    private static Controller INSTANCE;
    public Updater updater;
    public Logic logic;
    public Controller(boolean load) {
        INSTANCE = this;
        logic = new Logic(load);
        updater = new Updater();
//        updater.epsilon = logic.epsilon;
    }
    public void createEpsilonView(String id, Point2D center) {
        new EpsilonView(id, center);
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
    public void createGamePanel(String id, double x, double y, double width, double height) {
        new GamePanel(id, x, y, width, height);
    }
    public void createEnemyNonRigidBulletView(String id) {
        new EnemyNonRigidBulletView(id);
    }
    public void createOmenoctEnemyView(String id) {
        new OmenoctEnemyView(id);
    }
    public void createNecropickEnemyView(String id) {
        new NecropickEnemyView(id);
    }
    public void createArchmireEnemyView(String id) {
        new ArchmireEnemyView(id);
    }
    public void createWyrmEnemyView(String id) {
        new WyrmEnemyView(id);
    }
    public void createBlackOrbMiniBossView(String id) {
        new BlackOrbMiniBossView(id);
    }
    public void createBarricadosEnemyView(String id) {
        new BarricadosEnemyView(id);
    }
    public void createBossHeadView(String id) {
        new BossHeadView(id);
    }
    public void createBossRightHandView(String id) {
        new BossRightHandView(id);
    }
    public void createBossLeftHandView(String id) {
        new BossLeftHandView(id);
    }
    public void createBossPunchView(String id) {
        new BossPunchView(id);
    }
    public void createCheckPointView(String id) {
        new CheckPointView(id);
    }
    public static Controller getINSTANCE() {
//        if (INSTANCE == null) INSTANCE = new Controller();
        return INSTANCE;
    }
    public static void setINSTANCE(Controller INSTANCE) {
        Controller.INSTANCE = INSTANCE;
    }
}
