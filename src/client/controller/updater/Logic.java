package client.controller.updater;

import client.controller.bossHandle.BossUpdater;
import client.controller.constant.Constants;
import client.controller.constant.GameValues;
import client.controller.handeler.SkillTreeHandled;
import client.controller.handeler.StoreActionHandle;
import client.controller.handeler.TypedActionHandle;
import client.controller.random.RandomHelper;
import client.controller.saveAndLoad.FileManager;
import client.model.bulletModel.RigidBulletModel;
import client.model.bulletModel.NonRigidBulletModel;
import client.model.charactersModel.*;
import client.model.charactersModel.boss.BossHead;
import client.model.charactersModel.boss.BossLeftHand;
import client.model.charactersModel.boss.BossPunch;
import client.model.charactersModel.boss.BossRightHand;
import client.model.checkPointModel.CheckPointModel;
import client.model.collectibleModel.Collectible;
import client.model.collision.Collidable;
import client.model.movement.Movable;
import client.model.panelModel.Isometric;
import client.model.panelModel.PanelModel;
import client.model.panelModel.Rigid;
import client.view.bulletView.BulletView;
import client.view.bulletView.EnemyNonRigidBulletView;
import client.view.charecterViews.*;
import client.view.charecterViews.bossView.BossHeadView;
import client.view.charecterViews.bossView.BossLeftHandView;
import client.view.charecterViews.bossView.BossPunchView;
import client.view.charecterViews.bossView.BossRightHandView;
import client.view.checkPointView.CheckPointView;
import client.view.collectibleView.CollectibleView;
import client.view.container.FinishPanel;
import client.view.container.GamePanel;
import client.view.container.GlassFrame;
import client.view.container.InformationPanel;
import org.reflections.Reflections;

import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import static client.controller.constant.Constants.EPSILON_RADIUS;

public class Logic {
    private EpsilonModel epsilon;
    private BossUpdater bossUpdater;
    private ArrayList<Class<? extends Enemy>> enemyClasses;
    public Logic (boolean load) {
        if (!load) createInitialPanel();
        else {
            FileManager.loadGame(false);
            epsilon = EpsilonModel.epsilonModelsList.get(0);
        }
        GameValues.canSpawn = false;
        initiateEnemyClasses();
    }
    private void initiateEnemyClasses() {
        Reflections reflections = new Reflections("client.model.charactersModel");
        Set<Class<? extends Enemy>> subTypes = reflections.getSubTypesOf(Enemy.class);
        enemyClasses = new ArrayList<>(subTypes);
    }
    private void createEpsilon(double panelX, double panelY) {
        epsilon = new EpsilonModel(new Point2D.Double(EPSILON_RADIUS + panelX, EPSILON_RADIUS + panelY), Utils.processRandomId());
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
        if (GameValues.waveNumber + 1 < 4) {
            GameValues.totalProgressTime += (GameValues.waveNumber * GameValues.waveLengthTime / 1000);
            GameValues.waveNumber++;
        }

        ArrayList<Point2D> enemiesCenter = RandomHelper.randomFirstWaveEnemyCenters(x, y, width, height);
        for (Point2D center : enemiesCenter) {
            int pointer = RandomHelper.randomFirstWaveEnemyPointer(enemyClasses);

            Class<? extends Enemy> enemy = enemyClasses.get(pointer);

            Constructor<?> constructor = enemy.getConstructors()[0];
            Object[] init = new Object[3];
            init[0] = center;
            init[1] = RandomHelper.randomWaveEnemySize(enemy);
            init[2] = RandomHelper.randomWaveEnemySpeed(enemy);
            try {
                constructor.newInstance(init);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void doSpawn() {
        ArrayList<Pair<Point2D, Integer>> toSpawn = RandomHelper.randomSecondWaveEnemyCentersAndPointer(enemyClasses);

        for (Pair<Point2D, Integer> centerAndPointer : toSpawn) {
            Class<? extends Enemy> enemy = enemyClasses.get(centerAndPointer.getSecond());

            if (enemy.equals(OrbEnemy.class)) {
                new BlackOrbMiniBoss(centerAndPointer.getFirst(), RandomHelper.randomWaveEnemySize(OrbEnemy.class));
            }
            else {
                Constructor<?> constructor = enemy.getConstructors()[0];
                Object[] init = new Object[3];
                init[0] = centerAndPointer.getFirst();
                init[1] = RandomHelper.randomWaveEnemySize(enemy);
                init[2] = RandomHelper.randomWaveEnemySpeed(enemy);
                try {
                    constructor.newInstance(init);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void updateSecondRoundWave(int time) {
        if (Enemy.enemiesList.isEmpty()) {
            if (GameValues.waveNumber < 9) {
                GameValues.totalProgressTime += (GameValues.waveNumber * GameValues.waveLengthTime / 1000);
                GameValues.waveNumber++;
            }
            GameValues.canSpawn = true;
            GameValues.waveStartTime = time;
            GameValues.temporaryEnemyKilledNumber = 0;
            doSpawn();
        }
    }
    public void updateSpawn(int time) {
        if (GameValues.temporaryEnemyKilledNumber >= Utils.getMinimumKilled(GameValues.level)) {
            GameValues.canSpawn = false;
        }

        if (!GameValues.canSpawn) return;

        if (time - GameValues.lastAttackUpdate >= 5000) {
            GameValues.lastAttackUpdate = time;
            doSpawn();
        }
    }

    public void updateLocalSave(int time) {
        if (time - GameValues.lastSaveTime > 1000 || GameValues.lastSaveTime == 0) {
            FileManager.saveGame(false);
            GameValues.lastSaveTime = time;
        }
    }
    public void updateCheckPointMaking(int time, double x, double y, double width, double height) {
        if (time - GameValues.lastCheckPointMade >= 20000 || GameValues.lastCheckPointMade == 0) {
            new CheckPointModel(RandomHelper.makeCheckPointCenter(x, y, width, height));
            GameValues.lastCheckPointMade = time;
        }
    }

    public void checkGameOver() {
        if (epsilon.getHp() <= 0) {
            showFinishGame();
        }
    }
    public void showFinishGame() {
        if (epsilon.getHp() <= 0) {
            if (FileManager.canLoad(true)) {
                deleteAllInfo(true, true);
                FileManager.loadGame(true);
                Controller.getINSTANCE().updater.modelUpdater.start();
                Controller.getINSTANCE().updater.viewUpdater.start();
            }
            else {
                Constants.gameOver.play();
                int finishXP = epsilon.getXp();
                int bulletFired = GameValues.bulletFired;
                int successfulBullet = GameValues.successfulBullet;
                int enemyKilled = GameValues.enemyKilled;
                String totalTime = GlassFrame.getINSTANCE().getTimer().toString();
                deleteAllInfo(true, false);
                new FinishPanel(finishXP, bulletFired, successfulBullet, enemyKilled, totalTime);
            }
        }
        else if (!GameValues.firstRoundFinish) {
            Constants.INITIAL_HP = epsilon.getHp();
            GameValues.firstRoundFinish = true;
            deleteAllInfo(false, false);
            GameValues.waveNumber++;
            createInitialPanel();
        }
        else if (!GameValues.secondRoundFinish) {
            Controller.getINSTANCE().updater.modelUpdater.stop();
            Controller.getINSTANCE().updater.viewUpdater.stop();

            RigidBulletModel.rigidBulletModelList.clear();
            NonRigidBulletModel.nonRigidBulletModelsList.clear();
            Collectible.collectibleList.clear();
            CheckPointModel.checkPointModelsList.clear();

            BulletView.bulletViewList.clear();
            EnemyNonRigidBulletView.nonRigidBulletViewsList.clear();
            CollectibleView.collectibleViewList.clear();
            CheckPointView.checkPointViewsList.clear();


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
            deleteAllInfo(true, false);
            new FinishPanel(finishXP, bulletFired, successfulBullet, enemyKilled, totalTime);
        }
    }
    private void deleteAllInfo(boolean end, boolean checkPoint) {
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
            GameValues.lastAttackUpdate = 0;
            GameValues.lastSaveTime = 0;
            GameValues.canSpawn = false;
            GameValues.lastCheckPointMade = 0;
            GameValues.totalProgressTime = 0;
            GameValues.firstRoundFinish = false;
            GameValues.secondRoundFinish = false;
            GameValues.temporaryEnemyKilledNumber = 0;
            GameValues.bossFightStart = false;
            GameValues.waveLengthTime = 0;
            GameValues.waveStartTime = 0;
            SkillTreeHandled.makeAllRestart();
            if (!checkPoint) {
                GlassFrame.getINSTANCE().remove(InformationPanel.getINSTANCE());
                InformationPanel.setINSTANCE(null);
            }
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
        CheckPointModel.checkPointModelsList.clear();
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
        CheckPointView.checkPointViewsList.clear();

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
    public CheckPointModel findCheckPointModel(String id) {
        for (CheckPointModel ptr : CheckPointModel.checkPointModelsList) {
            if (ptr.getId().equals(id)) return ptr;
        }
        return null;
    }

    public BossUpdater getBossUpdater() {
        return bossUpdater;
    }

    public void setEpsilon(EpsilonModel epsilon) {
        this.epsilon = epsilon;
    }
}
