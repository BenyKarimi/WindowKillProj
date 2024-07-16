package model.panelModel;

import controller.Controller;
import controller.Utils;
import controller.constant.Constants;
import model.bulletModel.BulletModel;
import view.container.GlassFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PanelModel {
    private double x;
    private double y;
    private double width;
    private double height;
    private double leftSpeed = 0, bulletRelatedLeftSpeed = 0;
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
    public static ArrayList<PanelModel> panelModelList = new ArrayList<>();

    public PanelModel(double x, double y, double width, double height, Isometric isometric, Rigid rigid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isometric = isometric;
        this.rigid = rigid;
        this.id = UUID.randomUUID().toString();
        Controller.getINSTANCE().createGamePanel(id, x, y, width, height);
        panelModelList.add(this);
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

        changeShrinkByCircle(Controller.getINSTANCE().logic.epsilon.getCenter(), Controller.getINSTANCE().logic.epsilon.getRadius());

        for(BulletModel bullet : BulletModel.bulletModelList)
            changeShrinkByCircle(bullet.getCenter(), bullet.getRadius());

        // TODO: add the rigid bullets of enemies
    }
    public void endGameShrinkValue() {
        leftSpeed = width / Constants.SHRINK_DECELERATION;
        rightSpeed = width / Constants.SHRINK_DECELERATION;

        upSpeed = height / Constants.SHRINK_DECELERATION;
        downSpeed = height / Constants.SHRINK_DECELERATION;
    }
    private boolean collidesWithOtherPanel() {
        for(PanelModel panel : panelModelList) {
            if (!this.equals(panel) && new Rectangle((int)x, (int)y, (int)height, (int)width).intersects(new Rectangle((int)panel.getX(), (int)panel.getY(), (int)panel.getHeight(), (int)panel.getWidth()))
                && (rigid.equals(Rigid.YES) || panel.rigid.equals(Rigid.YES)))
                return true;
        }

        return false;
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
    }

    public void bulletHit(String wall) {
        if (wall.equals("right")) {
            rightAccel.add(-Constants.ACCELERATION_MAX_VAL  );
        }
        if (wall.equals("left")) {
            leftAccel.add(-Constants.ACCELERATION_MAX_VAL);
        }
        if (wall.equals("up")) {
            upAccel.add(-Constants.ACCELERATION_MAX_VAL);
        }
        if (wall.equals("down")) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanelModel that = (PanelModel) o;
        return Objects.equals(id, that.id);
    }
}
