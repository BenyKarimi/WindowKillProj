package controller;

import controller.bossHandle.BossUpdater;
import controller.constant.Constants;
import controller.constant.GameValues;
import controller.handeler.SkillTreeHandled;
import controller.handeler.StoreActionHandle;
import controller.handeler.TypedActionHandle;
import controller.random.RandomHelper;
import model.bulletModel.RigidBulletModel;
import model.bulletModel.NonRigidBulletModel;
import model.charactersModel.*;
import model.charactersModel.boss.BossHead;
import model.charactersModel.boss.BossLeftHand;
import model.charactersModel.boss.BossPunch;
import model.charactersModel.boss.BossRightHand;
import model.collectibleModel.Collectible;
import model.collision.Collidable;
import model.movement.Movable;
import model.panelModel.Isometric;
import model.panelModel.PanelModel;
import model.panelModel.Rigid;
import view.bulletView.BulletView;
import view.bulletView.EnemyNonRigidBulletView;
import view.charecterViews.*;
import view.charecterViews.bossView.BossHeadView;
import view.charecterViews.bossView.BossLeftHandView;
import view.charecterViews.bossView.BossPunchView;
import view.charecterViews.bossView.BossRightHandView;
import view.collectibleView.CollectibleView;
import view.container.FinishPanel;
import view.container.GamePanel;
import view.container.GlassFrame;
import view.container.InformationPanel;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.EPSILON_RADIUS;

public class Logic {
    private int lastAttackUpdate;
    private boolean canSpawn;
    public EpsilonModel epsilon;
    private BossUpdater bossUpdater;
    public Logic() {
        createInitialPanel();
        canSpawn = false;
//        makeThread();
    }
    private void createEpsilon(double panelX, double panelY) {
        epsilon = new EpsilonModel(new Point2D.Double(EPSILON_RADIUS + panelX, EPSILON_RADIUS + panelY));
    }
    private void createInitialPanel() {
        PanelModel panelModel = new PanelModel(Constants.GAME_PANEL_INITIAL_DIMENSION, Isometric.NO, Rigid.NO);
        if (!GameValues.firstRoundFinish) {
            InformationPanel.getINSTANCE();
            GlassFrame.getINSTANCE().getTimer().Reset();
            GlassFrame.getINSTANCE().getTimer().Start();
        }
        createEpsilon(panelModel.getX(), panelModel.getY());
    }
    public void createCollectible(int collectibleNumber, int collectibleXp, Point2D enemyCenter, double size) {
        ArrayList<Point2D> centers = Utils.circlePartition(enemyCenter, size / 2, collectibleNumber);

        for (Point2D ptr : centers) {
            new Collectible(ptr, collectibleXp);
        }
    }
    public boolean checkCanMakeWave() {
        return Enemy.enemiesList.isEmpty();
    }
    public void makeFirstRoundWave(double x, double y, double width, double height) {
        if (GameValues.waveNumber + 1 < 4) GameValues.waveNumber++;
        ArrayList<Point2D> enemiesCenter = RandomHelper.randomFirstWaveEnemyCenters(x, y, width, height);
        for (Point2D ptr : enemiesCenter) {
            if (RandomHelper.randomFirstWaveEnemyType() == 0) {
                if (SquareEnemy.squareEnemyList.isEmpty()) new SquareEnemy(ptr, RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else {
                new TriangleEnemy(ptr, RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
        }
    }
    private void doSpawn() {
        ArrayList<Pair<Point2D, Integer>> toSpawn = RandomHelper.randomSecondWaveEnemyCentersAndType();
//        System.out.println(toSpawn.size());

        for (Pair<Point2D, Integer> centerAndTime : toSpawn) {
            if (centerAndTime.getSecond() == 1) {
                new SquareEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else if (centerAndTime.getSecond() == 2) {
                new TriangleEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else if (centerAndTime.getSecond() == 3) {
                new OmenoctEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed() * 1.5, RandomHelper.omenoctWallSide());
            }
            else if (centerAndTime.getSecond() == 4) {
                new NecropickEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else if (centerAndTime.getSecond() == 5) {
                new ArchmireEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else if (centerAndTime.getSecond() == 6) {
                new WyrmEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else if (centerAndTime.getSecond() == 7) {
                new BarricadosEnemy(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize() * 1.5, RandomHelper.barricadosType(), GlassFrame.getINSTANCE().getTimer().getMiliSecond());
            }
            else if (centerAndTime.getSecond() == 8) {
                new BlackOrbMiniBoss(centerAndTime.getFirst(), RandomHelper.randomWaveEnemySize());
            }
        }
    }
    public void updateSecondRoundWave(int time) {
        if (Enemy.enemiesList.isEmpty()) {
            canSpawn = true;
            GameValues.waveStartTime = time;
            GameValues.temporaryEnemyKilledNumber = 0;
            doSpawn();
            if (GameValues.waveNumber < 9) GameValues.waveNumber++;
        }
    }
    public void updateSpawn(int time) {
        if (GameValues.temporaryEnemyKilledNumber >= Utils.getMinimumKilled(GameValues.level)) {
            canSpawn = false;
        }

        if (canSpawn && time - lastAttackUpdate >= 5000) {
            lastAttackUpdate = time;
            doSpawn();
        }
    }

    public void checkGameOver() {
        if (epsilon.getHp() <= 0) {
            showFinishGame();
        }
    }
    public void showFinishGame() {
        if (epsilon.getHp() <= 0) {
            Constants.gameOver.play();
            int finishXP = epsilon.getXp();
            int bulletFired = GameValues.bulletFired;
            int successfulBullet = GameValues.successfulBullet;
            int enemyKilled = GameValues.enemyKilled;
            String totalTime = GlassFrame.getINSTANCE().getTimer().toString();
            deleteAllInfo(true);
            new FinishPanel(finishXP, bulletFired, successfulBullet, enemyKilled, totalTime);
        }
        else if (!GameValues.firstRoundFinish) {
            Constants.INITIAL_HP = epsilon.getHp();
            GameValues.firstRoundFinish = true;
            deleteAllInfo(false);
            GameValues.waveNumber += 2;
            createInitialPanel();
        }
        else if (!GameValues.secondRoundFinish) {
            Controller.getINSTANCE().updater.modelUpdater.stop();
            Controller.getINSTANCE().updater.viewUpdater.stop();

            RigidBulletModel.rigidBulletModelList.clear();
            NonRigidBulletModel.nonRigidBulletModelsList.clear();
            Collectible.collectibleList.clear();

            BulletView.bulletViewList.clear();
            EnemyNonRigidBulletView.nonRigidBulletViewsList.clear();
            CollectibleView.collectibleViewList.clear();


            GameValues.secondRoundFinish = true;
            GameValues.waveNumber++;
            GameValues.waveStartTime = GlassFrame.getINSTANCE().getTimer().getMiliSecond();
            bossUpdater = new BossUpdater();
        }
        else {
            Constants.winGame.play();
            int finishXP = epsilon.getXp();
            int bulletFired = GameValues.bulletFired;
            int successfulBullet = GameValues.successfulBullet;
            int enemyKilled = GameValues.enemyKilled;
            String totalTime = GlassFrame.getINSTANCE().getTimer().toString();
            deleteAllInfo(true);
            new FinishPanel(finishXP, bulletFired, successfulBullet, enemyKilled, totalTime);
        }
    }
    private void deleteAllInfo(boolean end) {
        Constants.INITIAL_XP = epsilon.getXp();
        if (GameValues.secondRoundFinish) {
            bossUpdater.getModelUpdater().stop();
            bossUpdater.getViewUpdater().stop();
        }
        if (end) {
            Controller.getINSTANCE().updater.modelUpdater.stop();
            Controller.getINSTANCE().updater.viewUpdater.stop();
            epsilon.setVerticesNumber(0);
            Constants.EPSILON_REDUCE_HP = 10;
            Constants.BULLET_REDUCE_HP = 5;
            GameValues.waveNumber = 0;
            GameValues.bulletFired = 0;
            GameValues.successfulBullet = 0;
            GameValues.enemyKilled = 0;
            GameValues.firstRoundFinish = false;
            GameValues.secondRoundFinish = false;
            GameValues.temporaryEnemyKilledNumber = 0;
            GameValues.bossFightStart = false;
            GameValues.waveLengthTime = 0;
            GameValues.waveStartTime = 0;
            SkillTreeHandled.makeAllRestart();
            GlassFrame.getINSTANCE().remove(InformationPanel.getINSTANCE());
            InformationPanel.setINSTANCE(null);
            GameValues.firstRoundFinish = false;
        }
        for (GamePanel panel : GamePanel.gamePanelList) {
            GlassFrame.getINSTANCE().remove(panel);
        }
        GlassFrame.getINSTANCE().revalidate();
        GlassFrame.getINSTANCE().repaint();
        PanelModel.panelModelList.clear();
        GamePanel.gamePanelList.clear();
        EpsilonModel.epsilonModelsList.clear();
        RigidBulletModel.rigidBulletModelList.clear();
        NonRigidBulletModel.nonRigidBulletModelsList.clear();
        Enemy.enemiesList.clear();
        SquareEnemy.squareEnemyList.clear();
        TriangleEnemy.triangleEnemyList.clear();
        OmenoctEnemy.omenoctEnemyList.clear();
        NecropickEnemy.necropickEnemiesList.clear();
        ArchmireEnemy.archmireEnemiesList.clear();
        WyrmEnemy.wyrmEnemiesList.clear();
        BlackOrbMiniBoss.blackOrbMiniBossesList.clear();
        BarricadosEnemy.barricadosEnemiesList.clear();
        OrbEnemy.orbEnemiesList.clear();
        BossHead.bossHeadsList.clear();
        BossLeftHand.bossLeftHandsList.clear();
        BossRightHand.bossRightHandsList.clear();
        BossPunch.bossPunchesList.clear();
        Collectible.collectibleList.clear();

        BulletView.bulletViewList.clear();
        EnemyNonRigidBulletView.nonRigidBulletViewsList.clear();
        EpsilonView.epsilonViewsList.clear();
        SquareEnemyView.squareEnemyViewList.clear();
        TriangleEnemyView.triangleEnemyViewList.clear();
        OmenoctEnemyView.omenoctEnemyViewList.clear();
        NecropickEnemyView.necropickEnemyViewsList.clear();
        ArchmireEnemyView.archmireEnemyViewsList.clear();
        WyrmEnemyView.wyrmEnemyViewsList.clear();
        BlackOrbMiniBossView.blackOrbMiniBossViewsList.clear();
        BarricadosEnemyView.barricadosEnemyViewsList.clear();
        CollectibleView.collectibleViewList.clear();
        BossHeadView.bossHeadViewsList.clear();
        BossRightHandView.bossRightHandViewsList.clear();
        BossLeftHandView.bossLeftHandViewsList.clear();
        BossPunchView.bossPunchViewsList.clear();

        Collidable.collidables.clear();
        Movable.movable.clear();
        TypedActionHandle.setDown(false);
        TypedActionHandle.setLeft(false);
        TypedActionHandle.setUp(false);
        TypedActionHandle.setRight(false);
        StoreActionHandle.setThreeBullet(false);
        StoreActionHandle.setBanishHovering(false);
        StoreActionHandle.setFreezing(false);
        StoreActionHandle.setHotBullet(false);
    }
    public EpsilonModel findEpsilonModel(String id) {
        for (EpsilonModel ptr : EpsilonModel.epsilonModelsList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public TriangleEnemy findTriangleEnemyModel(String id) {
        for (TriangleEnemy ptr : TriangleEnemy.triangleEnemyList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public SquareEnemy findSquareEnemyModel(String id) {
        for (SquareEnemy ptr : SquareEnemy.squareEnemyList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public RigidBulletModel findBulletModel(String id) {
        for (RigidBulletModel ptr : RigidBulletModel.rigidBulletModelList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public Collectible findCollectibleModel(String id) {
        for (Collectible ptr : Collectible.collectibleList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public PanelModel findPanelModel(String id) {
        for (PanelModel ptr : PanelModel.panelModelList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public OmenoctEnemy findOmenoctEnemyModel(String id) {
        for (OmenoctEnemy ptr : OmenoctEnemy.omenoctEnemyList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public NecropickEnemy findNecropickEnemyModel(String id) {
        for (NecropickEnemy ptr : NecropickEnemy.necropickEnemiesList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public WyrmEnemy findWyrmEnemyModel(String id) {
        for (WyrmEnemy ptr : WyrmEnemy.wyrmEnemiesList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public BlackOrbMiniBoss findBlackOrbMiniBossModel(String id) {
        for (BlackOrbMiniBoss ptr : BlackOrbMiniBoss.blackOrbMiniBossesList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public BarricadosEnemy findBarricadosEnemyModel(String id) {
        for (BarricadosEnemy ptr : BarricadosEnemy.barricadosEnemiesList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public ArchmireEnemy findArchmireEnemyModel(String id) {
        for (ArchmireEnemy ptr : ArchmireEnemy.archmireEnemiesList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public NonRigidBulletModel findNonRigidBullet(String id) {
        for (NonRigidBulletModel ptr : NonRigidBulletModel.nonRigidBulletModelsList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public BossHead findBossHeadModel(String id) {
        for (BossHead ptr : BossHead.bossHeadsList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public BossRightHand findBossRightHandModel(String id) {
        for (BossRightHand ptr : BossRightHand.bossRightHandsList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public BossLeftHand findBossLeftHandModel(String id) {
        for (BossLeftHand ptr : BossLeftHand.bossLeftHandsList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }
    public BossPunch findBossPunchModel(String id) {
        for (BossPunch ptr : BossPunch.bossPunchesList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }

    public BossUpdater getBossUpdater() {
        return bossUpdater;
    }
}
