package client.model.bulletModel;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.model.collision.Collidable;
import client.model.movement.Direction;
import client.model.movement.Movable;
import client.model.panelModel.PanelModel;
import client.view.bulletView.BulletView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class RigidBulletModel implements Collidable, Movable {
    private final String id;
    private final String shooterEntity;
    private final int reduceHp;
    private double radius;
    private double speed;
    private Point2D center;
    private Direction direction;
    public static ArrayList<RigidBulletModel> rigidBulletModelList = new ArrayList<>();

    public RigidBulletModel(Point2D center, Direction direction, int reduceHp, String shooterEntity, double speed) {
        this.center = center;
        this.direction = direction;
        this.reduceHp = reduceHp;
        this.shooterEntity = shooterEntity;
        this.speed = speed;
        id = Utils.processRandomId();
        radius = 5.0;
        rigidBulletModelList.add(this);
        Collidable.collidables.add(this);
        Movable.movable.add(this);
        Controller.getINSTANCE().createBulletView(id);
    }

    public ArrayList<PanelModel> hasCollisions(ArrayList<PanelModel> panelModels) {
        if (Utils.isCircleInside(panelModels, center, radius)) return null;

        Point2D tmp = Utils.addVectors(center, Utils.multiplyVector(Utils.makeNegativeVector(getDirection().getDirectionVector()), getSpeed()));
        return Utils.coveringPanels(panelModels, tmp, radius);
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

    @Override
    public ArrayList<Point2D> getVertices() {
        return null;
    }
    public static void removeFromAllList(String id) {
        for (int i = 0; i < rigidBulletModelList.size(); i++) {
            if (rigidBulletModelList.get(i).getId().equals(id)) {
                rigidBulletModelList.remove(i);
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
        for (int i = 0; i < BulletView.bulletViewList.size(); i++) {
            if (BulletView.bulletViewList.get(i).getId().equals(id)) {
                BulletView.bulletViewList.remove(i);
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

    public String getShooterEntity() {
        return shooterEntity;
    }

    public int getReduceHp() {
        return reduceHp;
    }
}
