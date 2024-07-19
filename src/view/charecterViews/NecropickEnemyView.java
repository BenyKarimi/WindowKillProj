package view.charecterViews;

import model.charactersModel.NecropickEnemy;
import model.charactersModel.StationedType;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NecropickEnemyView {
    private final String id;
    private Point2D currentCenter;
    private double currentSize;
    private StationedType stationedType;
    public static ArrayList<NecropickEnemyView> necropickEnemyViewsList = new ArrayList<>();

    public NecropickEnemyView(String id) {
        this.id = id;
        currentCenter = new Point2D.Double(0, 0);
        stationedType = StationedType.HIDE;
        necropickEnemyViewsList.add(this);
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

    public StationedType getStationedType() {
        return stationedType;
    }

    public void setStationedType(StationedType stationedType) {
        this.stationedType = stationedType;
    }

}
