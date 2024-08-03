package client.model.panelModel;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.controller.constant.Constants;
import client.model.bulletModel.RigidBulletModel;
import client.model.charactersModel.EpsilonModel;
import client.model.movement.Direction;
import client.model.movement.Movable;
import client.view.container.GamePanel;
import client.view.container.GlassFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

public class PanelModel implements Movable {
    private double x;
    private double y;
    private double width;
    private double height;
    private double leftSpeed = 0;
    private double bulletRelatedLeftSpeed = 0;
    private double rightSpeed = 0, bulletRelatedRightSpeed = 0;
    private double upSpeed = 0, bulletRelatedUpSpeed = 0;
    private double downSpeed = 0, bulletRelatedDownSpeed = 0;
    public ArrayList<Double> leftAccel = new ArrayList<>();
    public ArrayList<Double> rightAccel = new ArrayList<>();
    public ArrayList<Double> upAccel = new ArrayList<>();
    public ArrayList<Double> downAccel = new ArrayList<>();
    private Isometric isometric;
    private Rigid rigid;
    private final String id;
    private Direction direction;
    private double speed;
    private Point2D center;
    public static ArrayList<PanelModel> panelModelList = new ArrayList<>();

    public PanelModel(double x, double y, double width, double height, Isometric isometric, Rigid rigid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isometric = isometric;
        this.rigid = rigid;
        this.direction = new Direction(new Point2D.Double(0, 0));
        this.speed = 0;
        this.id = Utils.processRandomId();
        updateCenter();
        Controller.getINSTANCE().createGamePanel(id, x, y, width, height);
        panelModelList.add(this);
        Movable.movable.add(this);
    }

    public PanelModel(Dimension size, Isometric isometric, Rigid rigid) {
        this((double) GlassFrame.getINSTANCE().getWidth() / 2 - (double) size.width / 2, (double) GlassFrame.getINSTANCE().getHeight() / 2 - (double) size.height / 2,
                size.width, size.height, isometric, rigid);
    }
    public void changeShrinkByCircle(Point2D center, double radius) {
        if (Utils.coveringPanels(PanelModel.panelModelList, center, radius).size() == 0) {
            ArrayList<Point2D> sphere = Utils.circlePca(center, radius);
            boolean hasPointInside = false;
            for (Point2D point : sphere) {
                if (new Rectangle((int) x, (int) y, (int) width, (int) height).contains(point)) {
                    hasPointInside = true;
                }
            }

            if (hasPointInside) {
                for (Point2D point : sphere) {
                    if (!(new Rectangle((int) x, (int) y, (int) width, (int) height).contains(point))) {
                        if (point.getX() < x) {
                            leftSpeed = 0;
                        }
                        if (point.getX() > x + width) {
                            rightSpeed = 0;
                        }
                        if (point.getY() < y) {
                            upSpeed = 0;
                        }
                        if (point.getY() > y + height) {
                            downSpeed = 0;
                        }
                    }
                }
            }
        }
    }
    private boolean haveMinimumSize(boolean wonGame) {
        if (wonGame) {
            return true;
        }
        return !(height < Constants.GAME_PANEL_MIN_DIMENSION.height) && !(width < Constants.GAME_PANEL_MIN_DIMENSION.width);
    }
    public void startShrinkValue() {
        leftSpeed = (width - Constants.GAME_PANEL_START_DIMENSION.width) / Constants.SHRINK_DECELERATION;
        rightSpeed = (width - Constants.GAME_PANEL_START_DIMENSION.width) / Constants.SHRINK_DECELERATION;

        upSpeed = (height - Constants.GAME_PANEL_START_DIMENSION.height) / Constants.SHRINK_DECELERATION;
        downSpeed = (height - Constants.GAME_PANEL_START_DIMENSION.height) / Constants.SHRINK_DECELERATION;
    }
    public void inGameShrinkValue() {
        leftSpeed = ((width - Constants.GAME_PANEL_MIN_DIMENSION.width) / Constants.SHRINK_DECELERATION);
        rightSpeed = ((width - Constants.GAME_PANEL_MIN_DIMENSION.width) / Constants.SHRINK_DECELERATION);

        upSpeed = ((height - Constants.GAME_PANEL_MIN_DIMENSION.height) / Constants.SHRINK_DECELERATION);
        downSpeed = ((height - Constants.GAME_PANEL_MIN_DIMENSION.height) / Constants.SHRINK_DECELERATION);

        changeShrinkByCircle(EpsilonModel.epsilonModelsList.get(0).getCenter(), EpsilonModel.epsilonModelsList.get(0).getRadius());

        for(RigidBulletModel bullet : RigidBulletModel.rigidBulletModelList)
            changeShrinkByCircle(bullet.getCenter(), bullet.getRadius());

    }
    public void endGameShrinkValue() {
        leftSpeed = width / Constants.SHRINK_DECELERATION;
        rightSpeed = width / Constants.SHRINK_DECELERATION;

        upSpeed = height / Constants.SHRINK_DECELERATION;
        downSpeed = height / Constants.SHRINK_DECELERATION;
    }
    public boolean collidesWithOtherPanel() {
        for(PanelModel panel : panelModelList) {
            if ((!this.equals(panel) && new Rectangle((int)x, (int)y, (int)width, (int)height).intersects(new Rectangle((int)panel.getX(), (int)panel.getY(), (int)panel.getWidth(), (int)panel.getHeight())))
                && (rigid.equals(Rigid.YES) || panel.rigid.equals(Rigid.YES)))
                return true;
        }

        return false;
    }
    public void updateCenter() {
        center = new Point2D.Double(x + width / 2, y + height / 2);
    }
    private void updateLeftLocationWithCenter() {
        x = center.getX() - width / 2;
        y = center.getY() - height / 2;
    }
    public void shrink(boolean wonGame) {
        if (!leftAccel.isEmpty()) {
            for(int i = 0; i < leftAccel.size(); i++) {
                bulletRelatedLeftSpeed += leftAccel.get(i);
                leftAccel.set(i, leftAccel.get(i) + Constants.ACCELERATION_ADD_VALUE);
                if (leftAccel.get(i) > Constants.ACCELERATION_MAX_VAL) {
                    leftAccel.remove(i);
                    i--;
                }
            }
        }

        if (!rightAccel.isEmpty()) {
            for(int i = 0; i < rightAccel.size(); i++) {
                bulletRelatedRightSpeed += rightAccel.get(i);
                rightAccel.set(i, rightAccel.get(i) + Constants.ACCELERATION_ADD_VALUE);
                if (rightAccel.get(i) > Constants.ACCELERATION_MAX_VAL) {
                    rightAccel.remove(i);
                    i--;
                }
            }
        }

        if (!downAccel.isEmpty()) {
            for(int i = 0; i < downAccel.size(); i++) {
                bulletRelatedDownSpeed += downAccel.get(i);
                downAccel.set(i, downAccel.get(i) + Constants.ACCELERATION_ADD_VALUE);
                if (downAccel.get(i) > Constants.ACCELERATION_MAX_VAL) {
                    downAccel.remove(i);
                    i--;
                }
            }
        }
        if (!upAccel.isEmpty()) {
            for(int i = 0; i < upAccel.size(); i++) {
                bulletRelatedUpSpeed += upAccel.get(i);
                upAccel.set(i, upAccel.get(i) + Constants.ACCELERATION_ADD_VALUE);
                if (upAccel.get(i) > Constants.ACCELERATION_MAX_VAL) {
                    upAccel.remove(i);
                    i--;
                }
            }
        }

        width -= (rightSpeed + bulletRelatedRightSpeed);
        if (collidesWithOtherPanel() || !haveMinimumSize(wonGame)) {
            width += (rightSpeed + bulletRelatedRightSpeed);
        }

        x += leftSpeed + bulletRelatedLeftSpeed;
        width -= (leftSpeed + bulletRelatedLeftSpeed);
        if (collidesWithOtherPanel() || !haveMinimumSize(wonGame)) {
            x -= leftSpeed + bulletRelatedLeftSpeed;
            width += (leftSpeed + bulletRelatedLeftSpeed);
        }

        height -= downSpeed + bulletRelatedDownSpeed;
        if (collidesWithOtherPanel() || !haveMinimumSize(wonGame)) {
            height += downSpeed + bulletRelatedDownSpeed;
        }

        y += upSpeed + bulletRelatedUpSpeed;
        height -= upSpeed + bulletRelatedUpSpeed;
        if (collidesWithOtherPanel() || !haveMinimumSize(wonGame)) {
            y -= upSpeed + bulletRelatedUpSpeed;
            height += upSpeed + bulletRelatedUpSpeed;
        }
//        System.out.println(bulletRelatedRightSpeed);
    }

    public void bulletHit(WallSideIndicator wall) {
        if (wall.equals(WallSideIndicator.RIGHT)) {
            rightAccel.add(-Constants.ACCELERATION_MAX_VAL );
        }
        if (wall.equals(WallSideIndicator.LEFT)) {
            leftAccel.add(-Constants.ACCELERATION_MAX_VAL);
        }
        if (wall.equals(WallSideIndicator.UP)) {
            upAccel.add(-Constants.ACCELERATION_MAX_VAL);
        }
        if (wall.equals(WallSideIndicator.DOWN)) {
            downAccel.add(-Constants.ACCELERATION_MAX_VAL);
        }
    }
    public void setLeftSpeed(double leftSpeed) {
        this.leftSpeed = leftSpeed;
    }

    public void setRightSpeed(double rightSpeed) {
        this.rightSpeed = rightSpeed;
    }

    public void setUpSpeed(double upSpeed) {
        this.upSpeed = upSpeed;
    }

    public void setDownSpeed(double downSpeed) {
        this.downSpeed = downSpeed;
    }

    public Isometric getIsometric() {
        return isometric;
    }

    public void setIsometric(Isometric isometric) {
        this.isometric = isometric;
    }

    public Rigid getRigid() {
        return rigid;
    }

    public void setRigid(Rigid rigid) {
        this.rigid = rigid;
    }

    @Override
    public void setCenter(Point2D center) {
        this.center = center;
        updateLeftLocationWithCenter();
    }

    @Override
    public Point2D getCenter() {
        return center;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean isStationed() {
        return false;
    }
    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    public static void removeFromAllList(String id) {
        for (int i = 0; i < panelModelList.size(); i++) {
            if (panelModelList.get(i).getId().equals(id)) {
                panelModelList.remove(i);
                break;
            }
        }
        for (int i = 0; i < Movable.movable.size(); i++) {
            if (Movable.movable.get(i).getId() != null && Movable.movable.get(i).getId().equals(id)) {
                Movable.movable.remove(i);
                break;
            }
        }
        for (int i = 0; i < GamePanel.gamePanelList.size(); i++) {
            if (GamePanel.gamePanelList.get(i).getId().equals(id)) {
                GlassFrame.getINSTANCE().remove(GamePanel.gamePanelList.get(i));
                GamePanel.gamePanelList.remove(i);
                break;
            }
        }
    }
    public boolean moveToCenter() {
        Point2D frameCenter = new Point2D.Double(Constants.GLASS_FRAME_DIMENSION.width / 2.0, Constants.GLASS_FRAME_DIMENSION.height / 2.0);
        if (Utils.pointsApproxEqual(frameCenter, this.center)) return true;

        Point2D delta = new Point2D.Double(frameCenter.getX() - this.center.getX(), frameCenter.getY() - this.center.getY());
        Direction toPoint = new Direction(delta);
        this.setDirection(new Direction(toPoint.getDirectionVector()));
        this.setSpeed(2);
        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanelModel that = (PanelModel) o;
        return Objects.equals(id, that.id);
    }

}
