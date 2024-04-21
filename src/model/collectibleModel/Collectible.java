package model.collectibleModel;

import controller.Controller;
import controller.constant.Constants;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class Collectible {
    private final String id;
    double size;
    double existence;
    int addedXp;
    Point2D center;
    public static ArrayList<Collectible> collectibleList = new ArrayList<>();

    public Collectible(Point2D center, int addedXp) {
        this.center = center;
        this.addedXp = addedXp;
        existence = Constants.COLLECTIBLE_EXISTENCE;
        size = Constants.COLLECTIBLE_SIZE;
        id = UUID.randomUUID().toString();
        collectibleList.add(this);
        Controller.getINSTANCE().createCollectibleView(id);
    }

    public String getId() {
        return id;
    }

    public int getAddedXp() {
        return addedXp;
    }

    public double getSize() {
        return size;
    }

    public double getExistence() {
        return existence;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setExistence(double existence) {
        this.existence = existence;
    }
}
