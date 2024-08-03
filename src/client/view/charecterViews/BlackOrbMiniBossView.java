package client.view.charecterViews;

import client.model.collision.Line;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BlackOrbMiniBossView {
    private final String id;
    private ArrayList<Point2D> currentOrbLocations;
    private ArrayList<Line> currentOrbLasers;
    private double orbRadius;
    public static ArrayList<BlackOrbMiniBossView> blackOrbMiniBossViewsList = new ArrayList<>();

    public BlackOrbMiniBossView(String id) {
        this.id = id;
        this.orbRadius = 0;
        currentOrbLasers = new ArrayList<>();
        currentOrbLocations = new ArrayList<>();
        blackOrbMiniBossViewsList.add(this);
    }

    public String getId() {
        return id;
    }

    public ArrayList<Point2D> getCurrentOrbLocations() {
        return currentOrbLocations;
    }

    public void setCurrentOrbLocations(ArrayList<Point2D> currentOrbLocations) {
        this.currentOrbLocations = currentOrbLocations;
    }

    public ArrayList<Line> getCurrentOrbLasers() {
        return currentOrbLasers;
    }

    public void setCurrentOrbLasers(ArrayList<Line> currentOrbLasers) {
        this.currentOrbLasers = currentOrbLasers;
    }

    public double getOrbRadius() {
        return orbRadius;
    }

    public void setOrbRadius(double orbRadius) {
        this.orbRadius = orbRadius;
    }
}
