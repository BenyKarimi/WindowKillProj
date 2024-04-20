package view.charecterViews;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TriangleEnemyView {
    private final String id;
    Point2D currentCenter;
    double currentSize;
    public static ArrayList<TriangleEnemyView> triangleEnemyViewList = new ArrayList<>();
    public TriangleEnemyView(String id) {
        this.id = id;
        triangleEnemyViewList.add(this);
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

    public double getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(double currentSize) {
        this.currentSize = currentSize;
    }
}
