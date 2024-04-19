package model.charactersModel;

import controller.constant.Constants;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class EpsilonModel {
    Point2D center;
    int xp, hp;
    public double xVelocity, yVelocity;
    ArrayList<Point2D> vertices;

    public EpsilonModel(Point2D center) {
        this.center = center;
        xp = INITIAL_XP;
        hp = INITIAL_HP;
        xVelocity = 0;
        yVelocity = 0;
        vertices = new ArrayList<>();
    }
    public void moveWithKeys(double xFactor, double yFactor, Dimension panelSize) {
        xVelocity = xVelocity + (xFactor * 0.5);
        yVelocity = yVelocity + (yFactor * 0.5);

        if (xVelocity > 0) xVelocity = Math.min(xVelocity, 4);
        else if (xVelocity < 0) xVelocity = Math.max(xVelocity, -4);
        if (yVelocity > 0) yVelocity = Math.min(yVelocity, 4);
        else if (yVelocity < 0) yVelocity = Math.max(yVelocity, -4);

        center.setLocation(center.getX() + xVelocity, center.getY() + yVelocity);
        adjustLocation(panelSize);
    }
    public void adjustLocation(Dimension panelSize) {
        double x = center.getX();
        x = Math.max(x, EPSILON_RADIUS);
        x = Math.min(x, panelSize.width);

        double y = center.getY();
        y = Math.max(y, EPSILON_RADIUS);
        y = Math.min(y, panelSize.height);

        center.setLocation(x, y);
    }
    public Point2D getCenter() {
        return center;
    }

    public int getXp() {
        return xp;
    }

    public int getHp() {
        return hp;
    }

    public ArrayList<Point2D> getVertices() {
        return vertices;
    }
}
