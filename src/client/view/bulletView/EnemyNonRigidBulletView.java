package client.view.bulletView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EnemyNonRigidBulletView {
    private final String id;
    private Point2D currentCenter;
    private double currentRadius;
    public static ArrayList<EnemyNonRigidBulletView> nonRigidBulletViewsList = new ArrayList<>();

    public EnemyNonRigidBulletView(String id) {
        this.id = id;
        this.currentCenter = new Point2D.Double(0, 0);
        nonRigidBulletViewsList.add(this);
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

    public double getCurrentRadius() {
        return currentRadius;
    }

    public void setCurrentRadius(double currentRadius) {
        this.currentRadius = currentRadius;
    }
}
