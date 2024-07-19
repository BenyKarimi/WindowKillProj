package view.charecterViews;

import controller.Pair;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ArchmireEnemyView {
    private final String id;
    private Point2D currentCenter;
    private double currentSize;
    private ArrayList<Pair<Point2D, Integer>> currentCentersPointMemory = new ArrayList<>();
    public static ArrayList<ArchmireEnemyView> archmireEnemyViewsList = new ArrayList<>();

    public ArchmireEnemyView(String id) {
        this.id = id;
        currentCenter = new Point2D.Double(0, 0);
        archmireEnemyViewsList.add(this);
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

    public ArrayList<Pair<Point2D, Integer>> getCurrentCentersPointMemory() {
        return currentCentersPointMemory;
    }

    public void setCurrentCentersPointMemory(ArrayList<Pair<Point2D, Integer>> currentCentersPointMemory) {
        this.currentCentersPointMemory = currentCentersPointMemory;
    }
}
