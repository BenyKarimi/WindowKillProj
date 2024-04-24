package controller;

import controller.constant.GameValues;
import controller.handeler.TypedActionHandel;
import model.bulletModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquareEnemy;
import model.charactersModel.TriangleEnemy;
import model.collectibleModel.Collectible;
import model.collision.Collidable;
import model.collision.CollisionPoint;
import model.movement.ImpactMechanism;
import model.movement.Movable;
import view.bulletView.BulletView;
import view.charecterViews.SquareEnemyView;
import view.charecterViews.TriangleEnemyView;
import view.collectibleView.CollectibleView;
import view.container.GamePanel;
import view.container.GlassFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static controller.constant.Constants.*;

public class Updater {
    boolean startGame = false;
    boolean isWave = false;
    boolean wonGame = false;
    boolean epsilonGoesBigger = false;
    EpsilonModel epsilon;
    Timer viewUpdater;
    Timer modelUpdater;
    int startTime, startWave;
    public Updater() {
        viewUpdater = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()){{setCoalesce(true);}};
        viewUpdater.start();
        modelUpdater = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()){{setCoalesce(true);}};
        modelUpdater.start();
    }
    public void updateView() {
        Rectangle tmp = GamePanel.getINSTANCE().getBounds();
        updateEpsilonView();

        if (!startGame) {
            if (tmp.width > GAME_PANEL_START_DIMENSION.width && tmp.height > GAME_PANEL_START_DIMENSION.height)
                GamePanel.getINSTANCE().setBounds(tmp.x + 3, tmp.y + 3, tmp.width - 6, tmp.height - 6);
            else {
                startGame = true;
                GameValues.panelUpLeft = new Point2D.Double(tmp.x, tmp.y);
                GameValues.panelSize = new Point2D.Double(tmp.width, tmp.height);
                startTime = GamePanel.getINSTANCE().getTimer().getSeconds();
            }
        }
        else {
            GamePanel.getINSTANCE().setBounds((int) GameValues.panelUpLeft.getX(), (int) GameValues.panelUpLeft.getY(), (int) GameValues.panelSize.getX(), (int) (GameValues.panelSize.getY()));
            if (GamePanel.getINSTANCE().getTimer().getSeconds() - startTime >= 10 && GamePanel.getINSTANCE().getTimer().getSeconds() - startWave >= 3) {
                int f = (GameValues.panelSize.getX() > 300 ? 1 : 0);
                int s = (GameValues.panelSize.getY() > 300 ? 1 : 0);
                if (wonGame) {
                    f = (GameValues.panelSize.getX() > 0 ? 1 : 0);
                    s = (GameValues.panelSize.getY() > 0 ? 1 : 0);
                    if (f == 0 && s == 0) Controller.getINSTANCE().logic.showFinishGame();
                    GameValues.panelUpLeft = new Point2D.Double(GameValues.panelUpLeft.getX() + 3 * f, GameValues.panelUpLeft.getY() + 3 * s);
                    GameValues.panelSize = new Point2D.Double(GameValues.panelSize.getX() - 6 * f, GameValues.panelSize.getY() - 6 * s);
                }
                else if (f == 1 || s == 1) {
                    GameValues.panelUpLeft = new Point2D.Double(GameValues.panelUpLeft.getX() + 0.125 * f, GameValues.panelUpLeft.getY() + 0.125 * s);
                    GameValues.panelSize = new Point2D.Double(GameValues.panelSize.getX() - 0.25 * f, GameValues.panelSize.getY() - 0.25 * s);
                }
            }
            // update enemies and add boolean for is wave or not
            updateTriangleEnemyView();
            updateSquareEnemyView();
            updateCollectibleView();
            updateBulletView();
        }
        GlassFrame.getINSTANCE().repaint();
    }
    public void updateModel() {
        updateEpsilonModel();
        updateEnemiesModel();
        updateCollectibleModel();
        updateCollisionAndImpact();
        checkCollisionBulletAndPanel();
        moveAllModels();
        Controller.getINSTANCE().logic.checkGameOver();
        if (startGame && !epsilonGoesBigger) {
            if (Controller.getINSTANCE() == null) return;
            isWave = Controller.getINSTANCE().logic.makeWave();
            if (!isWave && GameValues.waveNumber == 4) {
                epsilonGoesBigger = true;
                return;
            }
            if (isWave) startWave = GamePanel.getINSTANCE().getTimer().getSeconds();
            isWave = false;
        }
    }
    /// model functions
    private void updateEpsilonModel() {
        if (epsilonGoesBigger) {
            epsilon.setRadius(epsilon.getRadius() + 10);
            if (epsilon.getRadius() > Math.max(GamePanel.getINSTANCE().getWidth(), GamePanel.getINSTANCE().getHeight())) {
                wonGame = true;
            }
            return;
        }
        TypedActionHandel.doMove();
        epsilon.adjustLocation(new Dimension(GamePanel.getINSTANCE().getWidth(), GamePanel.getINSTANCE().getHeight()));
        if (epsilon.getSpeed() > 0) {
            epsilon.setSpeed(epsilon.getSpeed() - (epsilon.getSpeed() / 10));
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
                        for (Point2D ptr : first.getVertices()) if (ptr.equals(point)) firstVer = true;
                        for (Point2D ptr : second.getVertices()) if (ptr.equals(point)) secondVer = true;
                        if (firstVer && !secondVer) {
                            if (second instanceof TriangleEnemy) {
                                ((TriangleEnemy) second).setHp(((TriangleEnemy) second).getHp() - 10);
                                if (((TriangleEnemy) second).getHp() <= 0) {
                                    Controller.getINSTANCE().logic.createCollectible(((TriangleEnemy) second).getCollectibleNumber()
                                            , ((TriangleEnemy) second).getCollectibleXp(), second.getCenter());
                                    TriangleEnemy.removeFromAllList(second.getId());
                                }
                            }
                            else {
                                ((SquareEnemy) second).setHp(((SquareEnemy) second).getHp() - 10);
                                if (((SquareEnemy) second).getHp() <= 0) {
                                    Controller.getINSTANCE().logic.createCollectible(((SquareEnemy) second).getCollectibleNumber()
                                            , ((SquareEnemy) second).getCollectibleXp(), second.getCenter());
                                    SquareEnemy.removeFromAllList(second.getId());
                                }
                            }
                        }
                        else if (secondVer && !firstVer) {
                            if (second instanceof TriangleEnemy) {
                                epsilon.setHp(epsilon.getHp() - ((TriangleEnemy) second).getReducerHp());
                            }
                            else epsilon.setHp(epsilon.getHp() - ((SquareEnemy) second).getReducerHp());
                        }
                        impactLevel = 5;
                    }
                    else if (first instanceof BulletModel && (second instanceof TriangleEnemy || second instanceof SquareEnemy)) {
                        if (second instanceof TriangleEnemy) {
                            ((TriangleEnemy) second).setHp(((TriangleEnemy) second).getHp() - 5);
                            if (((TriangleEnemy) second).getHp() <= 0) {
                                Controller.getINSTANCE().logic.createCollectible(((TriangleEnemy) second).getCollectibleNumber()
                                        , ((TriangleEnemy) second).getCollectibleXp(), second.getCenter());
                                TriangleEnemy.removeFromAllList(second.getId());
                            }
                        }
                        else {
                            ((SquareEnemy) second).setHp(((SquareEnemy) second).getHp() - 5);
                            if (((SquareEnemy) second).getHp() <= 0) {
                                Controller.getINSTANCE().logic.createCollectible(((SquareEnemy) second).getCollectibleNumber()
                                        , ((SquareEnemy) second).getCollectibleXp(), second.getCenter());
                                SquareEnemy.removeFromAllList(second.getId());
                            }
                        }
                        BulletModel.removeFromAllList(first.getId());
                        impactLevel = 5;
                    }
                    else impactLevel = 2;
                    ImpactMechanism.applyImpact(point, impactLevel);
                }
            }
        }
    }
    private void checkCollisionBulletAndPanel() {
        for (int i = 0; i < BulletModel.bulletModelList.size(); i++) {
            BulletModel ptr = BulletModel.bulletModelList.get(i);
            if (ptr.getCenter().getX() + ptr.getRadius() > GameValues.panelSize.getX()) {
                GameValues.panelUpLeft = new Point2D.Double(GameValues.panelUpLeft.getX() + 4, GameValues.panelUpLeft.getY());
                GameValues.panelSize = new Point2D.Double(GameValues.panelSize.getX() + 4, GameValues.panelSize.getY());
                BulletModel.removeFromAllList(ptr.getId());
                i--;
            }
            else if (ptr.getCenter().getX() - ptr.getRadius() < 0) {
                GameValues.panelUpLeft = new Point2D.Double(GameValues.panelUpLeft.getX() - 8, GameValues.panelUpLeft.getY());
                GameValues.panelSize = new Point2D.Double(GameValues.panelSize.getX() + 4, GameValues.panelSize.getY());
                BulletModel.removeFromAllList(ptr.getId());
                i--;
            }
            else if (ptr.getCenter().getY() + ptr.getRadius() > GameValues.panelSize.getY()) {
                GameValues.panelUpLeft = new Point2D.Double(GameValues.panelUpLeft.getX(), GameValues.panelUpLeft.getY() + 4);
                GameValues.panelSize = new Point2D.Double(GameValues.panelSize.getX(), GameValues.panelSize.getY() + 4);
                BulletModel.removeFromAllList(ptr.getId());
                i--;
            }
            else if (ptr.getCenter().getY() - ptr.getRadius() < 0) {
                GameValues.panelUpLeft = new Point2D.Double(GameValues.panelUpLeft.getX(), GameValues.panelUpLeft.getY() - 8);
                GameValues.panelSize = new Point2D.Double(GameValues.panelSize.getX(), GameValues.panelSize.getY() + 4);
                BulletModel.removeFromAllList(ptr.getId());
                i--;
            }
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

    public Timer getViewUpdater() {
        return viewUpdater;
    }

    public Timer getModelUpdater() {
        return modelUpdater;
    }
}
