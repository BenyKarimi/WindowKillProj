package model.charactersModel.boss;

import controller.Controller;
import controller.Utils;
import controller.constant.Constants;
import model.charactersModel.Enemy;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import model.panelModel.Isometric;
import model.panelModel.PanelModel;
import model.panelModel.Rigid;
import view.charecterViews.TriangleEnemyView;
import view.charecterViews.bossView.BossRightHandView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BossRightHand implements Movable, Collidable {
    private final String id;
    private double size;
    private double speed;
    private int hp;
    private boolean canInjure;
    private Point2D center;
    private Direction direction;
    private ArrayList<Point2D> vertices;
    private PanelModel rightHandPanel;
    public static ArrayList<BossRightHand> bossRightHandsList = new ArrayList<>();

    public BossRightHand(Point2D center, double size) {
        this.center = center;
        this.size = size;
        this.id = Utils.processRandomId();
        this.speed = 0;
        this.hp = Constants.BOSS_HEAD_HP;
        this.canInjure = false;
        direction = new Direction(new Point2D.Double(0, 0));
        vertices = new ArrayList<>();
        calculateVertices();
        makePanel();
        Controller.getINSTANCE().createBossRightHandView(id);
        Collidable.collidables.add(this);
        bossRightHandsList.add(this);
        Movable.movable.add(this);
    }

    public void calculateVertices() {
        vertices.clear();
        ArrayList<Point2D> ver = Utils.calculateSquareVertices(center, size);
        vertices.addAll(ver);
    }
    private void makePanel() {
        rightHandPanel = new PanelModel(vertices.get(0).getX(), vertices.get(0).getY(), size, size, Isometric.NO, Rigid.NO);
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
    public ArrayList<Point2D> getVertices() {
        return vertices;
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
    public String getId() {
        return id;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean isStationed() {
        return false;
    }

    public double getSize() {
        return size;
    }

    public int getHp() {
        return hp;
    }

    public boolean isCanInjure() {
        return canInjure;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setCanInjure(boolean canInjure) {
        this.canInjure = canInjure;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public static void removeFromAllList(String id) {
        for (int i = 0; i < bossRightHandsList.size(); i++) {
            if (bossRightHandsList.get(i).getId().equals(id)) {
                bossRightHandsList.remove(i);
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
        for (int i = 0; i < BossRightHandView.bossRightHandViewsList.size(); i++) {
            if (BossRightHandView.bossRightHandViewsList.get(i).getId().equals(id)) {
                BossRightHandView.bossRightHandViewsList.remove(i);
                break;
            }
        }
    }
    public void removePanel() {
        PanelModel.removeFromAllList(rightHandPanel.getId());
    }
}
