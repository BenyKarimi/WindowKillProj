package client.model.charactersModel;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.model.collision.Collidable;
import client.model.movement.Direction;
import client.model.movement.Movable;
import org.jetbrains.annotations.NotNull;
import client.view.charecterViews.TriangleEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static client.controller.constant.Constants.*;

public class TriangleEnemy extends Enemy {
    public static ArrayList<TriangleEnemy> triangleEnemyList = new ArrayList<>();

    public TriangleEnemy(Point2D center, double size, double speed) {
        super(Utils.processRandomId(), size, speed, speed, false, TRIANGLE_ENEMY_HP, TRIANGLE_ENEMY_REDUCER_HP, TRIANGLE_ENEMY_COLLECTIBLE_NUMBER, TRIANGLE_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        calculateVertices();
        Controller.getINSTANCE().createTriangleEnemyView(super.getId());
        Collidable.collidables.add(this);
        triangleEnemyList.add(this);
        Movable.movable.add(this);
    }
    @Override
    public void calculateVertices() {
        super.getVertices().clear();
        Point2D upVer = new Point2D.Double(super.getCenter().getX(), super.getCenter().getY() - ((Math.sqrt(3) / 3.0) * super.getSize()));
        Point2D leftVer = new Point2D.Double(super.getCenter().getX() - (super.getSize() / 2), super.getCenter().getY() + ((Math.sqrt(3) / 6.0) * super.getSize()));
        Point2D rightVer = new Point2D.Double(super.getCenter().getX() + (super.getSize() / 2), super.getCenter().getY() + ((Math.sqrt(3) / 6.0) * super.getSize()));
        super.getVertices().add(upVer);
        super.getVertices().add(leftVer);
        super.getVertices().add(rightVer);
    }

    @Override
    public void updateDirection(@NotNull Point2D point) {
        Point2D delta = new Point2D.Double(point.getX() - super.getCenter().getX(), point.getY() - super.getCenter().getY());
        Direction toPoint = new Direction(delta);
        if (super.isImpact() && super.getSpeed() > 0.25) {
            super.setSpeed(super.getSpeed() - (super.getSpeed() / 10));
            return;
        }
        if (super.isImpact()) super.setImpact(false);
        super.setDirection(new Direction(toPoint.getDirectionVector()));
        super.setSpeed(Math.min(super.getSpeed() + (super.getSpeed() / 10), super.getInitialSpeed()));
    }
    public void updateSpeed(Point2D point) {
        if (!super.isImpact()) {
            double distance = super.getCenter().distance(point);
            if (distance >= 200.0) super.setSpeed(super.getSpeed() * 2);
            else super.setSpeed(super.getInitialSpeed());
        }
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < triangleEnemyList.size(); i++) {
            if (triangleEnemyList.get(i).getId().equals(id)) {
                triangleEnemyList.remove(i);
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
        for (int i = 0; i < TriangleEnemyView.triangleEnemyViewList.size(); i++) {
            if (TriangleEnemyView.triangleEnemyViewList.get(i).getId().equals(id)) {
                TriangleEnemyView.triangleEnemyViewList.remove(i);
                break;
            }
        }
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
}