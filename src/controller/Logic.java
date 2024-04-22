package controller;

import controller.constant.GameValues;
import controller.random.RandomHelper;
import model.bulletModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquareEnemy;
import model.charactersModel.TriangleEnemy;
import model.collectibleModel.Collectible;
import view.bulletView.BulletView;
import view.charecterViews.EpsilonView;
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
        epsilonView = EpsilonView.getINSTANCE();
    }
    public void createCollectible(int collectibleNumber, int collectibleXp, Point2D enemyCenter) {
        if (collectibleNumber == 1) {
            new Collectible(enemyCenter, collectibleXp);
        }
        if (collectibleNumber == 2) {
            new Collectible(new Point2D.Double(enemyCenter.getX() + COLLECTIBLE_SIZE, enemyCenter.getY()), collectibleXp);
            new Collectible(new Point2D.Double(enemyCenter.getX() - COLLECTIBLE_SIZE, enemyCenter.getY()), collectibleXp);
        }
    }
    public boolean makeWave() {
        if (TriangleEnemy.triangleEnemyList.size() != 0 || SquareEnemy.squareEnemyList.size() != 0) return false;

        GameValues.waveNumber++;
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
            /// has to change and go to game over panel
        }
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
