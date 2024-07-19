package model.charactersModel;

import controller.Controller;
import controller.Utils;
import controller.random.RandomHelper;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import org.jetbrains.annotations.NotNull;
import view.charecterViews.SquareEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

import static controller.constant.Constants.*;

public class SquareEnemy extends Enemy {
    private boolean isDash;
    public static ArrayList<SquareEnemy> squareEnemyList = new ArrayList<>();

    public SquareEnemy(Point2D center, double size, double speed) {
        super(Utils.processRandomId(), size, speed, speed, false, SQUARE_ENEMY_HP, SQUARE_ENEMY_REDUCER_HP, SQUARE_ENEMY_COLLECTIBLE_NUMBER, SQUARE_ENEMY_COLLECTIBLE_XP,
            center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        isDash = false;
        calculateVertices();
        Controller.getINSTANCE().createSquareEnemyView(super.getId());
        Collidable.collidables.add(this);
        squareEnemyList.add(this);
        Movable.movable.add(this);
    }

    @Override
    public void calculateVertices() {
        super.getVertices().clear();
        ArrayList<Point2D> ver = Utils.calculateSquareVertices(super.getCenter(), super.getSize());
        for (Point2D ptr : ver) super.getVertices().add(ptr);
    }

    @Override
    public void updateDirection(@NotNull Point2D point) {
        Point2D delta = new Point2D.Double(point.getX() - super.getCenter().getX(), point.getY() - super.getCenter().getY());
        Direction toPoint = new Direction(delta);
        if (super.isImpact() && super.getSpeed() > 0.25) {
            super.setSpeed(super.getSpeed() - (super.getSpeed() / 10));
            return;
        }
        if (super.isImpact()) super.setImpact(false);
        super.setDirection(new Direction(toPoint.getDirectionVector()));
        super.setSpeed(Math.min(super.getSpeed() + (super.getSpeed() / 10), super.getInitialSpeed()));
    }
    public void updateSpeed() {
        if (!super.isImpact()) {
            isDash = RandomHelper.squareEnemyDash();
            if (isDash) super.setSpeed(RandomHelper.squareEnemySpeed(super.getSpeed()));
            else {
                isDash = RandomHelper.squareEnemyDash();
                if (!isDash) super.setSpeed(super.getInitialSpeed());
            }
        }
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < squareEnemyList.size(); i++) {
            if (squareEnemyList.get(i).getId().equals(id)) {
                squareEnemyList.remove(i);
                break;
            }
        }
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            if (Collidable.collidables.get(i).getId() != null && Collidable.collidables.get(i).getId().equals(id)) {
                Collidable.collidables.remove(i);
                break;
            }
        }
        for (int i = 0; i < Movable.movable.size(); i++) {
            if (Movable.movable.get(i).getId() != null && Movable.movable.get(i).getId().equals(id)) {
                Movable.movable.remove(i);
                break;
            }
        }
        for (int i = 0; i < SquareEnemyView.squareEnemyViewList.size(); i++) {
            if (SquareEnemyView.squareEnemyViewList.get(i).getId().equals(id)) {
                SquareEnemyView.squareEnemyViewList.remove(i);
                break;
            }
        }
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isHovering() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public boolean isStationed() {
        return false;
    }
}
