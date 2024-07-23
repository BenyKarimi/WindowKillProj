package view.charecterViews.bossView;


import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BossHeadView {
    private final String id;
    private Point2D currentCenter;
    private double currentSize;
    private boolean dead;
    private ArrayList<Point2D> currentAoeCenters;
    public static ArrayList<BossHeadView> bossHeadViewsList = new ArrayList<>();

    public BossHeadView(String id) {
        this.id = id;
        this.dead = false;
        currentCenter = new Point2D.Double(0, 0);
        currentAoeCenters = new ArrayList<>();
        bossHeadViewsList.add(this);
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public ArrayList<Point2D> getCurrentAoeCenters() {
        return currentAoeCenters;
    }

    public void setCurrentAoeCenters(ArrayList<Point2D> currentAoeCenters) {
        this.currentAoeCenters = currentAoeCenters;
    }
}
