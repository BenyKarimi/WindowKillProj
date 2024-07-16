package model.bulletModel;

import controller.Controller;
import controller.Utils;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import model.panelModel.PanelModel;
import view.bulletView.BulletView;
import view.charecterViews.SquareEnemyView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class BulletModel implements Collidable, Movable {
    private final String id;
    private double radius;
    private double speed;
    private Point2D center;
    private Direction direction;
    public static ArrayList<BulletModel> bulletModelList = new ArrayList<>();

    public BulletModel(Point2D center, Direction direction) {
        this.center = center;
        this.direction = direction;
        speed = 5;
        id = UUID.randomUUID().toString();
        radius = 5.0;
        bulletModelList.add(this);
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
        for (int i = 0; i < bulletModelList.size(); i++) {
            if (bulletModelList.get(i).getId().equals(id)) {
                bulletModelList.remove(i);
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
}
