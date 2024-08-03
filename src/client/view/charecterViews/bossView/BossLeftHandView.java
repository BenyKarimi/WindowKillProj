package client.view.charecterViews.bossView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BossLeftHandView {
    private final String id;
    private Point2D currentCenter;
    private double currentSize;
    public static ArrayList<BossLeftHandView> bossLeftHandViewsList = new ArrayList<>();

    public BossLeftHandView(String id) {
        this.id = id;
        currentCenter = new Point2D.Double(0, 0);
        bossLeftHandViewsList.add(this);
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
