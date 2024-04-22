package controller;

import model.bulletModel.BulletModel;
import model.charactersModel.EpsilonModel;
import model.movement.Direction;

import java.awt.geom.Point2D;

public class MouseClickedActionHandled {
    public static void leftClicked(Point2D clickedPoint) {
        EpsilonModel epsilon = Controller.getINSTANCE().logic.epsilon;
        Direction direction = new Direction(new Point2D.Double(clickedPoint.getX() - epsilon.getCenter().getX(), clickedPoint.getY() - epsilon.getCenter().getY()));
        Point2D center = Utils.aimAndBulletDrawerCalculator(clickedPoint, epsilon.getCenter(), epsilon.getRadius());
        new BulletModel(center, direction);
    }
}
