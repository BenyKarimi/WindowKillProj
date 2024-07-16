package model.collision;

import java.awt.geom.Point2D;

public class CollisionPoint {
    private Point2D collisionPoint;
    public CollisionPoint(Point2D collisionPoint) {
        this.collisionPoint = collisionPoint;
    }

    public Point2D getCollisionPoint() {
        return collisionPoint;
    }
}
