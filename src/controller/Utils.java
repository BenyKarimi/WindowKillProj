package controller;

import model.panelModel.PanelModel;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.awt.*;
import java.awt.geom.Point2D;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;

import static controller.constant.Constants.ERROR;

public class Utils {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

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
    public static boolean ApproxEqual(double a, double b) {
        return a - ERROR <= b && a + ERROR >= b;
    }
    public static ArrayList<Point2D> calculateSquareVertices(Point2D center, double size) {
        ArrayList<Point2D> out = new ArrayList<>();
        Point2D leftUp = new Point2D.Double(center.getX() - (size / 2), center.getY() - (size / 2));
        Point2D rightUp = new Point2D.Double(center.getX() + (size / 2), center.getY() - (size / 2));
        Point2D rightDown = new Point2D.Double(center.getX() + (size / 2), center.getY() + (size / 2));
        Point2D leftDown = new Point2D.Double(center.getX() - (size / 2), center.getY() + (size / 2));

        out.add(leftUp);
        out.add(leftDown);
        out.add(rightUp);
        out.add(rightDown);
        return out;
    }
    public static String processRandomId() {
        return UUID.randomUUID().toString();
    }
    public static boolean isPolygonInside(Polygon p1, Polygon p2) {
        return p1.contains(p2);
    }
    public static <T, U> boolean pairArrayContainsKey (ArrayList<Pair<T, U>> pairArrayList, T key) {
        for(Pair<T, U> ptr : pairArrayList) {
            if (ptr.getFirst().equals(key)) {
                return true;
            }
        }
        return false;
    }
    public static <T, U> void pairArrayRemoveKey (ArrayList<Pair<T, U>> pairArrayList, T key) {
        for (int i = 0; i < pairArrayList.size(); i++) {
            if (pairArrayList.get(i).getFirst().equals(key)) {
                pairArrayList.remove(i);
                return;
            }
        }
    }
    public static Polygon makePolygonWithVertices(ArrayList<Point2D> point1) {
        Coordinate[] c1 = new Coordinate[point1.size() + 1];
        for (int i = 0; i <= point1.size(); i++) c1[i] = new Coordinate(point1.get(i % point1.size()).getX(), point1.get(i % point1.size()).getY());
        return geometryFactory.createPolygon(c1);
    }
    public static Geometry calculateUnion(ArrayList<Polygon> polygons) {
        if (polygons.isEmpty()) {
            return null;
        }
        Geometry geometry = polygons.get(0).union(polygons.get(0));

        geometry.contains(polygons.get(0));
        for (int i = 1; i < polygons.size(); i++) {
            geometry = geometry.union(polygons.get(i));
        }

        return geometry;
    }
}
