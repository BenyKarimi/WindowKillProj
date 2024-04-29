package controller;

import controller.constant.Constants;
import controller.constant.GameValues;
import controller.handeler.SkillTreeHandled;
import controller.handeler.StoreActionHandel;
import controller.handeler.TypedActionHandel;
import controller.random.RandomHelper;
import model.bulletModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquareEnemy;
import model.charactersModel.TriangleEnemy;
import model.collectibleModel.Collectible;
import model.collision.Collidable;
import model.movement.Movable;
import view.bulletView.BulletView;
import view.charecterViews.EpsilonView;
import view.charecterViews.SquareEnemyView;
import view.charecterViews.TriangleEnemyView;
import view.collectibleView.CollectibleView;
import view.container.FinishPanel;
import view.container.GamePanel;
import view.container.GlassFrame;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.COLLECTIBLE_SIZE;
import static controller.constant.Constants.EPSILON_RADIUS;

public class Logic {
    public EpsilonModel epsilon;
    EpsilonView epsilonView;
    public Logic() {
        createEpsilon();
    }
    public void createEpsilon() {
        epsilon = new EpsilonModel(new Point2D.Double(EPSILON_RADIUS, EPSILON_RADIUS));
//        epsilonView = EpsilonView.getINSTANCE();
        epsilonView = new EpsilonView();
    }
    public void createCollectible(int collectibleNumber, int collectibleXp, Point2D enemyCenter) {
        if (collectibleNumber == 1) {
            new Collectible(enemyCenter, collectibleXp);
        }
        if (collectibleNumber == 2) {
            new Collectible(new Point2D.Double(enemyCenter.getX() + 1.5 * COLLECTIBLE_SIZE, enemyCenter.getY()), collectibleXp);
            new Collectible(new Point2D.Double(enemyCenter.getX() - 1.5 * COLLECTIBLE_SIZE, enemyCenter.getY()), collectibleXp);
        }
    }
    public boolean makeWave() {
        if (TriangleEnemy.triangleEnemyList.size() != 0 || SquareEnemy.squareEnemyList.size() != 0) return false;

        GameValues.waveNumber++;
        if (GameValues.waveNumber == 4) return false;
        ArrayList<Point2D> enemiesCenter = RandomHelper.randomWaveEnemyCenters();
        for (Point2D ptr : enemiesCenter) {
            if (RandomHelper.randomWaveEnemyType() == 0) {
                new SquareEnemy(ptr, RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
            else {
                new TriangleEnemy(ptr, RandomHelper.randomWaveEnemySize(), RandomHelper.randomWaveEnemySpeed());
            }
        }
        return true;
    }
    public void checkGameOver() {
        if (epsilon.getHp() <= 0) {
            showFinishGame();
        }
    }
    public void showFinishGame() {
        if (epsilon.getHp() <= 0) Constants.gameOver.play();
        else Constants.winGame.play();

        int finishXP = epsilon.getXp();
        deleteAllInfo();
        new FinishPanel(finishXP);
    }
    private void deleteAllInfo() {
        Controller.getINSTANCE().updater.modelUpdater.stop();
        Controller.getINSTANCE().updater.viewUpdater.stop();
        Constants.INITIAL_XP = epsilon.getXp();
        epsilon.setVerticesNumber(0);
        Constants.EPSILON_REDUCE_HP = 10;
        Constants.BULLET_REDUCE_HP = 5;
        GlassFrame.getINSTANCE().remove(GamePanel.getINSTANCE());
        GamePanel.setINSTANCE(null);
        Controller.setINSTANCE(null);
        BulletModel.bulletModelList.clear();
        SquareEnemy.squareEnemyList.clear();
        TriangleEnemy.triangleEnemyList.clear();
        Collectible.collectibleList.clear();
        BulletView.bulletViewList.clear();
        SquareEnemyView.squareEnemyViewList.clear();
        TriangleEnemyView.triangleEnemyViewList.clear();
        CollectibleView.collectibleViewList.clear();
        Collidable.collidables.clear();
        Movable.movable.clear();
        GameValues.waveNumber = 0;
        TypedActionHandel.setDown(false);
        TypedActionHandel.setLeft(false);
        TypedActionHandel.setUp(false);
        TypedActionHandel.setRight(false);
        StoreActionHandel.setThreeBullet(false);
        SkillTreeHandled.makeAllRestart();
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
    public BulletModel findBulletModel(String id) {
        for (BulletModel ptr : BulletModel.bulletModelList) {
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
}
