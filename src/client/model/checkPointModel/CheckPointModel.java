package client.model.checkPointModel;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.controller.constant.Constants;
import client.view.checkPointView.CheckPointView;
import client.view.container.GlassFrame;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CheckPointModel {
    private final String id;
    private Point2D center;
    private double size;
    private int timeMade;
    public static ArrayList<CheckPointModel> checkPointModelsList = new ArrayList<>();

    public CheckPointModel(Point2D center) {
        this.center = center;
        id = Utils.processRandomId();
        timeMade = GlassFrame.getINSTANCE().getTimer().getMiliSecond();
        size = Constants.CHECKPOINT_SIZE;
        checkPointModelsList.add(this);
        Controller.getINSTANCE().createCheckPointView(id);
    }

    public String getId() {
        return id;
    }

    public Point2D getCenter() {
        return center;
    }

    public double getSize() {
        return size;
    }

    public int getTimeMade() {
        return timeMade;
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < checkPointModelsList.size(); i++) {
            if (checkPointModelsList.get(i).getId().equals(id)) {
                checkPointModelsList.remove(i);
                break;
            }
        }
        for (int i = 0; i < CheckPointView.checkPointViewsList.size(); i++) {
            if (CheckPointView.checkPointViewsList.get(i).getId().equals(id)) {
                CheckPointView.checkPointViewsList.remove(i);
                break;
            }
        }
    }
}
