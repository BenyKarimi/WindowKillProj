package view.charecterViews;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.EPSILON_RADIUS;

public class EpsilonView {
    private static EpsilonView INSTANCE;
    Point2D currentCenter;
    int currentXp, currentHp;
    double currentRadius;
    ArrayList<Point2D> currentVertices;
    public EpsilonView() {
        INSTANCE = this;
        currentCenter = new Point2D.Double(EPSILON_RADIUS, EPSILON_RADIUS);
        currentRadius = EPSILON_RADIUS;
        currentVertices = new ArrayList<>();
    }

    public static EpsilonView getINSTANCE() {
//        if (INSTANCE == null) {
//            INSTANCE = new EpsilonView();
//        }
        return INSTANCE;
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
