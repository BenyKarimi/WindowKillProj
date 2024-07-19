package model.charactersModel;

import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Enemy implements Collidable, Movable {
    private final String id;
    private double size;
    private double speed, initialSpeed;
    private boolean isImpact;
    private int hp;
    private final int reducerHp, collectibleNumber, collectibleXp;
    private Point2D center;
    private Direction direction;
    private ArrayList<Point2D> vertices;

    public Enemy(String id, double size, double speed, double initialSpeed, boolean isImpact, int hp, int reducerHp, int collectibleNumber, int collectibleXp, Point2D center, Direction direction, ArrayList<Point2D> vertices) {
        this.id = id;
        this.size = size;
        this.speed = speed;
        this.initialSpeed = initialSpeed;
        this.isImpact = isImpact;
        this.hp = hp;
        this.reducerHp = reducerHp;
        this.collectibleNumber = collectibleNumber;
        this.collectibleXp = collectibleXp;
        this.center = center;
        this.direction = direction;
        this.vertices = vertices;
    }

    public abstract void calculateVertices();
    public abstract void updateDirection(Point2D point);

    @Override
    public String getId() {
        return id;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getInitialSpeed() {
        return initialSpeed;
    }

    public void setInitialSpeed(double initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public boolean isImpact() {
        return isImpact;
    }

    public void setImpact(boolean impact) {
        isImpact = impact;
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

    @Override
    public Point2D getCenter() {
        return center;
    }

    @Override
    public void setCenter(Point2D center) {
        this.center = center;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public ArrayList<Point2D> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Point2D> vertices) {
        this.vertices = vertices;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enemy that = (Enemy) o;
        return Objects.equals(id, that.id);
    }
}
