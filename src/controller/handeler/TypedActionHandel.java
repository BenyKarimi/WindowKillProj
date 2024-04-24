package controller.handeler;

import controller.Controller;
import controller.constant.KeyActions;
import model.charactersModel.EpsilonModel;
import view.container.GamePanel;
import view.container.StorePanel;

import java.awt.*;

public class TypedActionHandel {
    static boolean up = false;
    static boolean down = false;
    static boolean left = false;
    static boolean right = false;

    public static void handlePressedKey(int keyCode) {
        if (keyCode == KeyActions.UP) {
            up = true;
        }
        else if (keyCode == KeyActions.DOWN) {
            down = true;
        }
        else if (keyCode == KeyActions.LEFT) {
            left = true;
        }
        else if (keyCode == KeyActions.RIGHT) {
            right = true;
        }
        else if (keyCode == KeyActions.showStore) {
            if (StorePanel.getNow() != null) return;
            Controller.getINSTANCE().updater.getViewUpdater().stop();
            Controller.getINSTANCE().updater.getModelUpdater().stop();
            GamePanel.getINSTANCE().getTimer().Stop();
            GamePanel.getINSTANCE().setVisible(false);
            new StorePanel();
        }
    }
    public static void handleReleasedKey(int keyCode) {
        EpsilonModel model = Controller.getINSTANCE().logic.epsilon;
        if (keyCode == KeyActions.UP) {
            up = false;
            model.yVelocity = 0;
        }
        else if (keyCode == KeyActions.DOWN) {
            down = false;
            model.yVelocity = 0;
        }
        else if (keyCode == KeyActions.LEFT) {
            left = false;
            model.xVelocity = 0;
        }
        else if (keyCode == KeyActions.RIGHT) {
            right = false;
            model.xVelocity = 0;
        }
    }
    public static void doMove() {
        EpsilonModel model = Controller.getINSTANCE().logic.epsilon;
        Dimension panelSize = new Dimension(GamePanel.getINSTANCE().getWidth(), GamePanel.getINSTANCE().getHeight());
        if (up && !down) {
            if (right && !left) model.moveWithKeys(1, -1, panelSize);
            else if (left && !right) model.moveWithKeys(-1, -1, panelSize);
            else model.moveWithKeys(0, -1, panelSize);
        }
        else if (down && !up) {
            if (right && !left) model.moveWithKeys(1, 1, panelSize);
            else if (left && !right) model.moveWithKeys(-1, 1, panelSize);
            else model.moveWithKeys(0, 1, panelSize);
        }
        else {
            if (right && !left) model.moveWithKeys(1, 0, panelSize);
            else if (left && !right) model.moveWithKeys(-1, 0, panelSize);
        }
    }
}
