package model.charactersModel.boss;

import controller.Controller;
import controller.Pair;
import controller.Utils;
import controller.constant.Constants;
import model.bulletModel.NonRigidBulletModel;
import model.bulletModel.RigidBulletModel;
import model.charactersModel.Enemy;
import model.charactersModel.EpsilonModel;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import model.panelModel.Isometric;
import model.panelModel.PanelModel;
import model.panelModel.Rigid;
import view.charecterViews.bossView.BossHeadView;
import view.charecterViews.bossView.BossRightHandView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class BossHead implements Movable, Collidable {
    private final String id;
    private double size;
    private double speed;
    private int hp;
    private boolean canInjure;
    private Point2D center;
    private Direction direction;
    private ArrayList<Point2D> vertices;
    private PanelModel headPanel;
    private boolean dead;
    private ArrayList<Pair<EpsilonModel, Integer>> epsilonsInsideAoe;
    private ArrayList<Point2D> aoeCenters;
    public static ArrayList<BossHead> bossHeadsList = new ArrayList<>();

    public BossHead(Point2D center, double size) {
        this.center = center;
        this.size = size;
        this.id = Utils.processRandomId();
        this.speed = 0;
        this.hp = Constants.BOSS_HEAD_HP;
        this.canInjure = false;
        this.dead = false;
        epsilonsInsideAoe = new ArrayList<>();
        aoeCenters = new ArrayList<>();
        vertices = new ArrayList<>();
        direction = new Direction(new Point2D.Double(0, 0));
        calculateVertices();
        makePanel();
        Controller.getINSTANCE().createBossHeadView(id);
        Collidable.collidables.add(this);
        bossHeadsList.add(this);
        Movable.movable.add(this);
    }

    public void calculateVertices() {
        vertices.clear();
        ArrayList<Point2D> ver = Utils.calculateSquareVertices(center, size);
        vertices.addAll(ver);
    }

    private void makePanel() {
        headPanel = new PanelModel(vertices.get(0).getX(), vertices.get(0).getY(), size, size, Isometric.NO, Rigid.NO);
    }

    public void shootBullets() {
        ArrayList<Point2D> targets = Utils.circlePartition(center, size, BOSS_RAPID_FIRE_ATTACK_NUMBER);
        for (Point2D target : targets) {
            Direction bulletDir = new Direction(new Point2D.Double(target.getX() - center.getX(), target.getY() - center.getY()));
            new NonRigidBulletModel(target, bulletDir, BOSS_RAPID_FIRE_ATTACK_REDUCE_HP, id);
        }
    }

    public void updateEpsilonInsideAoe(int time) {
        for (EpsilonModel epsilon : EpsilonModel.epsilonModelsList) {
            boolean inside = false;
            for (Point2D aoeCenter : aoeCenters) {
                if (aoeCenter.distance(epsilon.getCenter()) <= Constants.BOSS_AOE_RADIUS - epsilon.getRadius()) {
                    inside = true;
                }
            }
            if (inside) {
                if (!Utils.pairArrayContainsKey(epsilonsInsideAoe, epsilon)) epsilonsInsideAoe.add(new Pair<>(epsilon, time));
            }
            else Utils.pairArrayRemoveKey(epsilonsInsideAoe, epsilon);
        }
    }
    public ArrayList<Point2D> getAoeCenters() {
        return aoeCenters;
    }

    public void setAoeCenters(ArrayList<Point2D> aoeCenters) {
        this.aoeCenters = aoeCenters;
    }

    public ArrayList<Pair<EpsilonModel, Integer>> getEpsilonsInsideAoe() {
        return epsilonsInsideAoe;
    }

    public PanelModel getHeadPanel() {
        return headPanel;
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    public static void removeFromAllList(String id) {
        for (int i = 0; i < bossHeadsList.size(); i++) {
            if (bossHeadsList.get(i).getId().equals(id)) {
                bossHeadsList.remove(i);
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
        for (int i = 0; i < BossHeadView.bossHeadViewsList.size(); i++) {
            if (BossHeadView.bossHeadViewsList.get(i).getId().equals(id)) {
                BossHeadView.bossHeadViewsList.remove(i);
                break;
            }
        }
    }
    public void removePanel() {
        PanelModel.removeFromAllList(headPanel.getId());
    }
}
