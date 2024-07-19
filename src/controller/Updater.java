package controller;

import controller.constant.Constants;
import controller.constant.GameValues;
import controller.handeler.SkillTreeHandled;
import controller.handeler.TypedActionHandle;
import model.bulletModel.RigidBulletModel;
import model.bulletModel.NonRigidBulletModel;
import model.charactersModel.*;
import model.collectibleModel.Collectible;
import model.collision.Collidable;
import model.collision.CollisionPoint;
import model.movement.ImpactMechanism;
import model.movement.Movable;
import model.panelModel.PanelModel;
import model.panelModel.WallSideIndicator;
import view.bulletView.BulletView;
import view.bulletView.EnemyNonRigidBulletView;
import view.charecterViews.NecropickEnemyView;
import view.charecterViews.OmenoctEnemyView;
import view.charecterViews.SquareEnemyView;
import view.charecterViews.TriangleEnemyView;
import view.collectibleView.CollectibleView;
import view.container.GamePanel;
import view.container.GlassFrame;
import view.container.InformationPanel;
import view.gameTimerView.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class Updater {
    private boolean startGame = false;
    private boolean isWave = false;
    private boolean wonGame = false;
    private boolean epsilonGoesBigger = false;
    private int epsilonGoesBiggerTime;
    EpsilonModel epsilon;
    Timer viewUpdater;
    Timer modelUpdater;
    GameTimer gameTimer;
    int startTime, startWave;
    public Updater() {
        gameTimer = GlassFrame.getINSTANCE().getTimer();
        viewUpdater = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()){{setCoalesce(true);}};
        viewUpdater.start();
        modelUpdater = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()){{setCoalesce(true);}};
        modelUpdater.start();
    }
    public void updateView() {
        // update enemies and add boolean for is wave or not
        updateEpsilonView();
        updateTriangleEnemyView();
        updateSquareEnemyView();
        updateOmenoctEnemyView();
        updateNecropickEnemyView();
        updateCollectibleView();
        updateBulletView();
        updateNonRigidBulletView();
        updatePanelView();
        for (GamePanel panel : GamePanel.gamePanelList) {
            panel.repaint();
        }
        GlassFrame.getINSTANCE().repaint();
        InformationPanel.getINSTANCE().repaint();
    }
    public void updateModel() {
        updateEpsilonModel();
        updatePanelModel();
        SkillTreeHandled.addHpByTime();
        updateEnemiesModel();
        updateCollectibleModel();
        updateCollisionAndImpact();
        checkCollisionBulletAndPanels();
        moveAllModels();
        Controller.getINSTANCE().logic.checkGameOver();
        if (startGame && !epsilonGoesBigger) {
            if (Controller.getINSTANCE() == null) return;
            /// havaset bashe che gohi dari mikhori
            if (PanelModel.panelModelList.isEmpty()) return;
            PanelModel pm = PanelModel.panelModelList.get(0);
            isWave = Controller.getINSTANCE().logic.makeWave(pm.getX(), pm.getY(), pm.getWidth(), pm.getHeight());
            if (GameValues.waveNumber == 4 && !isWave) {
                epsilonGoesBigger = true;
                epsilonGoesBiggerTime = gameTimer.getSeconds();
                return;
            }
            if (isWave) {
                Constants.startWave.play();
                startWave = gameTimer.getSeconds();
            }
            isWave = false;
        }
    }
    /// model functions
    private void updateEpsilonModel() {
        epsilon.updateVertices();
        epsilon.updateMainPanels(PanelModel.panelModelList);

        if (epsilonGoesBigger) {
            PanelModel firstMainPanel = PanelModel.panelModelList.get(0);
            double addRate = Math.max(firstMainPanel.getHeight(), firstMainPanel.getWidth()) / 75;
            epsilon.setRadius(epsilon.getRadius() + addRate);

            Point2D lst = epsilon.getCenter();
            epsilon.setCenter(new Point2D.Double(lst.getX() - addRate / 3, lst.getY() - addRate / 3));

            if (Utils.unionPanels(PanelModel.panelModelList, epsilon.getCenter(), epsilon.getRadius()).isEmpty()) {
                wonGame = true;
            }

            return;
        }
        TypedActionHandle.doMove();
        epsilon.adjustLocation(PanelModel.panelModelList);
        if (epsilon.getSpeed() > 0) {
            epsilon.setSpeed(epsilon.getSpeed() - (epsilon.getSpeed() / EPSILON_REDUCE_HP));
        }
    }
    private void updatePanelModel() {
        for (PanelModel panel : PanelModel.panelModelList) {
            if (!startGame) {
                panel.startShrinkValue();
                if (panel.getWidth() <= Constants.GAME_PANEL_START_DIMENSION_ERROR.width) {
                    startGame = true;
                    startTime = gameTimer.getSeconds();
                    panel.setLeftSpeed(0);
                    panel.setRightSpeed(0);
                    panel.setUpSpeed(0);
                    panel.setDownSpeed(0);
                }
            }
            else if (wonGame) {
                panel.endGameShrinkValue();
                if ((panel.getWidth() <= GAME_PANEL_END_SIZE.width && panel.getHeight() <= GAME_PANEL_END_SIZE.height) || gameTimer.getSeconds() - epsilonGoesBiggerTime >= 10) {
                    Controller.getINSTANCE().logic.showFinishGame();
                    return;
                }
            }

            else if (gameTimer.getSeconds() - startTime >= 10 && gameTimer.getSeconds() - startWave >= 3) {
                panel.inGameShrinkValue();
            }
            panel.shrink(wonGame);

        }
    }
    private void updateEnemiesModel() {
        for (SquareEnemy ptr : SquareEnemy.squareEnemyList) {
            ptr.updateDirection(epsilon.getCenter());
            ptr.calculateVertices();
            ptr.updateSpeed();
        }
        for (TriangleEnemy ptr : TriangleEnemy.triangleEnemyList) {
            ptr.updateDirection(epsilon.getCenter());
            ptr.calculateVertices();
            ptr.updateSpeed(epsilon.getCenter());
        }
        for (OmenoctEnemy ptr : OmenoctEnemy.omenoctEnemyList) {
            ptr.updateDirection(ptr.findTargetPanel(epsilon.getMainPanels()));
            ptr.makeAttack(gameTimer.getSeconds(), epsilon.getCenter());
            ptr.calculateVertices();
        }
        for (NecropickEnemy ptr : NecropickEnemy.necropickEnemiesList) {
            ptr.updateStation(gameTimer.getMiliSecond());
            ptr.updateDirection(epsilon.getCenter());
            ptr.updateSpeed(epsilon.getCenter());
            ptr.calculateVertices();
        }
    }
    private void updateCollectibleModel() {
        int ptr = 0;
        Rectangle epsilonRect = new Rectangle((int)(epsilon.getCenter().getX() - epsilon.getRadius())
                , (int)(epsilon.getCenter().getY() - epsilon.getRadius())
                , (int)(2 * epsilon.getRadius()), (int)(2 * epsilon.getRadius()));

        while (ptr < Collectible.collectibleList.size()) {
            Collectible now = Collectible.collectibleList.get(ptr);
            boolean hasIntersect = epsilonRect.intersects(new Rectangle((int)(now.getCenter().getX() - (now.getSize()) / 2),
                    (int)(now.getCenter().getY() - (now.getSize()) / 2), (int)now.getSize(), (int)now.getSize()));
            now.setExistence(now.getExistence() - modelUpdater.getDelay());
            if (hasIntersect) {
                addCollectible.play();
                epsilon.setXp(epsilon.getXp() + now.getAddedXp());
                Collectible.removeFromAllList(Collectible.collectibleList.get(ptr).getId());
            }
            else {
                if (now.getExistence() <= 0) {
                    Collectible.removeFromAllList(Collectible.collectibleList.get(ptr).getId());
                }
                else ptr++;
            }
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
                    if (first instanceof NecropickEnemy && second instanceof NecropickEnemy) continue;
                    if (first instanceof EpsilonModel && second instanceof Enemy) {
                        boolean firstVer = false;
                        boolean secondVer = false;
                        for (Point2D ptr : first.getVertices()) {
                            if (Utils.ApproxEqual(ptr.getX(), point.getX()) && Utils.ApproxEqual(point.getY(), ptr.getY())) firstVer = true;
                        }
                        for (Point2D ptr : second.getVertices()) if (ptr.equals(point)) secondVer = true;
                        if (firstVer && !secondVer) {
                            enemyHealthReduction((Enemy) second, EPSILON_REDUCE_HP);
                        }
                        else if (secondVer && !firstVer) {
                            if (((Enemy) second).getReducerHp() != 0) injured.play();
                            epsilon.setHp(epsilon.getHp() - ((Enemy) second).getReducerHp());
                        }
                        impactLevel = 5;
                    }
                    else if ((first instanceof RigidBulletModel || first instanceof NonRigidBulletModel) && second instanceof Enemy) {
                        impactLevel = 5;
                        if (first instanceof RigidBulletModel && !second.getId().equals(((RigidBulletModel) first).getShooterEntity())) {
                            enemyHealthReduction((Enemy) second, ((RigidBulletModel) first).getReduceHp());
                            RigidBulletModel.removeFromAllList(first.getId());
                        }
                        else if (first instanceof NonRigidBulletModel && !second.getId().equals(((NonRigidBulletModel) first).getShooterEnemy())) {
                            enemyHealthReduction((Enemy) second, ((NonRigidBulletModel) first).getReduceHp());
                            NonRigidBulletModel.removeFromAllList(first.getId());
                        }
                        else impactLevel = 0;
                    }
                    else if (first instanceof EpsilonModel && (second instanceof NonRigidBulletModel || second instanceof RigidBulletModel)) {
                        if (second instanceof RigidBulletModel && ((RigidBulletModel) second).getShooterEntity().equals(first.getId())) continue;
                        injured.play();
                        if (second instanceof NonRigidBulletModel) {
                            epsilon.setHp(epsilon.getHp() - ((NonRigidBulletModel) second).getReduceHp());
                            NonRigidBulletModel.removeFromAllList(second.getId());
                        }
                        if (second instanceof RigidBulletModel) {
                            epsilon.setHp(epsilon.getHp() - ((RigidBulletModel) second).getReduceHp());
                            RigidBulletModel.removeFromAllList(second.getId());
                        }
                        impactLevel = 5;
                    }
                    else if (first instanceof RigidBulletModel && second instanceof NonRigidBulletModel) {
                        NonRigidBulletModel.removeFromAllList(second.getId());
                        RigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 2;
                    }
                    else if (first instanceof NonRigidBulletModel && second instanceof NonRigidBulletModel) {
                        NonRigidBulletModel.removeFromAllList(second.getId());
                        NonRigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 2;
                    }
                    else if (first instanceof RigidBulletModel && second instanceof RigidBulletModel) {
                        RigidBulletModel.removeFromAllList(second.getId());
                        RigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 2;
                    }
                    else impactLevel = 2;
                    if (impactLevel != 0) ImpactMechanism.applyImpact(point, impactLevel);
                }
            }
        }
    }
    private boolean enemyHealthReduction(Enemy enemy, int reduceHp) {
        enemy.setHp(enemy.getHp() - reduceHp);
        if (enemy.getHp() <= 0) {
            enemyDeath.play();
            Controller.getINSTANCE().logic.createCollectible(enemy.getCollectibleNumber(), enemy.getCollectibleXp(), enemy.getCenter(), enemy.getSize());
            if (enemy instanceof TriangleEnemy) TriangleEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof SquareEnemy) SquareEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof OmenoctEnemy) OmenoctEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof NecropickEnemy) NecropickEnemy.removeFromAllList(enemy.getId());
            return true;
        }
        return false;
    }
    private void omenoctWallHealthReduction(WallSideIndicator wallSideIndicator, double pl) {
        for (int i = 0; i < OmenoctEnemy.omenoctEnemyList.size(); i++) {
            OmenoctEnemy omenoct = OmenoctEnemy.omenoctEnemyList.get(i);
            if (omenoct.isConnectedToWall(wallSideIndicator, pl)) {
                if (enemyHealthReduction(omenoct, BULLET_REDUCE_HP)) i--;
            }
        }
    }
    private void checkCollisionBulletAndPanels() {
        for (int i = 0; i < RigidBulletModel.rigidBulletModelList.size(); i++) {
            RigidBulletModel ptr = RigidBulletModel.rigidBulletModelList.get(i);
            ArrayList<PanelModel> coveredPanels = ptr.hasCollisions(PanelModel.panelModelList);

            if (coveredPanels == null) return;

            for (PanelModel panel : coveredPanels) {
                if (ptr.getCenter().getX() + ptr.getRadius() >= panel.getX() + panel.getWidth()) {
                    panel.bulletHit(WallSideIndicator.RIGHT);
                    omenoctWallHealthReduction(WallSideIndicator.RIGHT, panel.getX() + panel.getWidth());
                }
                if (ptr.getCenter().getX() - ptr.getRadius() <= panel.getX()) {
                    panel.bulletHit(WallSideIndicator.LEFT);
                    omenoctWallHealthReduction(WallSideIndicator.LEFT, panel.getX());
                }
                if (ptr.getCenter().getY() + ptr.getRadius() >= panel.getY() + panel.getHeight()) {
                    panel.bulletHit(WallSideIndicator.DOWN);
                    omenoctWallHealthReduction(WallSideIndicator.DOWN, panel.getY() + panel.getHeight());
                }
                if (ptr.getCenter().getY() - ptr.getRadius() <= panel.getY()) {
                    panel.bulletHit(WallSideIndicator.UP);
                    omenoctWallHealthReduction(WallSideIndicator.UP, panel.getY());
                }
            }

            RigidBulletModel.removeFromAllList(ptr.getId());
        }
    }
    private void moveAllModels() {
        for (Movable ptr : Movable.movable) {
            ptr.move();
        }
    }

    /// view functions
    private void updateEpsilonView() {
        Controller.getINSTANCE().logic.epsilonView.setCurrentCenter(epsilon.getCenter());
        Controller.getINSTANCE().logic.epsilonView.setCurrentHp(epsilon.getHp());
        Controller.getINSTANCE().logic.epsilonView.setCurrentXp(epsilon.getXp());
        Controller.getINSTANCE().logic.epsilonView.setCurrentVertices(epsilon.getVertices());
        Controller.getINSTANCE().logic.epsilonView.setCurrentRadius(epsilon.getRadius());
    }
    private void updateTriangleEnemyView() {
        for (TriangleEnemyView ptr : TriangleEnemyView.triangleEnemyViewList) {
            TriangleEnemy tmp = Controller.getINSTANCE().logic.findTriangleEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateSquareEnemyView() {
        for (SquareEnemyView ptr : SquareEnemyView.squareEnemyViewList) {
            SquareEnemy tmp = Controller.getINSTANCE().logic.findSquareEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateOmenoctEnemyView() {
        for (OmenoctEnemyView ptr : OmenoctEnemyView.omenoctEnemyViewList) {
            OmenoctEnemy tmp = Controller.getINSTANCE().logic.findOmenoctEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateNecropickEnemyView() {
        for (NecropickEnemyView ptr : NecropickEnemyView.necropickEnemyViewsList) {
            NecropickEnemy tmp = Controller.getINSTANCE().logic.findNecropickEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
            ptr.setStationedType(tmp.getStationedType());
        }
    }
    private void updateCollectibleView() {
        for (CollectibleView ptr : CollectibleView.collectibleViewList) {
            Collectible tmp = Controller.getINSTANCE().logic.findCollectibleModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
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

    public Timer getViewUpdater() {
        return viewUpdater;
    }

    public Timer getModelUpdater() {
        return modelUpdater;
    }
}
