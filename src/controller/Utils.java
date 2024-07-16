package controller;

import java.awt.geom.Point2D;

public class Utils {
    public static Point2D addVectors(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }
    public static Point2D multiplyVector(Point2D point, double scalar){
        return new Point2D.Double(point.getX() * scalar,point.getY() * scalar);
    }
    public static Point2D weightedAddVectors(Point2D point1,Point2D point2,double weight1,double weight2){
        return multiplyVector(addVectors(multiplyVector(point1,weight1),multiplyVector(point2,weight2)),1/(weight1+weight2));
    }
    public static Point2D aimAndBulletDrawerCalculator(Point2D mousePoint, Point2D epsilonCenter, double radius) {
        double dx = mousePoint.getX() - epsilonCenter.getX();
        double dy = mousePoint.getY() - epsilonCenter.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return new Point2D.Double((dx / dist) * radius / 2  + epsilonCenter.getX(), (dy / dist) * radius / 2 + epsilonCenter.getY());
    }
}
