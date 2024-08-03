package client.model.bulletModel;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.model.collision.Collidable;
import client.model.movement.Direction;
import client.model.movement.Movable;
import client.view.bulletView.EnemyNonRigidBulletView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NonRigidBulletModel implements Collidable, Movable {
    private final String id;
    private final String shooterEnemy;
    private double radius;
    private double speed;
    private Point2D center;
    private Direction direction;
    private final int reduceHp;
    public static ArrayList<NonRigidBulletModel> nonRigidBulletModelsList = new ArrayList<>();

    public NonRigidBulletModel(Point2D center, Direction direction, int reduceHp, String shooterEnemy) {
        this.center = center;
        this.direction = direction;
        this.reduceHp = reduceHp;
        this.shooterEnemy = shooterEnemy;
        speed = 5;
        id = Utils.processRandomId();
        radius = 5.0;
        nonRigidBulletModelsList.add(this);
        Collidable.collidables.add(this);
        Movable.movable.add(this);
        Controller.getINSTANCE().createEnemyNonRigidBulletView(id);
    }

    public String getId() {
        return id;
    }
    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public boolean isHovering() {
        return false;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void setCenter(Point2D center) {
        this.center = center;
    }

    public Point2D getCenter() {
        return center;
    }
    @Override
    public double getSpeed() {
        return speed;
    }

    public int getReduceHp() {
        return reduceHp;
    }

    public String getShooterEnemy() {
        return shooterEnemy;
    }

    @Override
    public ArrayList<Point2D> getVertices() {
        return null;
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < nonRigidBulletModelsList.size(); i++) {
            if (nonRigidBulletModelsList.get(i).getId().equals(id)) {
                nonRigidBulletModelsList.remove(i);
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
        for (int i = 0; i < EnemyNonRigidBulletView.nonRigidBulletViewsList.size(); i++) {
            if (EnemyNonRigidBulletView.nonRigidBulletViewsList.get(i).getId().equals(id)) {
                EnemyNonRigidBulletView.nonRigidBulletViewsList.remove(i);
                break;
            }
        }
    }
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean isStationed() {
        return false;
    }
}
