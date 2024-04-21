package controller;

import java.awt.geom.Point2D;

public class Utils {
    public static Point2D addVectors(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }
    public static Point2D multiplyVector(Point2D point, double scalar){
        return new Point2D.Double(point.getX() * scalar,point.getY() * scalar);
    }
}
