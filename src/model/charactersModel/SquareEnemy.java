package model.charactersModel;

import controller.Controller;
import controller.Utils;
import controller.constant.Constants;
import controller.random.RandomHelper;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SquareEnemy implements Collidable, Movable {
    private final String id;
    double size;
    double speed;
    boolean isDash;
    int hp;
    private final int reducerHp, collectibleNumber, collectibleXp;
    Point2D center;
    Direction direction;
    ArrayList<Point2D> vertices;
    public static ArrayList<SquareEnemy> squareEnemyList = new ArrayList<>();

    public SquareEnemy(Point2D center, double size, double speed) {
        this.center = center;
        this.size = size;
        this.speed = speed;
        isDash = false;
        this.id = UUID.randomUUID().toString();
        this.direction = new Direction(new Point2D.Double(0, 0));
        this.hp = Constants.SQUARE_ENEMY_HP;
        this.reducerHp = Constants.SQUARE_ENEMY_REDUCER_HP;
        this.collectibleNumber = Constants.SQUARE_ENEMY_COLLECTIBLE_NUMBER;
        this.collectibleXp = Constants.SQUARE_ENEMY_COLLECTIBLE_XP;
        vertices = new ArrayList<>();
        calculateVertices();
        Controller.getINSTANCE().createSquareEnemyView(id);
        Collidable.collidables.add(this);
        squareEnemyList.add(this);
        Movable.movable.add(this);
    }

    public void calculateVertices() {
        Point2D leftUp = new Point2D.Double(center.getX() - (size / 2), center.getY() - (size / 2));
        Point2D rightUp = new Point2D.Double(center.getX() + (size / 2), center.getY() - (size / 2));
        Point2D rightDown = new Point2D.Double(center.getX() + (size / 2), center.getY() + (size / 2));
        Point2D leftDown = new Point2D.Double(center.getX() - (size / 2), center.getY() + (size / 2));
        vertices.add(leftUp);
        vertices.add(rightUp);
        vertices.add(rightDown);
        vertices.add(leftDown);
    }

    public void updateDirection(Point2D point) {
        Point2D delta = new Point2D.Double(point.getX() - center.getX(), point.getY() - center.getY());
        Direction toPoint = new Direction(delta);
        direction = new Direction(Utils.addVectors(direction.getDirectionVector(), toPoint.getDirectionVector()));
    }

    public void updateSpeed() {
        isDash = RandomHelper.squareEnemyDash();
        if (isDash) speed = RandomHelper.squareEnemySpeed(speed);
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
    public boolean isCircular() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SquareEnemy that = (SquareEnemy) o;
        return Objects.equals(id, that.id);
    }
}
