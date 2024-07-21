package view.charecterViews;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.EPSILON_RADIUS;

public class EpsilonView {
    private final String id;
    private static EpsilonView INSTANCE;
    private Point2D currentCenter;
    private int currentXp, currentHp;
    private double currentRadius;
    private ArrayList<Point2D> currentVertices;
    public static ArrayList<EpsilonView> epsilonViewsList = new ArrayList<>();
    public EpsilonView(String id, Point2D currentCenter) {
        this.id = id;
        INSTANCE = this;
        this.currentCenter = currentCenter;
        currentRadius = EPSILON_RADIUS;
        currentVertices = new ArrayList<>();
        epsilonViewsList.add(this);
    }

    public String getId() {
        return id;
    }

    public double getCurrentRadius() {
        return currentRadius;
    }
    public void setCurrentRadius(double currentRadius) {
        this.currentRadius = currentRadius;
    }

    public Point2D getCurrentCenter() {
        return currentCenter;
    }

    public void setCurrentCenter(Point2D currentCenter) {
        this.currentCenter = currentCenter;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public void setCurrentXp(int currentXp) {
        this.currentXp = currentXp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public ArrayList<Point2D> getCurrentVertices() {
        return currentVertices;
    }

    public void setCurrentVertices(ArrayList<Point2D> currentVertices) {
        this.currentVertices = currentVertices;
    }
}
