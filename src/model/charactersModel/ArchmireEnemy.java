package model.charactersModel;

import controller.Controller;
import controller.Pair;
import controller.Utils;
import model.collision.Collidable;
import model.movement.Direction;
import model.movement.Movable;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import view.charecterViews.ArchmireEnemyView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class ArchmireEnemy extends Enemy{

    public static ArrayList<ArchmireEnemy> archmireEnemiesList = new ArrayList<>();
    private ArrayList<Pair<Enemy, Integer>> enemiesInsideDrown = new ArrayList<>();
    private ArrayList<Pair<Enemy, Integer>> enemiesInsideAOE = new ArrayList<>();
    private ArrayList<Pair<EpsilonModel, Integer>> epsilonsInsideDrown = new ArrayList<>();
    private ArrayList<Pair<EpsilonModel, Integer>> epsilonsInsideAOE = new ArrayList<>();
    private ArrayList<Pair<Point2D, Integer>> centersPointMemory = new ArrayList<>();

    public ArchmireEnemy(Point2D center, double size, double speed) {
        super(Utils.processRandomId(), size, speed, speed, false, ARCHMIRE_ENEMY_HP, ARCHMIRE_ENEMY_REDUCER_HP, ARCHMIRE_ENEMY_COLLECTIBLE_NUMBER, ARCHMIRE_ENEMY_COLLECTIBLE_XP,
                center, new Direction(new Point2D.Double(0, 0)), new ArrayList<>());
        calculateVertices();
        Controller.getINSTANCE().createArchmireEnemyView(super.getId());
        Collidable.collidables.add(this);
        archmireEnemiesList.add(this);
        Movable.movable.add(this);
    }
    @Override
    public void calculateVertices() {
        super.getVertices().clear();

        ArrayList<Point2D> points = Utils.circlePartition(super.getCenter(), 3 * super.getSize() / 4, 12);
        for (Point2D ptr : points) super.getVertices().add(ptr);
    }
    @Override
    public void updateDirection(Point2D point) {
        Point2D delta = new Point2D.Double(point.getX() - super.getCenter().getX(), point.getY() - super.getCenter().getY());
        Direction toPoint = new Direction(delta);
        super.setDirection(new Direction(toPoint.getDirectionVector()));
    }
    public void updateEntitiesInsideDrown(int time) {
        for (Enemy enemy : Enemy.enemiesList) {
            if (Utils.isPolygonInside(Utils.makePolygonWithVertices(super.getVertices()), Utils.makePolygonWithVertices(enemy.getVertices()))) {
                if (!Utils.pairArrayContainsKey(enemiesInsideDrown, enemy) && !enemy.equals(this)) enemiesInsideDrown.add(new Pair<>(enemy, time));
            }
            else Utils.pairArrayRemoveKey(enemiesInsideDrown, enemy);
        }
        for (EpsilonModel epsilonModel : EpsilonModel.epsilonModelsList) {
            if (Utils.isPolygonInside(Utils.makePolygonWithVertices(super.getVertices()), Utils.makePolygonWithVertices(Utils.circlePartition(epsilonModel.getCenter(), epsilonModel.getRadius(), 36)))) {
                if (!Utils.pairArrayContainsKey(epsilonsInsideDrown, epsilonModel)) epsilonsInsideDrown.add(new Pair<>(epsilonModel, time));
            }
            else Utils.pairArrayRemoveKey(epsilonsInsideDrown, epsilonModel);
        }
    }
    public void updateEntitiesInsideAOE(int time) {
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (Pair<Point2D, Integer> ptr : centersPointMemory) {
            polygons.add(Utils.makePolygonWithVertices(Utils.circlePartition(ptr.getFirst(), 3 * super.getSize() / 4, 12)));
        }
        Geometry geometry = Utils.calculateUnion(polygons);
        if (geometry == null) {
            enemiesInsideAOE.clear();
            epsilonsInsideAOE.clear();
            return;
        }

        for (Enemy enemy : Enemy.enemiesList) {
            if (Utils.isPolygonInside((Polygon) geometry, Utils.makePolygonWithVertices(enemy.getVertices()))) {
                if (!Utils.pairArrayContainsKey(enemiesInsideAOE, enemy)) enemiesInsideAOE.add(new Pair<>(enemy, time));
            }
            else Utils.pairArrayRemoveKey(enemiesInsideAOE, enemy);
        }
        for (EpsilonModel epsilonModel : EpsilonModel.epsilonModelsList) {
            if (Utils.isPolygonInside((Polygon) geometry, Utils.makePolygonWithVertices(Utils.circlePartition(epsilonModel.getCenter(), epsilonModel.getRadius(), 36)))) {
                if (!Utils.pairArrayContainsKey(epsilonsInsideAOE, epsilonModel)) epsilonsInsideAOE.add(new Pair<>(epsilonModel, time));
            }
            else Utils.pairArrayRemoveKey(epsilonsInsideAOE, epsilonModel);
        }
    }
    public void updateCentersMemory(int time) {
        if (centersPointMemory.isEmpty() || time - centersPointMemory.get(centersPointMemory.size() - 1).getSecond() >= 200) {
            centersPointMemory.add(new Pair<>(super.getCenter(), time));
        }
        if (centersPointMemory.size() > 25) {
            centersPointMemory.remove(0);
        }
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < archmireEnemiesList.size(); i++) {
            if (archmireEnemiesList.get(i).getId().equals(id)) {
                archmireEnemiesList.remove(i);
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
        for (int i = 0; i < ArchmireEnemyView.archmireEnemyViewsList.size(); i++) {
            if (ArchmireEnemyView.archmireEnemyViewsList.get(i).getId().equals(id)) {
                ArchmireEnemyView.archmireEnemyViewsList.remove(i);
                break;
            }
        }
    }
    @Override
    public boolean isCircular() {
        return false;
    }

    @Override
    public boolean isHovering() {
        return true;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public boolean isStationed() {
        return false;
    }
    public ArrayList<Pair<Enemy, Integer>> getEnemiesInsideDrown() {
        return enemiesInsideDrown;
    }

    public ArrayList<Pair<EpsilonModel, Integer>> getEpsilonsInsideDrown() {
        return epsilonsInsideDrown;
    }

    public ArrayList<Pair<Point2D, Integer>> getCentersPointMemory() {
        return centersPointMemory;
    }

    public ArrayList<Pair<Enemy, Integer>> getEnemiesInsideAOE() {
        return enemiesInsideAOE;
    }

    public ArrayList<Pair<EpsilonModel, Integer>> getEpsilonsInsideAOE() {
        return epsilonsInsideAOE;
    }
}
