package controller.handeler;

import controller.Controller;
import controller.Utils;
import controller.constant.Constants;
import model.bulletModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.movement.Direction;
import view.container.GamePanel;
import view.container.GlassFrame;

import java.awt.geom.Point2D;

public class MouseClickedActionHandled {
    public static void leftClicked(Point2D clickedPoint) {
        EpsilonModel epsilon = Controller.getINSTANCE().logic.epsilon;
        Point2D center = Utils.aimAndBulletDrawerCalculator(clickedPoint, epsilon.getCenter(), epsilon.getRadius());
        if (StoreActionHandle.isThreeBullet()) {
            if (GlassFrame.getINSTANCE().getTimer().getSeconds() - StoreActionHandle.getStartThreeBullet() < 10) {
                Direction direction1 = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX(), clickedPoint.getY() - epsilon.getCenter().getY()));
                Direction direction2 = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX() + 20, clickedPoint.getY() - epsilon.getCenter().getY() - 20));
                Direction direction3 = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX() - 20, clickedPoint.getY() - epsilon.getCenter().getY() + 20));
                new BulletModel(center, direction1);
                new BulletModel(center, direction2);
                new BulletModel(center, direction3);
            }
            else {
                StoreActionHandle.setThreeBullet(false);
            }
        }
        else {
            Direction direction = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX(), clickedPoint.getY() - epsilon.getCenter().getY()));
            new BulletModel(center, direction);
        }
        Constants.bulletSound.play();
    }
}
