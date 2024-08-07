package client.model.charactersModel;

import client.controller.random.RandomHelper;
import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.model.bulletModel.NonRigidBulletModel;
import client.model.collision.Collidable;
import client.model.movement.Direction;
import client.model.movement.Movable;
import client.model.panelModel.PanelModel;
import client.model.panelModel.WallSideIndicator;
import client.view.charecterViews.OmenoctEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static client.controller.constant.Constants.*;

public class OmenoctEnemy extends Enemy {

    private int lastAttackTime;
    private PanelModel currentPanel;
    private WallSideIndicator wallSideIndicator;
    public static ArrayList<OmenoctEnemy> omenoctEnemyList = new ArrayList<>();

    public OmenoctEnemy(Point2D center, double size, double speed) {
        super(Utils.processRandomId(), size, speed, speed, false, OMENOCT_ENEMY_HP, OMENOCT_ENEMY_REDUCER_HP, OMENOCT_ENEMY_COLLECTIBLE_NUMBER, OMENOCT_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        this.wallSideIndicator = RandomHelper.omenoctWallSide();
        calculateVertices();
        Controller.getINSTANCE().createOmenoctEnemyView(super.getId());
        Collidable.collidables.add(this);
        omenoctEnemyList.add(this);
        Movable.movable.add(this);
    }

    @Override
    public void calculateVertices() {
        super.getVertices().clear();

        double angleStep = Math.PI / 8;
        double rad = super.getSize() / (2 * Math.cos(angleStep));

        for (int i = 1; i < 16; i += 2) {
            double angle = angleStep * i;

            double x = super.getCenter().getX() + rad * Math.cos(angle);
            double y = super.getCenter().getY() + rad * Math.sin(angle);

            super.getVertices().add(new Point2D.Double(x, y));
        }
    }

    @Override
    public void updateDirection(Point2D point) {
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
    public void makeAttack(int time, Point2D epsilonCenter) {
        if (time - lastAttackTime >= 2) {
            Direction bulletDirection = new Direction(new Point2D.Double(epsilonCenter.getX() - super.getCenter().getX(), epsilonCenter.getY() - super.getCenter().getY()));
            Point2D bulletCenter = Utils.aimAndBulletDrawerCalculator(epsilonCenter, super.getCenter(), super.getSize());
            new NonRigidBulletModel(bulletCenter, bulletDirection, OMENOCT_BULLET_REDUCE_HP, super.getId());
            lastAttackTime = time;
        }
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < omenoctEnemyList.size(); i++) {
            if (omenoctEnemyList.get(i).getId().equals(id)) {
                omenoctEnemyList.remove(i);
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
        for (int i = 0; i < OmenoctEnemyView.omenoctEnemyViewList.size(); i++) {
            if (OmenoctEnemyView.omenoctEnemyViewList.get(i).getId().equals(id)) {
                OmenoctEnemyView.omenoctEnemyViewList.remove(i);
                break;
            }
        }
    }

    public Point2D findTargetPanel(ArrayList<PanelModel> candidatePanels, Point2D def) {
        boolean ok = false;
        for (PanelModel panel : candidatePanels) {
            if (currentPanel != null && this.currentPanel.equals(panel)) {
                ok = true;
                break;
            }
        }
        if (!ok && candidatePanels.size() > 0) this.currentPanel = candidatePanels.get(0);

        Point2D out = null;

        if (currentPanel == null) return def;

        if (wallSideIndicator.equals(WallSideIndicator.LEFT)) {
            if (isConnectedToWall(wallSideIndicator, currentPanel.getX())) out = super.getCenter();
            else out = new Point2D.Double(currentPanel.getX() + super.getSize() / 2, currentPanel.getY() + currentPanel.getHeight() / 2);
        }
        if (wallSideIndicator.equals(WallSideIndicator.UP)) {
            if (isConnectedToWall(wallSideIndicator, currentPanel.getY())) out = super.getCenter();
            else out = new Point2D.Double(currentPanel.getX() + currentPanel.getWidth() / 2, currentPanel.getY() + super.getSize() / 2);
        }
        if (wallSideIndicator.equals(WallSideIndicator.RIGHT)) {
            if (isConnectedToWall(wallSideIndicator, currentPanel.getX() + currentPanel.getWidth())) out = super.getCenter();
            else out = new Point2D.Double(currentPanel.getX() + currentPanel.getWidth() - super.getSize() / 2, currentPanel.getY() + currentPanel.getHeight() / 2);
        }
        if (wallSideIndicator.equals(WallSideIndicator.DOWN)) {
            if (isConnectedToWall(wallSideIndicator, currentPanel.getY() + currentPanel.getHeight())) out = super.getCenter();
            else out = new Point2D.Double(currentPanel.getX() + currentPanel.getWidth() / 2, currentPanel.getY() + currentPanel.getHeight() - super.getSize() / 2);
        }
        return out;
    }
    public boolean isConnectedToWall(WallSideIndicator wallSideIndicator, double pl) {
        if (wallSideIndicator.equals(WallSideIndicator.UP)) {
            return Utils.approxEqual(this.getCenter().getY() - super.getSize() / 2, pl);
        }
        if (wallSideIndicator.equals((WallSideIndicator.DOWN))) {
            return Utils.approxEqual(this.getCenter().getY() + super.getSize() / 2, pl);
        }
        if (wallSideIndicator.equals((WallSideIndicator.RIGHT))) {
            return Utils.approxEqual(this.getCenter().getX() + super.getSize() / 2, pl);
        }
        if (wallSideIndicator.equals((WallSideIndicator.LEFT))) {
            return  Utils.approxEqual(this.getCenter().getX() - super.getSize() / 2, pl);
        }
        return false;
    }

    public void setWallSideIndicator(WallSideIndicator wallSideIndicator) {
        this.wallSideIndicator = wallSideIndicator;
    }

    @Override
    public double getRadius() {
        return 0;
    }
    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isHovering() {
        return false;
    }

    public PanelModel getCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(PanelModel currentPanel) {
        this.currentPanel = currentPanel;
    }

    @Override
    public boolean isStationed() {
        return false;
    }

    public WallSideIndicator getWallSideIndicator() {
        return wallSideIndicator;
    }
}
