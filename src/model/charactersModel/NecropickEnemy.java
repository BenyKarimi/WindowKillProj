package model.charactersModel;

import controller.Controller;
import controller.Utils;
import model.bulletModel.RigidBulletModel;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import org.jetbrains.annotations.NotNull;
import view.charecterViews.NecropickEnemyView;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static controller.constant.Constants.*;

public class NecropickEnemy extends Enemy {
    private StationedType stationedType;
    private int lastTypeTime;
    private boolean hovering;
    public static ArrayList<NecropickEnemy> necropickEnemiesList = new ArrayList<>();

    public NecropickEnemy(Point2D center, double size, double speed) {
        super(Utils.processRandomId(), size, speed, speed, false, NECROPICK_ENEMY_HP, NECROPICK_ENEMY_REDUCER_HP, NECROPICK_ENEMY_COLLECTIBLE_NUMBER, NECROPICK_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        stationedType = StationedType.HIDE;
        calculateVertices();
        Controller.getINSTANCE().createNecropickEnemyView(super.getId());
        Collidable.collidables.add(this);
        necropickEnemiesList.add(this);
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
    public void updateSpeed(Point2D point) {
        double distance = super.getCenter().distance(point);
        if (distance <= NECROPICK_EPSILON_MIN_DISTANCE) super.setSpeed(0);
        else super.setSpeed(super.getInitialSpeed());
    }

    private void shootBullets() {
        ArrayList<Point2D> targets = Utils.circlePartition(super.getCenter(), super.getSize(), NECROPICK_RANGE_ATTACK_NUMBER);
        for (Point2D target : targets) {
                Direction bulletDir = new Direction(new Point2D.Double(target.getX() - super.getCenter().getX(), target.getY() - super.getCenter().getY()));
            if (bulletDir.getDirectionVector().equals(new Point2D.Double(0, 0))) {
                System.out.println((target.getX() - super.getCenter().getX() )+ " " + (target.getY() - super.getCenter().getY()));
            }
            new RigidBulletModel(target, bulletDir, NECROPICK_RANGE_ATTACK_REDUCE_HP, super.getId(), 3);
        }
    }
    public void updateStation(int time) {
        if (time - lastTypeTime >= 3600 && stationedType.equals(StationedType.HIDE)) {
            stationedType = StationedType.PRE_SHOW;
            lastTypeTime = time;
        }
        else if (time - lastTypeTime >= 400 && stationedType.equals(StationedType.PRE_SHOW)) {
            stationedType = StationedType.SHOW;
            hovering = false;
            shootBullets();
            lastTypeTime = time;
        }
        else if (time - lastTypeTime >= 4000 && stationedType.equals(StationedType.SHOW)) {
            stationedType = StationedType.HIDE;
            hovering = true;
            lastTypeTime = time;
        }
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < necropickEnemiesList.size(); i++) {
            if (necropickEnemiesList.get(i).getId().equals(id)) {
                necropickEnemiesList.remove(i);
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
        for (int i = 0; i < Enemy.enemiesList.size(); i++) {
            if (Enemy.enemiesList.get(i).getId().equals(id)) {
                Enemy.enemiesList.remove(i);
                break;
            }
        }
        for (int i = 0; i < NecropickEnemyView.necropickEnemyViewsList.size(); i++) {
            if (NecropickEnemyView.necropickEnemyViewsList.get(i).getId().equals(id)) {
                NecropickEnemyView.necropickEnemyViewsList.remove(i);
                break;
            }
        }
    }

    public StationedType getStationedType() {
        return stationedType;
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isHovering() {
        return hovering;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public boolean isStationed() {
        return stationedType.equals(StationedType.SHOW);
    }
}
