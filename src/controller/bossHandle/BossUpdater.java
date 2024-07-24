package controller.bossHandle;

import controller.Controller;
import controller.Pair;
import controller.Utils;
import controller.constant.Constants;
import controller.constant.GameValues;
import controller.handeler.SkillTreeHandled;
import controller.handeler.StoreActionHandle;
import controller.handeler.TypedActionHandle;
import model.bulletModel.NonRigidBulletModel;
import model.bulletModel.RigidBulletModel;
import model.charactersModel.*;
import model.charactersModel.boss.BossHead;
import model.charactersModel.boss.BossLeftHand;
import model.charactersModel.boss.BossPunch;
import model.charactersModel.boss.BossRightHand;
import model.collision.Collidable;
import model.collision.CollisionPoint;
import model.movement.Direction;
import model.movement.ImpactMechanism;
import model.movement.Movable;
import model.panelModel.PanelModel;
import model.panelModel.WallSideIndicator;
import view.bulletView.BulletView;
import view.bulletView.EnemyNonRigidBulletView;
import view.charecterViews.EpsilonView;
import view.charecterViews.bossView.BossHeadView;
import view.charecterViews.bossView.BossLeftHandView;
import view.charecterViews.bossView.BossPunchView;
import view.charecterViews.bossView.BossRightHandView;
import view.container.GamePanel;
import view.container.GlassFrame;
import view.container.InformationPanel;
import view.gameTimerView.GameTimer;

import javax.swing.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;
import static controller.constant.Constants.GAME_PANEL_END_SIZE;

public class BossUpdater {
    EpsilonModel epsilon;
    private Timer viewUpdater;
    private Timer modelUpdater;
    private final GameTimer gameTimer;
    private BossHandler bossHandler;
    private BossHead bossHead;
    private BossRightHand bossRightHand;
    private BossLeftHand bossLeftHand;
    private BossPunch bossPunch;
    private PanelModel epsilonPanel;
    private boolean wonGame = false;
    private boolean firstBossAttackFinished = false;
    private boolean secondBossAttackFinished = false;
    private boolean epsilonGoesBigger = false;
    private int epsilonGoesBiggerTime;

    public BossUpdater() {
        gameTimer = GlassFrame.getINSTANCE().getTimer();

        bossHandler = new BossHandler();
        bossHead = new BossHead(new Point2D.Double(GLASS_FRAME_DIMENSION.width / 2.0, BOSS_SIZE / 2.0), BOSS_SIZE);
        bossHandler.setBossHead(bossHead);
        epsilonPanel = PanelModel.panelModelList.get(0);

        viewUpdater = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()){{setCoalesce(true);}};
        viewUpdater.start();
        modelUpdater = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()){{setCoalesce(true);}};
        modelUpdater.start();
    }

    public void updateView() {
        updateEpsilonView();
        updateBossHeadView();
        updateBossRightHandView();
        updateBossLeftHandView();
        updateBossPunchView();
        updateBulletView();
        updateNonRigidBulletView();
        updatePanelView();
        for (GamePanel panel : GamePanel.gamePanelList) {
            panel.repaint();
        }
        GlassFrame.getINSTANCE().revalidate();
        GlassFrame.getINSTANCE().repaint();
        InformationPanel.getINSTANCE().repaint();
    }
    public void updateModel() {
        epsilon = EpsilonModel.epsilonModelsList.get(0);
        GameValues.waveLengthTime = gameTimer.getMiliSecond() - GameValues.waveStartTime;
        updateEpsilonModel();
        updateBoss();
        updatePanelModel();
        reduceHealthByVomit();
        SkillTreeHandled.addHpByTime();
        updateCollisionAndImpact();
        checkCollisionBulletAndPanels();
        moveAllModels();
        Controller.getINSTANCE().logic.checkGameOver();

        if (!GameValues.bossFightStart) {
            if (bossHead == null) return;
            Point2D dest = new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() / 2, epsilonPanel.getY() - BOSS_SIZE / 2);
            if (Utils.pointsApproxEqual(dest, bossHead.getHeadPanel().getCenter())) {
                bossHead.setDirection(new Direction(new Point2D.Double(0, 0)));
                bossHead.setSpeed(0);
                bossHead.getHeadPanel().setDirection(new Direction(new Point2D.Double(0, 0)));
                bossHead.getHeadPanel().setSpeed(0);
                bossLeftHand = new BossLeftHand(new Point2D.Double(bossHead.getHeadPanel().getCenter().getX() - BOSS_SIZE, bossHead.getHeadPanel().getCenter().getY()), BOSS_SIZE);
                bossHandler.setBossLeftHand(bossLeftHand);
                bossRightHand = new BossRightHand(new Point2D.Double(bossHead.getHeadPanel().getCenter().getX() + BOSS_SIZE, bossHead.getHeadPanel().getCenter().getY()), BOSS_SIZE);
                bossHandler.setBossRightHand(bossRightHand);
                GameValues.bossFightStart = true;
            }
            else {
                Point2D delta = new Point2D.Double(dest.getX() - bossHead.getHeadPanel().getCenter().getX(), dest.getY() - bossHead.getHeadPanel().getCenter().getY());
                Direction toPoint = new Direction(delta);
                bossHead.setDirection(new Direction(toPoint.getDirectionVector()));
                bossHead.setSpeed(2);
                bossHead.getHeadPanel().setDirection(new Direction(toPoint.getDirectionVector()));
                bossHead.getHeadPanel().setSpeed(2);
            }
        }
        if (secondBossAttackFinished) {
            if (bossHead == null) return;
            PanelModel headPanel = bossHead.getHeadPanel();
            headPanel.endGameShrinkValue();
            headPanel.shrink(true);
            if ((headPanel.getWidth() <= GAME_PANEL_END_SIZE.width && headPanel.getHeight() <= GAME_PANEL_END_SIZE.height)) {
                removeHead();
                epsilonGoesBigger = true;
                epsilonGoesBiggerTime = gameTimer.getSeconds();
            }
        }
    }

    private void updateBoss() {
        bossHandler.setBossHead(bossHead);
        bossHandler.setBossLeftHand(bossLeftHand);
        bossHandler.setBossRightHand(bossRightHand);
        bossHandler.setBossPunch(bossPunch);

        if (GameValues.bossFightStart && !secondBossAttackFinished) {
            bossHandler.updateModels(gameTimer.getMiliSecond());
            bossHandler.makeAttack(gameTimer.getMiliSecond(), firstBossAttackFinished);
            bossHandler.updateAttack(gameTimer.getMiliSecond(), epsilon.getCenter(), epsilonPanel);
        }

        bossHead = bossHandler.getBossHead();
        bossLeftHand = bossHandler.getBossLeftHand();
        bossRightHand = bossHandler.getBossRightHand();
        bossPunch = bossHandler.getBossPunch();
    }
    private void updateEpsilonModel() {
        epsilon.updateVertices();
        epsilon.updateMainPanels(PanelModel.panelModelList);

        if (epsilonGoesBigger) {
            PanelModel firstMainPanel = PanelModel.panelModelList.get(0);
            double addRate = Math.max(firstMainPanel.getHeight(), firstMainPanel.getWidth()) / 75;
            epsilon.setRadius(epsilon.getRadius() + addRate);

            Point2D lst = epsilon.getCenter();
            epsilon.setCenter(new Point2D.Double(lst.getX() - addRate / 3, lst.getY() - addRate / 3));

            if (Utils.unionPanels(PanelModel.panelModelList, epsilon.getCenter(), epsilon.getRadius()).isEmpty()  || gameTimer.getSeconds() - epsilonGoesBiggerTime >= 10) {
                wonGame = true;
            }
            return;
        }
        TypedActionHandle.doMove();
        epsilon.adjustLocation(PanelModel.panelModelList);
        if (epsilon.getSpeed() > 0) {
            epsilon.setSpeed(epsilon.getSpeed() - (epsilon.getSpeed() / 10));
        }
    }
    private void reduceHealthByVomit() {
        if (bossHead == null) return;
        ArrayList<Pair<EpsilonModel, Integer>> tmp = bossHead.getEpsilonsInsideAoe();
        for (int i = 0; i < tmp.size(); i++) {
            if (gameTimer.getMiliSecond() - tmp.get(i).getSecond() >= 1000) {
                epsilonHealthReduction(tmp.get(i).getFirst(), BOSS_AOE_REDUCE_HP);
                tmp.remove(i--);
            }
        }
    }
    private void updatePanelModel() {
        for (PanelModel panel : PanelModel.panelModelList) {
            if (wonGame) {
                panel.endGameShrinkValue();
                if ((panel.getWidth() <= GAME_PANEL_END_SIZE.width && panel.getHeight() <= GAME_PANEL_END_SIZE.height)) {
                    Controller.getINSTANCE().logic.showFinishGame();
                    return;
                }
            }

            else if (GameValues.bossFightStart && gameTimer.getMiliSecond() - GameValues.waveStartTime >= 3000) {
                panel.inGameShrinkValue();
            }
            panel.shrink(wonGame);
            panel.updateCenter();
        }
    }
    private void updateCollisionAndImpact() {
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            Collidable first = Collidable.collidables.get(i);
            for (int j = 0; j < Collidable.collidables.size(); j++) {
                if (i == j) continue;
                Collidable second = Collidable.collidables.get(j);
                CollisionPoint tmp = first.collides(second);
                Point2D point = null;
                if (tmp != null) point = tmp.getCollisionPoint();

                if (point != null) {
                    double impactLevel = 0;
                    if (first instanceof EpsilonModel && (second instanceof BossRightHand || second instanceof BossLeftHand || second instanceof BossPunch)) {
                        epsilonHealthReduction((EpsilonModel) first, BOSS_REDUCE_HP);
                        impactLevel = 5;
                    }
                    else if (first instanceof EpsilonModel && second instanceof NonRigidBulletModel) {
                        epsilonHealthReduction((EpsilonModel) first, ((NonRigidBulletModel) second).getReduceHp());
                        NonRigidBulletModel.removeFromAllList(second.getId());
                        impactLevel = 5;
                    }
                    else if (first instanceof RigidBulletModel && second instanceof BossHead) {
                        if (!((BossHead) second).isCanInjure()) {
                            RigidBulletModel.removeFromAllList(first.getId());
                            continue;
                        }
                        ((BossHead) second).setHp(((BossHead) second).getHp() - ((RigidBulletModel) first).getReduceHp());
                        if (((BossHead) second).getHp() <= 0) {
                            secondBossAttackFinished = true;
                            removeLeftHand();
                            removeRightHand();
                            removePunch();
                            ((BossHead) second).setDead(true);
                            epsilon.setXp(epsilon.getXp() + BOSS_ADDED_XP);
                        }
                        else if (((BossHead) second).getHp() < (2 * BOSS_HEAD_HP) / 3) {
                            bossPunch = new BossPunch(new Point2D.Double(GLASS_FRAME_DIMENSION.width / 2.0, GLASS_FRAME_DIMENSION.height - BOSS_SIZE / 2.0), BOSS_SIZE);
                            bossHandler.setBossPunch(bossPunch);
                            firstBossAttackFinished = true;
                        }
                        RigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 5;
                    }
                    else if (first instanceof RigidBulletModel && (second instanceof BossLeftHand || second instanceof BossRightHand || second instanceof BossPunch)) {
                        if (second instanceof BossLeftHand) {
                            if (!((BossLeftHand) second).isCanInjure()) {
                                RigidBulletModel.removeFromAllList(first.getId());
                                continue;
                            }
                            ((BossLeftHand) second).setHp(((BossLeftHand) second).getHp() - ((RigidBulletModel) first).getReduceHp());
                            if (((BossLeftHand) second).getHp() <= 0) {
                                removeLeftHand();
                            }
                        }
                        else if (second instanceof BossRightHand) {
                            if (!((BossRightHand) second).isCanInjure()) {
                                RigidBulletModel.removeFromAllList(first.getId());
                                continue;
                            }
                            ((BossRightHand) second).setHp(((BossRightHand) second).getHp() - ((RigidBulletModel) first).getReduceHp());
                            if (((BossRightHand) second).getHp() <= 0) {
                                removeRightHand();
                            }
                        }
                        impactLevel = 5;
                        RigidBulletModel.removeFromAllList(first.getId());
                    }
                    else impactLevel = 2;
                    ImpactMechanism.applyImpact(point, impactLevel);
                }
            }
        }
    }
    private void epsilonHealthReduction(EpsilonModel epsilon, int reduceHp) {
        if (reduceHp != 0) injured.play();
        epsilon.setHp(epsilon.getHp() - reduceHp);
    }
    private void checkCollisionBulletAndPanels() {
        for (int i = 0; i < RigidBulletModel.rigidBulletModelList.size(); i++) {
            RigidBulletModel ptr = RigidBulletModel.rigidBulletModelList.get(i);
            ArrayList<PanelModel> coveredPanels = ptr.hasCollisions(PanelModel.panelModelList);

            if (coveredPanels == null) return;

            for (PanelModel panel : coveredPanels) {
                if (ptr.getCenter().getX() + ptr.getRadius() >= panel.getX() + panel.getWidth()) {
                    panel.bulletHit(WallSideIndicator.RIGHT);
                }
                if (ptr.getCenter().getX() - ptr.getRadius() <= panel.getX()) {
                    panel.bulletHit(WallSideIndicator.LEFT);
                }
                if (ptr.getCenter().getY() + ptr.getRadius() >= panel.getY() + panel.getHeight()) {
                    panel.bulletHit(WallSideIndicator.DOWN);
                }
                if (ptr.getCenter().getY() - ptr.getRadius() <= panel.getY()) {
                    panel.bulletHit(WallSideIndicator.UP);
                }
            }

            RigidBulletModel.removeFromAllList(ptr.getId());
        }
    }
    private void moveAllModels() {
        for (Movable ptr : Movable.movable) {
            if (StoreActionHandle.isFreezing()) {
                if (gameTimer.getSeconds() - StoreActionHandle.getStartFreezing() < 10) {
                    if (!(ptr instanceof RigidBulletModel && ((RigidBulletModel) ptr).getShooterEntity().equals(epsilon.getId()))){
                        continue;
                    }
                }
                else StoreActionHandle.setFreezing(false);
            }

            if (StoreActionHandle.isBanishHovering()) {
                if (gameTimer.getSeconds() - StoreActionHandle.getStartBanish() < 10) {
                    if (ptr instanceof Enemy && !((Enemy) ptr).isHovering()) continue;
                }
                else StoreActionHandle.setBanishHovering(false);
            }

            if (ptr instanceof PanelModel && ((PanelModel) ptr).collidesWithOtherPanel()) continue;

            ptr.move();
        }
    }

    private void removeRightHand() {
        if (bossRightHand == null) return;
        bossHandler.setBossRightHand(null);
        BossRightHand.removeFromAllList(bossRightHand.getId());
        bossRightHand.removePanel();
        bossRightHand = null;
    }
    private void removeLeftHand() {
        if (bossLeftHand == null) return;
        bossHandler.setBossLeftHand(null);
        BossLeftHand.removeFromAllList(bossLeftHand.getId());
        bossLeftHand.removePanel();
        bossLeftHand = null;
    }
    private void removeHead() {
        if (bossHead == null) return;
        bossHandler.setBossHead(null);
        BossHead.removeFromAllList(bossHead.getId());
        bossHead.removePanel();
        bossHead = null;
    }
    private void removePunch() {
        if (bossPunch == null) return;
        bossHandler.setBossPunch(null);
        BossPunch.removeFromAllList(bossPunch.getId());
        bossPunch.removePanel();
        bossPunch = null;
    }

    /// update View
    private void updateEpsilonView() {
        for (EpsilonView ptr : EpsilonView.epsilonViewsList) {
            EpsilonModel tmp = Controller.getINSTANCE().logic.findEpsilonModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentXp(tmp.getXp());
            ptr.setCurrentHp(tmp.getHp());
            ptr.setCurrentVertices(tmp.getVertices());
            ptr.setCurrentRadius(tmp.getRadius());
        }
    }
    private void updateBulletView() {
        for (BulletView ptr : BulletView.bulletViewList) {
            RigidBulletModel tmp = Controller.getINSTANCE().logic.findBulletModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentRadius(tmp.getRadius());
        }
    }
    private void updateNonRigidBulletView() {
        for (EnemyNonRigidBulletView ptr : EnemyNonRigidBulletView.nonRigidBulletViewsList) {
            NonRigidBulletModel tmp = Controller.getINSTANCE().logic.findNonRigidBullet(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentRadius(tmp.getRadius());
        }
    }
    private void updatePanelView() {
        for (GamePanel ptr : GamePanel.gamePanelList) {
            PanelModel tmp = Controller.getINSTANCE().logic.findPanelModel(ptr.getId());
            ptr.setUpLeftX(tmp.getX());
            ptr.setUpLeftY(tmp.getY());
            ptr.setWidth(tmp.getWidth());
            ptr.setHeight(tmp.getHeight());
            ptr.updateBound();
        }
    }
    private void updateBossHeadView() {
        for (BossHeadView ptr : BossHeadView.bossHeadViewsList) {
            BossHead tmp = Controller.getINSTANCE().logic.findBossHeadModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setDead(tmp.isDead());
            ptr.setCurrentSize(tmp.getSize());
            ptr.setCurrentAoeCenters(tmp.getAoeCenters());
        }
    }
    private void updateBossRightHandView() {
        for (BossRightHandView ptr : BossRightHandView.bossRightHandViewsList) {
            BossRightHand tmp = Controller.getINSTANCE().logic.findBossRightHandModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateBossLeftHandView() {
        for (BossLeftHandView ptr : BossLeftHandView.bossLeftHandViewsList) {
            BossLeftHand tmp = Controller.getINSTANCE().logic.findBossLeftHandModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateBossPunchView() {
        for (BossPunchView ptr : BossPunchView.bossPunchViewsList) {
            BossPunch tmp = Controller.getINSTANCE().logic.findBossPunchModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }


    public Timer getViewUpdater() {
        return viewUpdater;
    }

    public Timer getModelUpdater() {
        return modelUpdater;
    }
}
