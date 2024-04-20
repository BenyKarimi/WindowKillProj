package model.charactersModel;

import controller.Controller;
import controller.constant.Constants;
import model.collision.Collidable;
import model.movement.Direction;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class SquareEnemy implements Collidable {
    private final String id;
    double size;
    private final int hp, reducerHp, collectibleNumber, collectibleXp;
    Point2D center;
    Direction direction;
    ArrayList<Point2D> vertices;
    public static ArrayList<SquareEnemy> squareEnemyList = new ArrayList<>();

    public SquareEnemy(Point2D center, double size) {
        this.center = center;
        this.size = size;
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
    }
    public void calculateVertices() {

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
}
