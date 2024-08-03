package client.controller.handeler;

import client.controller.updater.Utils;
import client.controller.constant.Constants;
import client.controller.constant.GameValues;
import client.model.bulletModel.RigidBulletModel;
import client.model.charactersModel.EpsilonModel;
import client.model.movement.Direction;
import client.view.container.GlassFrame;

import java.awt.geom.Point2D;

public class MouseClickedActionHandled {
    public static void leftClicked(Point2D clickedPoint) {
        if (GameValues.secondRoundFinish && !GameValues.bossFightStart) return;
        if (GameValues.isBossQuakeAttack) return;
        GameValues.bulletFired++;
        EpsilonModel epsilon = EpsilonModel.epsilonModelsList.get(0);
        Point2D center = Utils.aimAndBulletDrawerCalculator(clickedPoint, epsilon.getCenter(), epsilon.getRadius());
        if (StoreActionHandle.isHotBullet()) {
            Direction direction = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX(), clickedPoint.getY() - epsilon.getCenter().getY()));
            new RigidBulletModel(center, direction, Constants.HOT_BULLET_REDUCE_HP, epsilon.getId(), 5);
            StoreActionHandle.setHotBullet(false);
        }
        else if (StoreActionHandle.isThreeBullet()) {
            if (GlassFrame.getINSTANCE().getTimer().getSeconds() - StoreActionHandle.getStartThreeBullet() < 10) {
                Direction direction1 = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX(), clickedPoint.getY() - epsilon.getCenter().getY()));
                Direction direction2 = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX() + 20, clickedPoint.getY() - epsilon.getCenter().getY() - 20));
                Direction direction3 = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX() - 20, clickedPoint.getY() - epsilon.getCenter().getY() + 20));
                new RigidBulletModel(center, direction1, Constants.BULLET_REDUCE_HP, epsilon.getId(), 5);
                new RigidBulletModel(center, direction2, Constants.BULLET_REDUCE_HP, epsilon.getId(), 5);
                new RigidBulletModel(center, direction3, Constants.BULLET_REDUCE_HP, epsilon.getId(), 5);
            }
            else {
                StoreActionHandle.setThreeBullet(false);
            }
        }
        else {
            Direction direction = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX(), clickedPoint.getY() - epsilon.getCenter().getY()));
            new RigidBulletModel(center, direction, Constants.BULLET_REDUCE_HP, epsilon.getId(), 5);
        }
        Constants.bulletSound.play();
    }
}
