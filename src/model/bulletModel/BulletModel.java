package model.bulletModel;

import controller.Controller;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class BulletModel implements Collidable, Movable {
    private final String id;
    double radius;
    double speed;
    Point2D center;
    Direction direction;
    public static ArrayList<BulletModel> bulletModelList = new ArrayList<>();

    public BulletModel(Point2D center, Direction direction) {
        this.center = center;
        this.direction = direction;
        speed = 5;
        id = UUID.randomUUID().toString();
        radius = 2.0;
        bulletModelList.add(this);
        Collidable.collidables.add(this);
        Movable.movable.add(this);
        Controller.getINSTANCE().createBulletView(id);
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

    public Direction getDirection() {
        return direction;
    }
}
