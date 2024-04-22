package model.charactersModel;

import controller.Controller;
import controller.Utils;
import controller.constant.Constants;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class TriangleEnemy implements Collidable, Movable {
    private final String id;
    double size;
    boolean isImpact;
    double speed, initialSpeed;
    int hp;
    private final int reducerHp, collectibleNumber, collectibleXp;
    Point2D center;
    Direction direction;
    ArrayList<Point2D> vertices;
    public static ArrayList<TriangleEnemy> triangleEnemyList = new ArrayList<>();

    public TriangleEnemy(Point2D center, double size, double speed) {
        this.center = center;
        this.size = size;
        this.speed = speed;
        this.initialSpeed = speed;
        this.id = UUID.randomUUID().toString();
        this.direction = new Direction(new Point2D.Double(0, 0));
        this.hp = Constants.TRIANGLE_ENEMY_HP;
        this.reducerHp = Constants.TRIANGLE_ENEMY_REDUCER_HP;
        this.collectibleNumber = Constants.TRIANGLE_ENEMY_COLLECTIBLE_NUMBER;
        this.collectibleXp = Constants.TRIANGLE_ENEMY_COLLECTIBLE_XP;
        vertices = new ArrayList<>();
        calculateVertices();
        Controller.getINSTANCE().createTriangleEnemyView(id);
        Collidable.collidables.add(this);
        triangleEnemyList.add(this);
        Movable.movable.add(this);
    }
    public void calculateVertices() {
        vertices.clear();
        Point2D upVer = new Point2D.Double(center.getX(), center.getY() - ((Math.sqrt(3) / 3.0) * size));
        Point2D leftVer = new Point2D.Double(center.getX() - (size / 2), center.getY() + ((Math.sqrt(3) / 6.0) * size));
        Point2D rightVer = new Point2D.Double(center.getX() + (size / 2), center.getY() + ((Math.sqrt(3) / 6.0) * size));
        vertices.add(upVer);
        vertices.add(leftVer);
        vertices.add(rightVer);
    }

    public void updateDirection(Point2D point) {
        Point2D delta = new Point2D.Double(point.getX() - center.getX(), point.getY() - center.getY());
        Direction toPoint = new Direction(delta);
        if (isImpact && speed > 0.25) {
            speed -= (speed / 10);
            return;
        }
        if (isImpact) isImpact = false;
        direction = new Direction(toPoint.getDirectionVector());
        speed = Math.min(speed + (speed / 10), initialSpeed);
    }
    public void updateSpeed(Point2D point) {
        double distance = center.distance(point);
        if (speed > initialSpeed) speed = initialSpeed;
        if (distance >= 200.0) speed *= 2;
    }
    @Override
    public boolean isCircular() {
        return false;
    }
    @Override
    public double getRadius() {
        return 0;
    }

    public void setImpact(boolean impact) {
        isImpact = impact;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void setCenter(Point2D center) {
        this.center = center;
    }

    @Override
    public Point2D getCenter() {
        return center;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public ArrayList<Point2D> getVertices() {
        return vertices;
    }
    public String getId() {
        return id;
    }

    public double getSize() {
        return size;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getReducerHp() {
        return reducerHp;
    }

    public int getCollectibleNumber() {
        return collectibleNumber;
    }

    public int getCollectibleXp() {
        return collectibleXp;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriangleEnemy that = (TriangleEnemy) o;
        return Objects.equals(id, that.id);
    }
}