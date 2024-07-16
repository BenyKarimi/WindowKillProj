package controller;

import model.panelModel.PanelModel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.math.BigInteger;
import java.util.ArrayList;

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
    public static ArrayList<Point2D> circlePartition(Point2D center, double radius, int verticesNumber) {
        ArrayList<Point2D> out = new ArrayList<>();
        for (int i = 1; i <= verticesNumber; i++) {
            double x = radius * Math.cos(Math.toRadians(1.0 * i * 360 / verticesNumber));
            double y = radius * Math.sin(Math.toRadians(1.0 * i * 360 / verticesNumber));
            out.add(new Point2D.Double(center.getX() + x, center.getY() + y));
        }
        return out;
    }
    public static ArrayList<Point2D> circlePca(Point2D center, double radius) {
        return Utils.circlePartition(center, radius, 360);
    }
    public static boolean isCircleInside(ArrayList<PanelModel> panelModels, Point2D center, double radius) {
        ArrayList<Point2D> sphere = Utils.circlePca(center, radius);

        for(Point2D circlePoint : sphere) {
            boolean isInPanel = false;
            for(PanelModel panel : panelModels) {
                if (panel.getX() <= circlePoint.getX() && circlePoint.getX() <= panel.getX() + panel.getWidth() &&
                        panel.getY() <= circlePoint.getY() && circlePoint.getY() <= panel.getY() + panel.getHeight()) {
                    isInPanel = true;
                }
            }
            if (!isInPanel) {
                return false;
            }
        }
        return true;
    }
    public static Point2D adjustedCenter (Point2D center, double radius, ArrayList<PanelModel> panelModels, double speed) {
        ArrayList<Point2D> sphere = Utils.circlePca(center, radius);

        ArrayList<Point2D> notIn = new ArrayList<>();
        for (Point2D point : sphere) {
            boolean isIn = false;
            for(PanelModel panel : panelModels) {
                if (new Rectangle((int)panel.getX(), (int)panel.getY(), (int)panel.getWidth(), (int)panel.getHeight()).contains(point)) {
                    isIn = true;
                }
            }

            if (!isIn) {
                notIn.add(point);
            }
        }

        if (notIn.isEmpty()) {
            return center;
        }

        Point2D minimumPoint = new Point2D.Double(0, 0);
        double minimumDistance = 0;

        for(Point2D point : notIn) {
            double tmpMin = Double.MAX_VALUE;
            for(PanelModel panel : panelModels) {
                tmpMin = Math.min(tmpMin, Math.abs(panel.getX() - point.getX()));
                tmpMin = Math.min(tmpMin, Math.abs(panel.getX() + panel.getWidth() - point.getX()));

                tmpMin = Math.min(tmpMin, Math.abs(panel.getY() - point.getY()));
                tmpMin = Math.min(tmpMin, Math.abs(panel.getY() + panel.getHeight() - point.getY()));
            }

            if (minimumDistance < tmpMin) {
                minimumDistance = tmpMin;
                minimumPoint = point;
            }
        }

        double dx = center.getX() - minimumPoint.getX();
        double dy = center.getY() - minimumPoint.getY();
        double dis = center.distance(minimumPoint);

        Point2D newCenter = new Point2D.Double(minimumDistance / dis * dx + center.getX(), minimumDistance / dis * dy + center.getY());
        return newCenter;
    }
    public static Point2D makeNegativeVector(Point2D vector) {
        return new Point2D.Double(-vector.getX(), -vector.getY());
    }
    public static ArrayList<PanelModel> coveringPanels(ArrayList<PanelModel> panelModels, Point2D center, double radius) {
        ArrayList<PanelModel> out = new ArrayList<>();
        ArrayList<Point2D> sphere = Utils.circlePca(center, radius);

        for (PanelModel panel : panelModels) {
            boolean allInIt = true;

            for(Point2D circlePoint : sphere) {
                if (!(panel.getX() <= circlePoint.getX() && circlePoint.getX() <= panel.getX() + panel.getWidth() &&
                        panel.getY() <= circlePoint.getY() && circlePoint.getY() <= panel.getY() + panel.getHeight())) {
                     allInIt = false;
                }
            }

            if (allInIt) {
                out.add(panel);
            }
        }
        return out;
    }
    public static ArrayList<PanelModel> unionPanels(ArrayList<PanelModel> panelModels, Point2D center, double radius) {
        ArrayList<PanelModel> out = new ArrayList<>();
        ArrayList<Point2D> sphere = Utils.circlePca(center, radius);

        for (PanelModel panel : panelModels) {

            for(Point2D circlePoint : sphere) {
                if (panel.getX() <= circlePoint.getX() && circlePoint.getX() <= panel.getX() + panel.getWidth() &&
                        panel.getY() <= circlePoint.getY() && circlePoint.getY() <= panel.getY() + panel.getHeight()) {
                    out.add(panel);
                }
            }
        }
        return out;
    }
}
