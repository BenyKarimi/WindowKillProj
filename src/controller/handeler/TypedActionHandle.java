package controller.handeler;

import controller.Controller;
import controller.constant.KeyActions;
import model.charactersModel.EpsilonModel;
import view.container.GamePanel;
import view.container.GlassFrame;
import view.container.StorePanel;

import java.awt.*;

public class TypedActionHandle {
    private static boolean up = false;
    private static boolean down = false;
    private static boolean left = false;
    private static boolean right = false;

    public static void handlePressedKey(int keyCode) {
        if (keyCode == KeyActions.skillTreeAttack || keyCode == KeyActions.skillTreeDefence || keyCode == KeyActions.skillTreeProteus) {
            SkillTreeHandled.handlePressedKeys(keyCode);
        }
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
        else if (keyCode == KeyActions.showStore && !StoreActionHandle.isFreezing()) {
            if (StorePanel.getNow() != null) return;
            Controller.getINSTANCE().updater.getViewUpdater().stop();
            Controller.getINSTANCE().updater.getModelUpdater().stop();
            GlassFrame.getINSTANCE().getTimer().Stop();
            for (GamePanel panel : GamePanel.gamePanelList) panel.setVisible(false);
            new StorePanel();
        }
    }
    public static void handleReleasedKey(int keyCode) {
        EpsilonModel model = Controller.getINSTANCE().logic.epsilon;
        if (keyCode == KeyActions.UP) {
            up = false;
            model.setyVelocity(0);
        }
        else if (keyCode == KeyActions.DOWN) {
            down = false;
            model.setyVelocity(0);
        }
        else if (keyCode == KeyActions.LEFT) {
            left = false;
            model.setxVelocity(0);
        }
        else if (keyCode == KeyActions.RIGHT) {
            right = false;
            model.setxVelocity(0);
        }
    }
    public static void doMove() {
        EpsilonModel model = Controller.getINSTANCE().logic.epsilon;
        int yMove = (down ? 1 : 0) - (up ? 1 : 0);
        int xMove = (right ? 1 : 0) - (left ? 1 : 0);
        model.moveWithKeys(xMove, yMove);
    }

    public static void setUp(boolean up) {
        TypedActionHandle.up = up;
    }

    public static void setDown(boolean down) {
        TypedActionHandle.down = down;
    }

    public static void setLeft(boolean left) {
        TypedActionHandle.left = left;
    }

    public static void setRight(boolean right) {
        TypedActionHandle.right = right;
    }
}
