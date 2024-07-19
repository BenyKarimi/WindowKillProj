package model.movement;

import controller.Utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Movable {
    ArrayList<Movable> movable = new ArrayList<>();

    void setCenter(Point2D center);
    Point2D getCenter();
    double getSpeed();
    String getId();
    Direction getDirection();
    boolean isStationed();

    default void move() {
        if (isStationed()) return;
        Point2D tmp = Utils.addVectors(getCenter(), Utils.multiplyVector(getDirection().getDirectionVector(), getSpeed()));
        setCenter(tmp);
    }
}
