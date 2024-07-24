package model.charactersModel;

import controller.Controller;
import controller.Pair;
import controller.Utils;
import controller.constant.Constants;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import model.panelModel.PanelModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static controller.constant.Constants.*;

public class EpsilonModel implements Collidable, Movable {
    private final String id;
    private Point2D center;
    private int xp, hp;
    private int verticesNumber;
    private double radius;
    private double speed;
    private double xVelocity, yVelocity;
    private ArrayList<PanelModel> mainPanels;
    private Direction direction;
    private ArrayList<Point2D> vertices;
    private ArrayList<Point2D> aoeCenters;
    private ArrayList<Enemy> enemiesInsideAoe;
    private int lastAoeAttack;
    public static ArrayList<EpsilonModel> epsilonModelsList = new ArrayList<>();

    public EpsilonModel(Point2D center) {
        this.center = center;
        this.id = Utils.processRandomId();
        xp = INITIAL_XP;
        hp = INITIAL_HP;
        xVelocity = 0;
        yVelocity = 0;
        verticesNumber = 0;
        speed = 2;
        lastAoeAttack = 0;
        direction = new Direction(new Point2D.Double(0, 0));
        radius = EPSILON_RADIUS;
        vertices = new ArrayList<>();
        aoeCenters = new ArrayList<>();
        enemiesInsideAoe = new ArrayList<>();
        Controller.getINSTANCE().createEpsilonView(this.id, this.center);
        Collidable.collidables.add(this);
        Movable.movable.add(this);
        epsilonModelsList.add(this);
    }
    public void moveWithKeys(double xFactor, double yFactor) {
        xVelocity = xVelocity + (xFactor * (EPSILON_SPEED / 10));
        yVelocity = yVelocity + (yFactor * (EPSILON_SPEED / 10));

        if (xVelocity > 0) xVelocity = Math.min(xVelocity, EPSILON_SPEED);
        else if (xVelocity < 0) xVelocity = Math.max(xVelocity, -EPSILON_SPEED);
        if (yVelocity > 0) yVelocity = Math.min(yVelocity, EPSILON_SPEED);
        else if (yVelocity < 0) yVelocity = Math.max(yVelocity, -EPSILON_SPEED);

        center.setLocation(center.getX() + xVelocity, center.getY() + yVelocity);
        adjustLocation(PanelModel.panelModelList);
    }
    public void adjustLocation(ArrayList<PanelModel> panelModels) {
        center = Utils.adjustedCenter(center, radius, panelModels, EPSILON_SPEED);
    }

    public void updateVertices() {
        vertices.clear();
        vertices.addAll(Utils.circlePartition(center, radius + 5, verticesNumber));
    }
    public void updateMainPanels(ArrayList<PanelModel> panelModels) {
        mainPanels = Utils.coveringPanels(panelModels, center, radius);
    }
    public void updateAoeAttack() {
        aoeCenters.clear();
        aoeCenters.addAll(Utils.circlePartition(center, 2 * radius, 3));

        for (Enemy enemy : Enemy.enemiesList) {
            boolean inside = false;
            for (Point2D aoeCenter : aoeCenters) {
                if (Utils.isPolygonInside(Utils.makePolygonWithVertices(Utils.circlePartition(aoeCenter, radius / 2, 36)), Utils.makePolygonWithVertices(enemy.getVertices()))) {
                    inside = true;
                }
            }
            if (inside) {
                enemiesInsideAoe.add(enemy);
            }
            else enemiesInsideAoe.remove(enemy);
        }
    }

    public ArrayList<Point2D> getAoeCenters() {
        return aoeCenters;
    }

    public ArrayList<Enemy> getEnemiesInsideAoe() {
        return enemiesInsideAoe;
    }

    public int getLastAoeAttack() {
        return lastAoeAttack;
    }

    public void setLastAoeAttack(int lastAoeAttack) {
        this.lastAoeAttack = lastAoeAttack;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public boolean isHovering() {
        return false;
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
        return id;
    }
    @Override
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getVerticesNumber() {
        return verticesNumber;
    }

    public void setVerticesNumber(int verticesNumber) {
        this.verticesNumber = verticesNumber;
    }

    public int getXp() {
        return xp;
    }

    public ArrayList<PanelModel> getMainPanels() {
        return mainPanels;
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
    @Override
    public boolean isStationed() {
        return false;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ArrayList<Point2D> getVertices() {
        return vertices;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }
}
