package model.collision;

import controller.constant.GameValues;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.Utils.weightedAddVectors;

public interface Collidable {
    boolean isCircular();
    boolean isHovering();
    double getRadius();
    Point2D getCenter();
    String getId();
    ArrayList<Point2D> getVertices();

    ArrayList<Collidable> collidables = new ArrayList<>();
    default CollisionPoint collides(Collidable other) {
        if (isHovering() && other.isHovering()) return null;
        if (isCircular() && other.isCircular()) {
            double distance = getCenter().distance(other.getCenter());
            if (distance <= getRadius() + other.getRadius()) {
                return new CollisionPoint(weightedAddVectors(getCenter(), other.getCenter(),getRadius(), other.getRadius()));
            }
            return null;
        }
        else if (this.isCircular() && !other.isCircular()) {
            Point2D closest = closestPointOnPolygon(this.getCenter(), other.getVertices());
            if (this.getCenter().distance(closest) <= this.getRadius()) {
                return new CollisionPoint(closest);
            }
        }
        else if (!this.isCircular() && !other.isCircular()){
            // Handle polygon-polygon collision
            Point2D intersection = checkPolygonsCollision(this.getVertices(), other.getVertices());
            if (intersection != null) {
                return new CollisionPoint(intersection);
            }
        }
        return null;
    }

    default Point2D closestPointOnPolygon(Point2D point, ArrayList<Point2D> vertices) {
        double minDistance = Double.MAX_VALUE;
        Point2D closest = null;
        for (int i = 0; i < vertices.size(); i++) {
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
        double u = ((point.getX() - head1.getX()) * (head2.getX() - head1.getX()) + (point.getY() - head1.getY()) * (head2.getY() - head1.getY())) / head1.distanceSq(head2);
        if (u < 0.0) return (Point2D) head1.clone();
        else if (u > 1.0) return (Point2D) head2.clone();
        return new Point2D.Double(head1.getX() + u * (head2.getX() - head1.getX()), head1.getY() + u * (head2.getY() - head1.getY()));
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