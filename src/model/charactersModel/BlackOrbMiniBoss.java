package model.charactersModel;

import controller.Controller;
import controller.Pair;
import controller.Utils;
import model.collision.Line;
import view.charecterViews.BlackOrbMiniBossView;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BlackOrbMiniBoss {

    private final String id;
    private Point2D center;
    private double size;
    private int lastOrbMade;
    private boolean fullyMade;
    private final double orbRadius;
    private ArrayList<Line> orbLasers;
    private ArrayList<Point2D> orbsInitialLocation;
    private ArrayList<Point2D> orbsLocation;
    private ArrayList<OrbEnemy> orbEnemies;
    private ArrayList<Pair<Enemy, Integer>> enemiesLaserCollision;
    private ArrayList<Pair<EpsilonModel, Integer>> epsilonLaserCollision;

    public static ArrayList<BlackOrbMiniBoss> blackOrbMiniBossesList = new ArrayList<>();

    public BlackOrbMiniBoss(Point2D center, double size) {
        this.center = center;
        this.size = size;
        this.id = Utils.processRandomId();
        this.orbRadius = size * Math.sin(Math.PI / 5) / 2;
        orbsInitialLocation = new ArrayList<>();
        orbEnemies = new ArrayList<>();
        orbLasers = new ArrayList<>();
        orbsLocation = new ArrayList<>();
        enemiesLaserCollision = new ArrayList<>();
        epsilonLaserCollision = new ArrayList<>();
        calculateOrbLocations();
        Controller.getINSTANCE().createBlackOrbMiniBossView(this.id);
        blackOrbMiniBossesList.add(this);
    }

    private void calculateOrbLocations() {
        orbsInitialLocation = Utils.circlePartition(center, size, 5);
    }

    public void makeOrbs(int time) {
        if (time - lastOrbMade >= 500 && !fullyMade) {
            makeLasers(orbEnemies.size());
            orbsLocation.add(orbsInitialLocation.get(orbEnemies.size()));
            orbEnemies.add(new OrbEnemy(orbsInitialLocation.get(orbEnemies.size()), orbRadius, 0, this));
            if (orbEnemies.size() == 5) fullyMade = true;
            lastOrbMade = time;
        }
    }
    private void makeLasers(int ind) {
        for (int i = 0; i < ind; i++) {
            orbLasers.add(new Line(orbsInitialLocation.get(i), orbsInitialLocation.get(ind)));
        }
    }

    public void updateEntitiesLaserCollision(int time) {
        for (Enemy enemy : Enemy.enemiesList) {
            boolean eq = false;
            if (enemy instanceof OrbEnemy) for (OrbEnemy ptr : orbEnemies) if (enemy.equals(ptr)) eq = true;

            if (!eq && Utils.checkLinesAndPolygonCollision(orbLasers, enemy.getVertices())) {
                if (!Utils.pairArrayContainsKey(enemiesLaserCollision, enemy)) enemiesLaserCollision.add(new Pair<>(enemy, time));
            }
            else Utils.pairArrayRemoveKey(enemiesLaserCollision, enemy);
        }
        for (EpsilonModel epsilonModel : EpsilonModel.epsilonModelsList) {
            if (Utils.checkLinesAndPolygonCollision(orbLasers, Utils.circlePartition(epsilonModel.getCenter(), epsilonModel.getRadius(), 36))) {
                if (!Utils.pairArrayContainsKey(epsilonLaserCollision, epsilonModel)) {
                    epsilonLaserCollision.add(new Pair<>(epsilonModel, time));
                }
            }
            else Utils.pairArrayRemoveKey(epsilonLaserCollision, epsilonModel);
        }
    }

    public void removeEnemy(OrbEnemy enemy) {
        for (int i = 0; i < orbEnemies.size(); i++) {
            if (orbEnemies.get(i).equals(enemy)) {
                orbEnemies.remove(i);
                removeLasers(i);
                orbsLocation.remove(i);
                break;
            }
        }
        if (orbEnemies.isEmpty()) removeFromAllList(id);
    }
    private void removeLasers(int ind) {
        for (int i = 0; i < orbsLocation.size(); i++) {
            if (i == ind) continue;
            Line l1 = new Line(orbsLocation.get(i), orbsLocation.get(ind));
            Line l2 = new Line(orbsLocation.get(ind), orbsLocation.get(i));
            for (int j = 0; j < orbLasers.size(); j++) {
                if (orbLasers.get(j).equals(l1) || orbLasers.get(j).equals(l2)) {
                    orbLasers.remove(j--);
                }
            }
        }
    }

    public static void removeFromAllList(String id) {
        for (int i = 0; i < blackOrbMiniBossesList.size(); i++) {
            if (blackOrbMiniBossesList.get(i).getId().equals(id)) {
                blackOrbMiniBossesList.remove(i);
                break;
            }
        }
        for (int i = 0; i < BlackOrbMiniBossView.blackOrbMiniBossViewsList.size(); i++) {
            if (BlackOrbMiniBossView.blackOrbMiniBossViewsList.get(i).getId().equals(id)) {
                BlackOrbMiniBossView.blackOrbMiniBossViewsList.remove(i);
                break;
            }
        }
    }

    public String getId() {
        return id;
    }

    public boolean isFullyMade() {
        return fullyMade;
    }

    public ArrayList<Line> getOrbLasers() {
        return orbLasers;
    }

    public ArrayList<Point2D> getOrbsLocation() {
        return orbsLocation;
    }

    public ArrayList<OrbEnemy> getOrbEnemies() {
        return orbEnemies;
    }

    public ArrayList<Pair<Enemy, Integer>> getEnemiesLaserCollision() {
        return enemiesLaserCollision;
    }

    public ArrayList<Pair<EpsilonModel, Integer>> getEpsilonLaserCollision() {
        return epsilonLaserCollision;
    }

    public double getOrbRadius() {
        return orbRadius;
    }

    public Point2D getCenter() {
        return center;
    }

    public double getSize() {
        return size;
    }
}
