package model.collision;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Collidable {
    ArrayList<Collidable> collidables = new ArrayList<>();
    boolean isCircular();
    double getRadius();
    Point2D getCenter();
    ArrayList<Point2D> getVertices();

    default CollisionPoint collides(Collidable collidable) {
        // no collides between two circles
        if (isCircular() && !collidable.isCircular()) {
            Point2D closest = closestPointOnPolygon(getCenter(), collidable.getVertices());
            if (closest.distance(getCenter()) <= getRadius()) {
                return new CollisionPoint(closest);
            }
        }
        else if (!isCircular() && collidable.isCircular()) {
            return collidable.collides(this);
        }
        else {
            Point2D tmp = checkPolygonsCollision(getVertices(), collidable.getVertices());
            return new CollisionPoint(tmp);
        }
        return null;
    }
    default Point2D closestPointOnPolygon(Point2D point, ArrayList<Point2D> vertices){
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        for (int i = 0; i < vertices.size(); i++){
            Point2D tmp = getClosestPointOnSegment(vertices.get(i), vertices.get((i + 1) % vertices.size()), point);
            double distance = tmp.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = tmp;
            }
        }
        return closest;
    }
    default Point2D getClosestPointOnSegment(Point2D head1, Point2D head2, Point2D point) {
        double u = ((point.getX()-head1.getX())*(head2.getX()-head1.getX())+(point.getY()-head1.getY())*(head2.getY()-head1.getY()))/head2.distanceSq(head1);
        if (u > 1.0) return (Point2D) head2.clone();
        else if (u <= 0.0) return (Point2D) head1.clone();
        else return new Point2D.Double(head2.getX() * u + head1.getX() * (1.0 - u) + 0.5,head2.getY() * u + head1.getY() * (1.0 - u) + 0.5);
    }
    default Point2D checkPolygonsCollision(ArrayList<Point2D> firstPoly, ArrayList<Point2D> secondPoly) {
        for (int i = 0; i < firstPoly.size(); i++) {
            Line l1 = new Line(firstPoly.get(i), firstPoly.get((i + 1) % firstPoly.size()));
            for (int j = 0; j < secondPoly.size(); j++) {
                Line l2 = new Line(secondPoly.get(j), secondPoly.get((j + 1) % secondPoly.size()));
                Point2D intersection = Line.findIntersection(l1, l2);
                if (intersection != null) return intersection;
            }
        }
        return null;
    }
}
