package controller.constant;

import java.awt.*;


public class Constants {
    public static final Dimension GLASS_FRAME_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    public static final Dimension GAME_PANEL_INITIAL_DIMENSION = new Dimension(700, 700);
    public static final int FPS = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate();
    public static final double FRAME_UPDATE_TIME=(double) 1000/FPS;
    public static final int UPS = 100;
    public static final double MODEL_UPDATE_TIME=(double) 1000/UPS;
    public static final int EPSILON_RADIUS = 25;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_HP = 100;
    public static final Dimension GAME_PANEL_START_DIMENSION = new Dimension(300, 300);
}
