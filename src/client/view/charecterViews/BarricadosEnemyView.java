package client.view.charecterViews;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BarricadosEnemyView {
    private final String id;
    private Point2D currentCenter;
    private double currentSize;
    public static ArrayList<BarricadosEnemyView> barricadosEnemyViewsList = new ArrayList<>();

    public BarricadosEnemyView(String id) {
        this.id = id;
        currentCenter = new Point2D.Double(0, 0);
        barricadosEnemyViewsList.add(this);
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
