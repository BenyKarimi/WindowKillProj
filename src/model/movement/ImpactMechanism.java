package model.movement;

import controller.Controller;
import controller.constant.Constants;
import model.charactersModel.EpsilonModel;
import model.charactersModel.SquareEnemy;
import model.charactersModel.TriangleEnemy;

import java.awt.geom.Point2D;

import static controller.constant.Constants.IMPACT_RADIUS;

public class ImpactMechanism {

    public static void applyImpact(Point2D collisionPoint, double impactLevel) {
        /// epsilon
        {
            EpsilonModel epsilon = Controller.getINSTANCE().logic.epsilon;
            Direction direction = getNewDirection(collisionPoint, epsilon.getCenter());
            if (direction != null) {
                epsilon.setDirection(direction);
                epsilon.setSpeed(impactLevel);
            }
        }
        /// triangle enemy
        for (TriangleEnemy ptr : TriangleEnemy.triangleEnemyList) {
            Direction direction = getNewDirection(collisionPoint, ptr.getCenter());
            if (direction != null) {
                ptr.setImpact(true);
                ptr.setSpeed(getNewSpeed(collisionPoint, ptr.getCenter()) * impactLevel);
                ptr.setDirection(direction);
            }
        }
        /// square enemy
        for (SquareEnemy ptr : SquareEnemy.squareEnemyList) {
            Direction direction = getNewDirection(collisionPoint, ptr.getCenter());
            if (direction != null) {
                ptr.setImpact(true);
                ptr.setSpeed(getNewSpeed(collisionPoint, ptr.getCenter()) * impactLevel);
                ptr.setDirection(direction);
            }
        }
    }
    private static Direction getNewDirection(Point2D collisionPoint, Point2D center) {
        double dx = collisionPoint.getX() - center.getX();
        double dy = collisionPoint.getY() - center.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist == 0 || dist > IMPACT_RADIUS) return null;
        double impactEffect = 1 - (dist / IMPACT_RADIUS);
        Point2D newPoint = new Point2D.Double((-dx / dist) * impactEffect, (-dy / dist) * impactEffect);

        return new Direction(newPoint);
    }
    private static double getNewSpeed(Point2D collisionPoint, Point2D center) {
        double dx = collisionPoint.getX() - center.getX();
        double dy = collisionPoint.getY() - center.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist == 0 || dist > IMPACT_RADIUS) return 0;
        double impactEffect = 1 - (dist / IMPACT_RADIUS);
        return 2 * impactEffect;
    }
}
