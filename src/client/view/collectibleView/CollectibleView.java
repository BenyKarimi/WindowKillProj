package client.view.collectibleView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CollectibleView {
    private final String id;
    private Point2D currentCenter;
    private double currentSize;
    public static ArrayList<CollectibleView> collectibleViewList = new ArrayList<>();

    public CollectibleView(String id) {
        this.id = id;
        this.currentCenter = new Point2D.Double(0, 0);
        collectibleViewList.add(this);
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
