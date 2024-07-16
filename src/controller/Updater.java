package controller;

import controller.constant.Constants;
import controller.constant.GameValues;
import controller.handeler.SkillTreeHandled;
import controller.handeler.TypedActionHandle;
import model.bulletModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquareEnemy;
import model.charactersModel.TriangleEnemy;
import model.collectibleModel.Collectible;
import model.collision.Collidable;
import model.collision.CollisionPoint;
import model.movement.ImpactMechanism;
import model.movement.Movable;
import model.panelModel.PanelModel;
import org.w3c.dom.css.CSSImportRule;
import view.bulletView.BulletView;
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
    EpsilonModel epsilon;
    Timer viewUpdater;
    Timer modelUpdater;
    GameTimer gameTimer;
    int startTime, startWave, startGoingBigger;
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
        updateCollectibleView();
        updateBulletView();
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
                if (panel.getWidth() <= GAME_PANEL_END_SIZE.width && panel.getHeight() <= GAME_PANEL_END_SIZE.height) {
                    Controller.getINSTANCE().logic.showFinishGame();
                    return;
                }
            }

            else if (gameTimer.getSeconds() - startTime >= 10 && gameTimer.getSeconds() - startWave >= 3) {
                panel.inGameShrinkValue();
            }
            panel.shrink();

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
                    if ((first instanceof EpsilonModel && second instanceof BulletModel) || first instanceof BulletModel && second instanceof EpsilonModel) continue;
                    double impactLevel = 0;
                    if (first instanceof EpsilonModel && (second instanceof TriangleEnemy || second instanceof SquareEnemy)) {
                        boolean firstVer = false;
                        boolean secondVer = false;
                        for (Point2D ptr : first.getVertices()) {
                            if ((int)ptr.getX() == (int)point.getX() && (int)ptr.getY() == (int) point.getY()) firstVer = true;
                        }
                        for (Point2D ptr : second.getVertices()) if (ptr.equals(point)) secondVer = true;
                        if (firstVer && !secondVer) {
                            if (second instanceof TriangleEnemy) {
                                ((TriangleEnemy) second).setHp(((TriangleEnemy) second).getHp() - EPSILON_REDUCE_HP);
                                if (((TriangleEnemy) second).getHp() <= 0) {
                                    enemyDeath.play();
                                    Controller.getINSTANCE().logic.createCollectible(((TriangleEnemy) second).getCollectibleNumber()
                                            , ((TriangleEnemy) second).getCollectibleXp(), second.getCenter());
                                    TriangleEnemy.removeFromAllList(second.getId());
                                }
                            }
                            else {
                                ((SquareEnemy) second).setHp(((SquareEnemy) second).getHp() - EPSILON_REDUCE_HP);
                                if (((SquareEnemy) second).getHp() <= 0) {
                                    enemyDeath.play();
                                    Controller.getINSTANCE().logic.createCollectible(((SquareEnemy) second).getCollectibleNumber()
                                            , ((SquareEnemy) second).getCollectibleXp(), second.getCenter());
                                    SquareEnemy.removeFromAllList(second.getId());
                                }
                            }
                        }
                        else if (secondVer && !firstVer) {
                            injured.play();
                            if (second instanceof TriangleEnemy) {
                                epsilon.setHp(epsilon.getHp() - ((TriangleEnemy) second).getReducerHp());
                            }
                            else epsilon.setHp(epsilon.getHp() - ((SquareEnemy) second).getReducerHp());
                        }
                        impactLevel = BULLET_REDUCE_HP;
                    }
                    else if (first instanceof BulletModel && (second instanceof TriangleEnemy || second instanceof SquareEnemy)) {
                        if (second instanceof TriangleEnemy) {
                            ((TriangleEnemy) second).setHp(((TriangleEnemy) second).getHp() - BULLET_REDUCE_HP);
                            if (((TriangleEnemy) second).getHp() <= 0) {
                                enemyDeath.play();
                                Controller.getINSTANCE().logic.createCollectible(((TriangleEnemy) second).getCollectibleNumber()
                                        , ((TriangleEnemy) second).getCollectibleXp(), second.getCenter());
                                TriangleEnemy.removeFromAllList(second.getId());
                            }
                        }
                        else {
                            ((SquareEnemy) second).setHp(((SquareEnemy) second).getHp() - BULLET_REDUCE_HP);
                            if (((SquareEnemy) second).getHp() <= 0) {
                                enemyDeath.play();
                                Controller.getINSTANCE().logic.createCollectible(((SquareEnemy) second).getCollectibleNumber()
                                        , ((SquareEnemy) second).getCollectibleXp(), second.getCenter());
                                SquareEnemy.removeFromAllList(second.getId());
                            }
                        }
                        BulletModel.removeFromAllList(first.getId());
                        impactLevel = BULLET_REDUCE_HP;
                    }
                    else impactLevel = 2;
                    ImpactMechanism.applyImpact(point, impactLevel);
                }
            }
        }
    }
    private void checkCollisionBulletAndPanels() {
        for (int i = 0; i < BulletModel.bulletModelList.size(); i++) {
            BulletModel ptr = BulletModel.bulletModelList.get(i);
            ArrayList<PanelModel> coveredPanels = ptr.hasCollisions(PanelModel.panelModelList);

            if (coveredPanels == null) return;

            for (PanelModel panel : coveredPanels) {
                if (ptr.getCenter().getX() + ptr.getRadius() >= panel.getX() + panel.getWidth()) {
                    panel.bulletHit("right");
//                    panel.setX(panel.getX() + 4);
//                    panel.setWidth(panel.getWidth() + 4);
                }
                if (ptr.getCenter().getX() - ptr.getRadius() <= panel.getX()) {
                    panel.bulletHit("left");
//                    panel.setX(panel.getX() - 8);
//                    panel.setWidth(panel.getWidth() + 4);
                }
                if (ptr.getCenter().getY() + ptr.getRadius() >= panel.getY() + panel.getHeight()) {
                    panel.bulletHit("down");
//                    panel.setY(panel.getY() + 4);
//                    panel.setHeight(panel.getHeight() + 4);
                }
                if (ptr.getCenter().getY() - ptr.getRadius() <= panel.getY()) {
                    panel.bulletHit("up");
//                    panel.setY(panel.getY() - 8);
//                    panel.setHeight(panel.getHeight() + 4);
                }
            }

            BulletModel.removeFromAllList(ptr.getId());
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
    private void updateCollectibleView() {
        for (CollectibleView ptr : CollectibleView.collectibleViewList) {
            Collectible tmp = Controller.getINSTANCE().logic.findCollectibleModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateBulletView() {
        for (BulletView ptr : BulletView.bulletViewList) {
            BulletModel tmp = Controller.getINSTANCE().logic.findBulletModel(ptr.getId());
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
