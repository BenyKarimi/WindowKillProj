package model.charactersModel;

import controller.Controller;
import controller.Utils;
import model.bulletModel.NonRigidBulletModel;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import model.panelModel.Isometric;
import model.panelModel.PanelModel;
import model.panelModel.Rigid;
import view.charecterViews.WyrmEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class WyrmEnemy extends Enemy {

    private PanelModel wyrmPanel;
    private boolean clockWise;
    private int lastUpdate;
    private int lastAttackTime;
    public static ArrayList<WyrmEnemy> wyrmEnemiesList = new ArrayList<>();

    public WyrmEnemy(Point2D center, double size, double speed, boolean clockWise) {
        super(Utils.processRandomId(), size, speed, speed, false, WYRM_ENEMY_HP, WYRM_ENEMY_REDUCER_HP, WYRM_ENEMY_COLLECTIBLE_NUMBER, WYRM_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        this.clockWise = clockWise;
        calculateVertices();
        makePanel();
        Controller.getINSTANCE().createWyrmEnemyView(super.getId());
        Collidable.collidables.add(this);
        wyrmEnemiesList.add(this);
        Movable.movable.add(this);
    }

    @Override
    public void calculateVertices() {
        super.getVertices().clear();
        ArrayList<Point2D> ver = Utils.calculateSquareVertices(super.getCenter(), super.getSize());
        for (Point2D ptr : ver) super.getVertices().add(ptr);
    }
    @Override
    public void updateDirection(Point2D point) {
        double dist = point.distance(super.getCenter());
        if (Utils.approxEqual(dist, WYRM_EPSILON_MIN_DISTANCE)) {
            Point2D delta = new Point2D.Double(point.getX() - super.getCenter().getX(), point.getY() - super.getCenter().getY());
            if (clockWise) {
                delta = new Point2D.Double(delta.getX() * Math.cos(Math.PI / 2) + delta.getY() * Math.sin(Math.PI / 2), delta.getX() * -Math.sin(Math.PI / 2) + delta.getY() * Math.cos(Math.PI / 2));
            }
            else {
                delta = new Point2D.Double(delta.getX() * Math.cos(-Math.PI / 2) + delta.getY() * Math.sin(-Math.PI / 2), delta.getX() * -Math.sin(-Math.PI / 2) + delta.getY() * Math.cos(-Math.PI / 2));
            }
            Direction toPoint = new Direction(delta);
            super.setDirection(new Direction(toPoint.getDirectionVector()));
        }
        else if (dist < WYRM_EPSILON_MIN_DISTANCE) {
            Point2D delta = new Point2D.Double(point.getX() - super.getCenter().getX(), point.getY() - super.getCenter().getY());
            delta = new Point2D.Double(-delta.getX(), -delta.getY());
            Direction toPoint = new Direction(delta);
            super.setDirection(new Direction(toPoint.getDirectionVector()));
        }
        else if (dist > WYRM_EPSILON_MIN_DISTANCE) {
            Point2D delta = new Point2D.Double(point.getX() - super.getCenter().getX(), point.getY() - super.getCenter().getY());
            Direction toPoint = new Direction(delta);
            super.setDirection(new Direction(toPoint.getDirectionVector()));
        }

        wyrmPanel.setDirection(super.getDirection());
        wyrmPanel.setSpeed(super.getSpeed());
    }
    private void makePanel() {
        wyrmPanel = new PanelModel(super.getVertices().get(0).getX() - super.getSize() / 2, super.getVertices().get(0).getY() - super.getSize() / 2, super.getSize() * 2, super.getSize() * 2, Isometric.YES, Rigid.NO);
    }

    public void makeAttack(int time, Point2D epsilonCenter) {
        if (time - lastAttackTime >= 2) {
            Direction bulletDirection = new Direction(new Point2D.Double(epsilonCenter.getX() - super.getCenter().getX(), epsilonCenter.getY() - super.getCenter().getY()));
            Point2D bulletCenter = Utils.aimAndBulletDrawerCalculator(epsilonCenter, super.getCenter(), super.getSize());
            new NonRigidBulletModel(bulletCenter, bulletDirection, WYRM_ENEMY_RANGED_REDUCER_HP, super.getId());
            lastAttackTime = time;
        }
    }
    public PanelModel getWyrmPanel() {
        return wyrmPanel;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isHovering() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public boolean isStationed() {
        return false;
    }

    public void reverseClockwise(int time) {
        if (time - lastUpdate >= 400) {
            clockWise = !clockWise;
            lastUpdate = time;
        }
    }

    public boolean isClockWise() {
        return clockWise;
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < wyrmEnemiesList.size(); i++) {
            if (wyrmEnemiesList.get(i).getId().equals(id)) {
                wyrmEnemiesList.remove(i);
                break;
            }
        }
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            if (Collidable.collidables.get(i).getId() != null && Collidable.collidables.get(i).getId().equals(id)) {
                Collidable.collidables.remove(i);
                break;
            }
        }
        for (int i = 0; i < Movable.movable.size(); i++) {
            if (Movable.movable.get(i).getId() != null && Movable.movable.get(i).getId().equals(id)) {
                Movable.movable.remove(i);
                break;
            }
        }
        for (int i = 0; i < Enemy.enemiesList.size(); i++) {
            if (Enemy.enemiesList.get(i).getId().equals(id)) {
                Enemy.enemiesList.remove(i);
                break;
            }
        }
        for (int i = 0; i < WyrmEnemyView.wyrmEnemyViewsList.size(); i++) {
            if (WyrmEnemyView.wyrmEnemyViewsList.get(i).getId().equals(id)) {
                WyrmEnemyView.wyrmEnemyViewsList.remove(i);
                break;
            }
        }
    }
    public void removePanel() {
        PanelModel.removeFromAllList(wyrmPanel.getId());
    }
}
