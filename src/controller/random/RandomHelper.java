package controller.random;

import controller.constant.Constants;
import controller.constant.GameValues;
import controller.constant.Level;
import view.container.GamePanel;

import java.awt.geom.Point2D;
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
    public static int randomWaveEnemyType() {
        return random.nextInt(0, 2);
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
    public static ArrayList<Point2D> randomWaveEnemyCenters(double x, double y, double width, double height) {
        int tmp = 0;
        if (GameValues.level.equals(Level.EASY)) tmp = 1;
        else if (GameValues.level.equals(Level.MEDIUM)) tmp = 2;
        else tmp = 3;

        int num = random.nextInt((tmp * GameValues.waveNumber), (tmp * GameValues.waveNumber) + 3);
        ArrayList<Point2D> out = new ArrayList<>();

        while (out.size() < num) {
            Point2D test = new Point2D.Double(random.nextDouble(x, x + width), random.nextDouble(y, y + width));
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
