package model.collectibleModel;

import controller.Controller;
import controller.Utils;
import controller.constant.Constants;
import view.collectibleView.CollectibleView;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class Collectible {
    private final String id;
    private double size;
    private double existence;
    private int addedXp;
    private Point2D center;
    public static ArrayList<Collectible> collectibleList = new ArrayList<>();

    public Collectible(Point2D center, int addedXp) {
        this.center = center;
        this.addedXp = addedXp;
        existence = Constants.COLLECTIBLE_EXISTENCE;
        size = Constants.COLLECTIBLE_SIZE;
        id = Utils.processRandomId();
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

    public static void removeFromAllList(String id) {
        for (int i = 0; i < collectibleList.size(); i++) {
            if (collectibleList.get(i).getId().equals(id)) {
                collectibleList.remove(i);
                break;
            }
        }
        for (int i = 0; i < CollectibleView.collectibleViewList.size(); i++) {
            if (CollectibleView.collectibleViewList.get(i).getId().equals(id)) {
                CollectibleView.collectibleViewList.remove(i);
                break;
            }
        }
    }
    public void setExistence(double existence) {
        this.existence = existence;
    }
}
