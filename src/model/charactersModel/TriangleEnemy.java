package model.charactersModel;

import controller.Controller;
import controller.constant.Constants;
import model.collision.Collidable;
import model.movement.Direction;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class TriangleEnemy implements Collidable {
    private final String id;
    double size;
    private final int hp, reducerHp, collectibleNumber, collectibleXp;
    Point2D center;
    Direction direction;
    ArrayList<Point2D> vertices;
    public static ArrayList<TriangleEnemy> triangleEnemyList = new ArrayList<>();

    public TriangleEnemy(Point2D center, double size) {
        this.center = center;
        this.size = size;
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
    }
    private void calculateVertices() {
        Point2D upVer = new Point2D.Double(center.getX(), center.getY() - ((Math.sqrt(3) / 3.0) * size));
        Point2D leftVer = new Point2D.Double(center.getX() - (size / 2), center.getY() + ((Math.sqrt(3) / 6.0) * size));
        Point2D rightVer = new Point2D.Double(center.getX() + (size / 2), center.getY() + ((Math.sqrt(3) / 6.0) * size));
        vertices.add(upVer);
        vertices.add(leftVer);
        vertices.add(rightVer);
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
    public Point2D getCenter() {
        return center;
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
}