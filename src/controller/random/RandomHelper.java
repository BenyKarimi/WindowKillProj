package controller.random;

import controller.Pair;
import controller.Utils;
import controller.constant.GameValues;
import controller.constant.Level;
import model.charactersModel.BarricadosEnemy;
import model.charactersModel.BlackOrbMiniBoss;
import model.charactersModel.OmenoctEnemy;
import model.panelModel.WallSideIndicator;
import view.container.GlassFrame;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static controller.constant.Constants.ENEMY_SIZE_EASY_LEVEL;
import static controller.constant.Constants.ENEMY_SPEED_EASY_LEVEL;

public class RandomHelper {
    private static Random random = new Random();
    public static boolean squareEnemyDash() {
        int tmp = random.nextInt(0, 4);
        return tmp == 1;
    }
    public static double squareEnemySpeed(double currentSpeed) {
        return random.nextDouble(currentSpeed * 1.5, currentSpeed * 3);
    }
    public static int randomFirstWaveEnemyType() {
//        return random.nextInt(0, 2);
        return 0;
    }
    private static int randomSecondWaveEnemyType() {
        int type = 0;
        while (true) {
            if (GameValues.waveNumber < 7) {
                type = random.nextInt(1, 7);
            }
            else {
                if (BarricadosEnemy.barricadosEnemiesList.isEmpty()) {
                    type = 7;
                    break;
                }
                if (BlackOrbMiniBoss.blackOrbMiniBossesList.isEmpty()) {
                    type = 8;
                    break;
                }

                type = random.nextInt(1, 9);
            }
            if (!(type == 3 && OmenoctEnemy.omenoctEnemyList.size() == 4)) break;
        }
        return type;
    }
    public static WallSideIndicator omenoctWallSide() {
        boolean[] marked = new boolean[4];
        for (OmenoctEnemy enemy : OmenoctEnemy.omenoctEnemyList) {
            if (enemy.getWallSideIndicator().equals(WallSideIndicator.LEFT)) marked[0] = true;
            if (enemy.getWallSideIndicator().equals(WallSideIndicator.UP)) marked[1] = true;
            if (enemy.getWallSideIndicator().equals(WallSideIndicator.RIGHT)) marked[2] = true;
            if (enemy.getWallSideIndicator().equals(WallSideIndicator.DOWN)) marked[3] = true;
        }

        int num;
        do {
            num = random.nextInt(0, 4);
        } while (marked[num]);

        if (num == 0) return WallSideIndicator.LEFT;
        if (num == 1) return WallSideIndicator.UP;
        if (num == 2) return WallSideIndicator.RIGHT;
        return WallSideIndicator.DOWN;
    }
    public static boolean barricadosType() {
        int type = random.nextInt(0, 2);
        return type != 0;
    }
    public static double randomWaveEnemySpeed() {
        if (GameValues.level.equals(Level.EASY)) {
            return random.nextDouble(ENEMY_SPEED_EASY_LEVEL, ENEMY_SPEED_EASY_LEVEL + 1);
        }
        else if (GameValues.level.equals(Level.MEDIUM)) {
            return random.nextDouble(ENEMY_SPEED_EASY_LEVEL + 1, ENEMY_SPEED_EASY_LEVEL + 2);
        }
        else return random.nextDouble(ENEMY_SPEED_EASY_LEVEL + 2, ENEMY_SPEED_EASY_LEVEL + 3);
    }
    public static double randomWaveEnemySize() {
        if (GameValues.level.equals(Level.EASY)) {
            return random.nextDouble(ENEMY_SIZE_EASY_LEVEL - 10, ENEMY_SIZE_EASY_LEVEL);
        }
        else if (GameValues.level.equals(Level.MEDIUM)) {
            return random.nextDouble(ENEMY_SIZE_EASY_LEVEL - 20, ENEMY_SIZE_EASY_LEVEL - 10);
        }
        else {
            return random.nextDouble(ENEMY_SIZE_EASY_LEVEL - 30, ENEMY_SIZE_EASY_LEVEL - 20);
        }
    }
    private static ArrayList<Integer> randomArrayListIndices(int num, int listSize) {
        ArrayList<Integer> out = new ArrayList<>();
        if (num >= listSize) {
            for (int i = 0; i < listSize; i++) out.add(i);
        }
        else {
            while (out.size() < num) {
                int tmp = random.nextInt(0, listSize);
                if (!out.contains(tmp)) out.add(tmp);
            }
        }
        return out;
    }
    public static ArrayList<Pair<Point2D, Integer>> randomSecondWaveEnemyCentersAndType() {
        ArrayList<Rectangle2D> candidate = Utils.getCandidateRectangles();
        double tmp = 0;
        if (GameValues.level.equals(Level.EASY)) tmp = 1;
        else if (GameValues.level.equals(Level.MEDIUM)) tmp = 1.5;
        else tmp = 2;

        int num = (int) random.nextDouble((tmp * (GameValues.waveNumber - 3)) + GameValues.waveLengthTime / 30000.0, (tmp * (GameValues.waveNumber - 3)) + GameValues.waveLengthTime / 30000.0 + 2);
//        int num = 1;
        ArrayList<Integer> indices = randomArrayListIndices(num, candidate.size());

        ArrayList<Pair<Point2D, Integer>> out = new ArrayList<>();
        for (Integer ptr : indices) {
            Point2D center = new Point2D.Double(random.nextDouble(candidate.get(ptr).getX(), candidate.get(ptr).getX() + candidate.get(ptr).getWidth()),
                    random.nextDouble(candidate.get(ptr).getY(), candidate.get(ptr).getY() + candidate.get(ptr).getHeight()));
            out.add(new Pair<>(center, randomSecondWaveEnemyType()));
        }
        return out;
    }
    public static ArrayList<Point2D> randomFirstWaveEnemyCenters(double x, double y, double width, double height) {
        int tmp = 0;
        if (GameValues.level.equals(Level.EASY)) tmp = 1;
        else if (GameValues.level.equals(Level.MEDIUM)) tmp = 2;
        else tmp = 3;

        int num = random.nextInt((tmp * GameValues.waveNumber), (tmp * GameValues.waveNumber) + 3);
        ArrayList<Point2D> out = new ArrayList<>();

        while (out.size() < num) {
            Point2D test = new Point2D.Double(random.nextDouble(x, x + width), random.nextDouble(y, y + height));
            boolean ok = true;
            for (Point2D ptr : out)
                if (ptr.equals(test)) {
                    ok = false;
                    break;
                }
            if (ok) out.add(test);
        }
        return out;
    }
}
