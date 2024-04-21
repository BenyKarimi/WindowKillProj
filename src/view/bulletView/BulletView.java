package view.bulletView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BulletView {
    private final String id;
    Point2D currentCenter;
    double currentRadius;
    public static ArrayList<BulletView> bulletViewList = new ArrayList<>();

    public BulletView(String id) {
        this.id = id;
        bulletViewList.add(this);
    }

    public String getId() {
        return id;
    }

    public Point2D getCurrentCenter() {
        return currentCenter;
    }

    public void setCurrentCenter(Point2D currentCenter) {
        this.currentCenter = currentCenter;
    }

    public double getCurrentRadius() {
        return currentRadius;
    }

    public void setCurrentRadius(double currentRadius) {
        this.currentRadius = currentRadius;
    }
}
