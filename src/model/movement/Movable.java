package model.movement;

import controller.Utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Movable {
    ArrayList<Movable> movable = new ArrayList<>();

    void setCenter(Point2D center);
    Point2D getCenter();
    double getSpeed();
    Direction getDirection();

    default void move() {
        Point2D tmp = Utils.addVectors(getCenter(), Utils.multiplyVector(getDirection().getDirectionVector(), getSpeed()));
        setCenter(tmp);
    }
}
