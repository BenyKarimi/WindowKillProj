package model.charactersModel;

import controller.constant.Constants;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class EpsilonModel implements Collidable, Movable {
    Point2D center;
    int xp, hp;
    double radius;
    double speed;
    public double xVelocity, yVelocity;
    Direction direction;
    ArrayList<Point2D> vertices;

    public EpsilonModel(Point2D center) {
        this.center = center;
        xp = INITIAL_XP;
        hp = INITIAL_HP;
        xVelocity = 0;
        yVelocity = 0;
        speed = 2;
        direction = new Direction(new Point2D.Double(0, 0));
        radius = EPSILON_RADIUS;
        vertices = new ArrayList<>();
        Collidable.collidables.add(this);
        Movable.movable.add(this);
    }
    public void moveWithKeys(double xFactor, double yFactor, Dimension panelSize) {
        xVelocity = xVelocity + (xFactor * 0.5);
        yVelocity = yVelocity + (yFactor * 0.5);

        if (xVelocity > 0) xVelocity = Math.min(xVelocity, 5);
        else if (xVelocity < 0) xVelocity = Math.max(xVelocity, -5);
        if (yVelocity > 0) yVelocity = Math.min(yVelocity, 5);
        else if (yVelocity < 0) yVelocity = Math.max(yVelocity, -5);

        center.setLocation(center.getX() + xVelocity, center.getY() + yVelocity);
        adjustLocation(panelSize);
    }
    public void adjustLocation(Dimension panelSize) {
        double x = center.getX();
        x = Math.max(x, radius);
        x = Math.min(x, panelSize.width - radius);

        double y = center.getY();
        y = Math.max(y, radius);
        y = Math.min(y, panelSize.height - radius);

        center.setLocation(x, y);
    }
    @Override
    public boolean isCircular() {
        return true;
    }
    @Override
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
    public String getId() {
        return null;
    }
    @Override
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getXp() {
        return xp;
    }

    public int getHp() {
        return hp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ArrayList<Point2D> getVertices() {
        return vertices;
    }
}
