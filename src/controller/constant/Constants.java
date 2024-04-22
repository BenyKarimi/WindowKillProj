package controller.constant;

import java.awt.*;


public class Constants {
    public static final Dimension GLASS_FRAME_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    public static final Dimension GAME_PANEL_INITIAL_DIMENSION = new Dimension(700, 700);
    public static final int FPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate();
    public static final double FRAME_UPDATE_TIME=(double) 1000/FPS;
    public static final int UPS = 100;
    public static final double MODEL_UPDATE_TIME=(double) 1000/UPS;
    public static final double EPSILON_RADIUS = 25;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_HP = 100;
    public static final Dimension GAME_PANEL_START_DIMENSION = new Dimension(400, 400);
    public static final int TRIANGLE_ENEMY_HP = 15;
    public static final int TRIANGLE_ENEMY_REDUCER_HP = 10;
    public static final int TRIANGLE_ENEMY_COLLECTIBLE_NUMBER = 2;
    public static final int TRIANGLE_ENEMY_COLLECTIBLE_XP = 5;
    public static final int SQUARE_ENEMY_HP = 10;
    public static final int SQUARE_ENEMY_REDUCER_HP = 6;
    public static final int SQUARE_ENEMY_COLLECTIBLE_NUMBER = 1;
    public static final int SQUARE_ENEMY_COLLECTIBLE_XP = 5;
    public static final int COLLECTIBLE_EXISTENCE = 4;
    public static final int COLLECTIBLE_SIZE = 4;
    public static final int IMPACT_RADIUS = 250;
    public static final double ENEMY_SPEED_EASY_LEVEL = 1;
    public static final double ENEMY_SIZE_EASY_LEVEL = 60;
}
