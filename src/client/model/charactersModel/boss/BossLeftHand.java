package client.model.charactersModel.boss;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.controller.constant.Constants;
import client.model.bulletModel.NonRigidBulletModel;
import client.model.collision.Collidable;
import client.model.movement.Direction;
import client.model.movement.Movable;
import client.model.panelModel.Isometric;
import client.model.panelModel.PanelModel;
import client.model.panelModel.Rigid;
import client.view.charecterViews.bossView.BossLeftHandView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static client.controller.constant.Constants.BOSS_BULLET_REDUCE_HP;

public class BossLeftHand implements Movable, Collidable {
    private final String id;
    private double size;
    private double speed;
    private int hp;
    private boolean canInjure;
    private Point2D center;
    private Direction direction;
    private ArrayList<Point2D> vertices;
    private PanelModel leftHandPanel;
    public static ArrayList<BossLeftHand> bossLeftHandsList = new ArrayList<>();

    public BossLeftHand(Point2D center, double size) {
        this.center = center;
        this.size = size;
        this.id = Utils.processRandomId();
        this.speed = 0;
        this.hp = Constants.BOSS_HAND_HP;
        this.canInjure = false;
        direction = new Direction(new Point2D.Double(0, 0));
        vertices = new ArrayList<>();
        calculateVertices();
        makePanel();
        Controller.getINSTANCE().createBossLeftHandView(id);
        Collidable.collidables.add(this);
        bossLeftHandsList.add(this);
        Movable.movable.add(this);
    }

    public void calculateVertices() {
        vertices.clear();
        ArrayList<Point2D> ver = Utils.calculateSquareVertices(center, size);
        vertices.addAll(ver);
    }
    private void makePanel() {
        leftHandPanel = new PanelModel(vertices.get(0).getX(), vertices.get(0).getY(), size, size, Isometric.NO, Rigid.NO);
    }

    public void makeAttack(Point2D epsilonCenter) {
        Direction bulletDirection = new Direction(new Point2D.Double(epsilonCenter.getX() - center.getX(), epsilonCenter.getY() - center.getY()));
        Point2D bulletCenter = Utils.aimAndBulletDrawerCalculator(epsilonCenter, center, size);
        new NonRigidBulletModel(bulletCenter, bulletDirection, BOSS_BULLET_REDUCE_HP, id);
    }

    public PanelModel getLeftHandPanel() {
        return leftHandPanel;
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
        for (int i = 0; i < bossLeftHandsList.size(); i++) {
            if (bossLeftHandsList.get(i).getId().equals(id)) {
                bossLeftHandsList.remove(i);
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
        for (int i = 0; i < BossLeftHandView.bossLeftHandViewsList.size(); i++) {
            if (BossLeftHandView.bossLeftHandViewsList.get(i).getId().equals(id)) {
                BossLeftHandView.bossLeftHandViewsList.remove(i);
                break;
            }
        }
    }
    public void removePanel() {
        PanelModel.removeFromAllList(leftHandPanel.getId());
    }
}
