package client.model.charactersModel;

import client.controller.updater.Controller;
import client.controller.updater.Utils;
import client.model.collision.Collidable;
import client.model.movement.Direction;
import client.model.movement.Movable;
import client.model.panelModel.Isometric;
import client.model.panelModel.PanelModel;
import client.model.panelModel.Rigid;
import client.view.charecterViews.BarricadosEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static client.controller.constant.Constants.*;

public class BarricadosEnemy extends Enemy {

    private final boolean panelRigid;
    private PanelModel barricadosPanel;
    private final int timeMade;

    public static ArrayList<BarricadosEnemy> barricadosEnemiesList = new ArrayList<>();

    public BarricadosEnemy(Point2D center, double size, boolean panelRigid, int timeMade) {
        super(Utils.processRandomId(), size, 0, 0, false, BARRICADOS_ENEMY_HP, BARRICADOS_ENEMY_REDUCER_HP, BARRICADOS_ENEMY_COLLECTIBLE_NUMBER, BARRICADOS_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        this.panelRigid = panelRigid;
        this.timeMade = timeMade;
        calculateVertices();
        makePanel();
        Controller.getINSTANCE().createBarricadosEnemyView(super.getId());
        Collidable.collidables.add(this);
        barricadosEnemiesList.add(this);
        Movable.movable.add(this);
    }

    @Override
    public void calculateVertices() {
        super.getVertices().clear();
        ArrayList<Point2D> ver = Utils.calculateSquareVertices(super.getCenter(), super.getSize());
        for (Point2D ptr : ver) super.getVertices().add(ptr);
    }

    @Override
    public void updateDirection(Point2D point) {
        super.setDirection(new Direction(new Point2D.Double(0, 0)));
        super.setSpeed(0);
    }

    private void makePanel() {
        if (panelRigid) {
            barricadosPanel = new PanelModel(super.getVertices().get(0).getX(), super.getVertices().get(0).getY(), super.getSize(), super.getSize(), Isometric.YES, Rigid.YES);
        }
        else {
            barricadosPanel = new PanelModel(super.getVertices().get(0).getX(), super.getVertices().get(0).getY(), super.getSize(), super.getSize(), Isometric.YES, Rigid.NO);
        }
    }
    public boolean checkToRemove(int time) {
        return time - timeMade >= 2000 * 30;
    }
    public static void removeFromAllList(String id) {
        for (int i = 0; i < barricadosEnemiesList.size(); i++) {
            if (barricadosEnemiesList.get(i).getId().equals(id)) {
                barricadosEnemiesList.remove(i);
                break;
            }
        }
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            if (Collidable.collidables.get(i).getId() != null && Collidable.collidables.get(i).getId().equals(id)) {
                Collidable.collidables.remove(i);
                break;
            }
        }
        for (int i = 0; i < Movable.movable.size(); i++) {
            if (Movable.movable.get(i).getId() != null && Movable.movable.get(i).getId().equals(id)) {
                Movable.movable.remove(i);
                break;
            }
        }
        for (int i = 0; i < Enemy.enemiesList.size(); i++) {
            if (Enemy.enemiesList.get(i).getId().equals(id)) {
                Enemy.enemiesList.remove(i);
                break;
            }
        }
        for (int i = 0; i < BarricadosEnemyView.barricadosEnemyViewsList.size(); i++) {
            if (BarricadosEnemyView.barricadosEnemyViewsList.get(i).getId().equals(id)) {
                BarricadosEnemyView.barricadosEnemyViewsList.remove(i);
                break;
            }
        }
    }
    public void removePanel() {
        PanelModel.removeFromAllList(barricadosPanel.getId());
    }

    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isHovering() {
        return false;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public boolean isStationed() {
        return true;
    }

    public boolean isPanelRigid() {
        return panelRigid;
    }

    public int getTimeMade() {
        return timeMade;
    }
}
