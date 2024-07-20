package model.charactersModel;


import controller.Utils;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import model.panelModel.Isometric;
import model.panelModel.PanelModel;
import model.panelModel.Rigid;
import view.charecterViews.WyrmEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class OrbEnemy extends Enemy {

    private PanelModel orbPanel;
    private BlackOrbMiniBoss parentBoss;
    public static ArrayList<OrbEnemy> orbEnemiesList = new ArrayList<>();

    public OrbEnemy(Point2D center, double size, double speed, BlackOrbMiniBoss parentBoss) {
        super(Utils.processRandomId(), size, speed, speed, false, ORB_ENEMY_HP, ORB_ENEMY_REDUCER_HP, ORB_ENEMY_COLLECTIBLE_NUMBER, ORB_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        this.parentBoss = parentBoss;
        calculateVertices();
        makePanel();
        Collidable.collidables.add(this);
        orbEnemiesList.add(this);
        Movable.movable.add(this);
    }

    @Override
    public void calculateVertices() {
        super.getVertices().clear();
        ArrayList<Point2D> ver = Utils.circlePartition(super.getCenter(), super.getSize(), 36);
        for (Point2D ptr : ver) super.getVertices().add(ptr);
    }

    @Override
    public void updateDirection(Point2D point) {
        super.setDirection(new Direction(new Point2D.Double(0, 0)));
        super.setSpeed(0);
    }

    private void makePanel() {
        orbPanel = new PanelModel(super.getCenter().getX() - super.getSize() * 2, super.getCenter().getY() - super.getSize() * 2, super.getSize() * 4, super.getSize() * 4, Isometric.YES, Rigid.NO, true);
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < orbEnemiesList.size(); i++) {
            if (orbEnemiesList.get(i).getId().equals(id)) {
                orbEnemiesList.remove(i);
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
    }
    public void removeFromParentBoss() {
        parentBoss.removeEnemy(this);
    }

    public PanelModel getOrbPanel() {
        return orbPanel;
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
        return getSize();
    }

    @Override
    public boolean isStationed() {
        return true;
    }
}
